package com.evertech.core.ptrlm;

import android.content.Context;
import android.view.View;

import com.blankj.utilcode.util.ObjectUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.evertech.core.definition.JAction;
import com.evertech.core.definition.JConsumer;
import com.evertech.core.mvp.view.IView;
import com.evertech.core.util.ThreadHelper;

import java.util.Collection;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static com.evertech.core.ptrlm.EmptyView.EMPTY_DATA_COMMON;

/**
 * 下拉刷新/上拉加载封装类
 */
public class PtrLmHandler {

    /* 单页默认数量 */
    public static final int PAGE_COUNT = 15;

    /* 刷新关闭延迟时间 */
    public static final int REFRESH_FINISH_DELAY = 699;

    private PtrFrameLayout mPtrf;

    private RecyclerView mRv;

    private BaseQuickAdapter mAdapter;

    private IView mView;

    private CustomLoadMoreView mLoadMoreView;

    private EmptyView mEmptyView;

    private OnPtrLmListener mPtrLmListener;

    private JConsumer<PtrLmHandler> mOnRefreshConsumer;

    private JConsumer<PtrLmHandler> mOnLoadMoreConsumer;

    /* 是否已经初始化 */
    private boolean hasInitialized;

    /**
     * 与刷新的互斥行为，“上拉加载时不能刷新”，通过{@link PtrDefaultHandler#checkCanDoRefresh(PtrFrameLayout, View, View)}判断.
     * 但是“刷新时不能加载”的约束无需通过flag标记，而是通过{@link BaseQuickAdapter#setEnableLoadMore}进行约束.
     */
    private boolean isLoading;

    /**
     * 是“所有数据加载完毕”，而非“单页加载完毕”.
     * 注: 如果{@link #isLoading}为false，该值没有意义.
     */
    private boolean loadMoreEndNotComplete;

    public interface OnPtrLmListener {
        void onRefresh(PtrLmHandler handler);

        void onLoadMore(PtrLmHandler handler);
    }

    /**
     * 条件：当{@link PtrClassicFrameLayout}的子布局是且只是{@link RecyclerView}时，可以不传{@link RecyclerView}.
     */
    public static PtrLmHandler newHandler(PtrFrameLayout ptrf, BaseQuickAdapter adapter) {
        return new PtrLmHandler(ptrf, adapter);
    }

    /**
     * 条件：当{@link PtrFrameLayout}的子布局必须是{@link RecyclerView}.
     */
    private PtrLmHandler(PtrFrameLayout ptrf, BaseQuickAdapter adapter) {
        mPtrf = ptrf;
        mRv = getChildRv(ptrf);
        mAdapter = adapter;
    }

    /**
     * 刷新/加载的回调.
     */
    public PtrLmHandler setListener(OnPtrLmListener listener) {
        mPtrLmListener = listener;
        mOnLoadMoreConsumer = null;
        mOnRefreshConsumer = null;
        doInit();
        return this;
    }

    /**
     * 刷新的回调.
     */
    public PtrLmHandler onRefresh(JConsumer<PtrLmHandler> consumer) {
        mOnRefreshConsumer = consumer;
        mPtrLmListener = null;
        doInit();
        return this;
    }

    /**
     * 加载的回调.
     */
    public PtrLmHandler onLoadMore(JConsumer<PtrLmHandler> consumer) {
        mOnLoadMoreConsumer = consumer;
        mPtrLmListener = null;
        doInit();
        return this;
    }

    /**
     * 初始化
     */
    private void doInit() {
        checkStates();

        if (hasInitialized) return;

        mPtrf.setDurationToCloseHeader(REFRESH_FINISH_DELAY);
        mPtrf.setKeepHeaderWhenRefresh(true);
        mPtrf.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                /* 刷新时不能触发加载事件 */
                mAdapter.setEnableLoadMore(false);
                callOnRefresh();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return !isLoading && PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });

        /* LoadMoreView */
        mAdapter.setLoadMoreView(mLoadMoreView = new CustomLoadMoreView());
        mLoadMoreView.setRecyclerView(mRv);


        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                /* 加载时不能触发刷新事件 */
                isLoading = true;
                PtrLmHandler.this.callOnLoadMore();
            }
        }, mRv);

        hasInitialized = true;
    }

    /**
     * 立即执行自动刷新.
     */
    public void autoRefresh() {
        if (!hasInitialized) return;
        ThreadHelper.runDelayed(new JAction() {
            @Override
            public void run() {
                mPtrf.autoRefresh(true);
            }
        }, 199);
    }

    /**
     * 执行自动刷新，需要一定延迟；一般只有在界面初始化时需要自动刷新的情况.
     */
    public void autoRefreshWithDelay() {
        if (!hasInitialized) return;
        ThreadHelper.runDelayed(new JAction() {
            @Override
            public void run() {
                mPtrf.autoRefresh(true);
            }
        }, 199);
    }

    /**
     * 是否处于刷新中.
     */
    public boolean isRefreshing() {
        return mPtrf.isRefreshing();
    }

    /**
     * 是否处于刷新中，或者加载中.
     */
    public boolean isProcessing() {
        return mPtrf.isRefreshing() || isLoading;
    }

    /**
     * 当前是加载状态时，根据请求结果判断，是“所有数据加载完毕”还是“当前页加载完毕”.
     * 注：非“加载状态”，设置该方法没有意义.
     *
     * @param flag true 表示"所有数据加载完毕".
     */
    public void setLoadMoreEnd(boolean flag) {
        loadMoreEndNotComplete = flag;
        if (true) {
            ThreadHelper.runDelayed(new JAction() {
                @Override
                public void run() {
                    mAdapter.loadMoreEnd();
                }
            }, 300);
        }
    }

    /**
     * 刷新成功/失败后，关闭“刷新中”的UI显示.
     */
    public void stopRefreshing() {
        checkStates();
        mPtrf.refreshComplete();
        mAdapter.setEnableLoadMore(true);
    }

    /**
     * 统一处理“加载成功”后的状态：是“单页加载完毕”还是“所有数据加载完毕”.
     * 前提条件：必须是“加载成功后”.
     */
    public void loadMoreOver() {
        checkStates();
        if (loadMoreEndNotComplete) {
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }
        isLoading = false;
    }

    /**
     * 显示“加载失败”的状态.
     */
    public void loadMoreFail() {
        checkStates();
        mAdapter.loadMoreFail();
        isLoading = false;
    }

    /**
     * 想直接拿到Adapter.
     */
    public BaseQuickAdapter getAdapter() {
        return mAdapter;
    }

    public IView getView() {
        return mView;
    }

    public void setView(IView mView) {
        this.mView = mView;
    }

    /**
     * 通用的安全判断.
     */
    private void checkStates() {
        if (mPtrf == null || mRv == null || mAdapter == null) {
            throw new IllegalArgumentException("参数错误");
        }
    }

    /**
     * 触发“下拉刷新”.
     */
    private void callOnRefresh() {
        if (mPtrLmListener != null) {
            mPtrLmListener.onRefresh(this);
        }

        if (mOnRefreshConsumer != null) {
            mOnRefreshConsumer.accept(this);
        }
    }

    /**
     * 触发“上拉加载”.
     */
    private void callOnLoadMore() {
        if (mPtrLmListener != null) {
            mPtrLmListener.onLoadMore(this);
        }

        if (mOnLoadMoreConsumer != null) {
            mOnLoadMoreConsumer.accept(this);
        }
    }

    /**
     * 构造方法没传RecyclerView时，获取RecyclerView的方式.
     */
    private RecyclerView getChildRv(PtrFrameLayout ptrf) {
        for (int i = 0; i < ptrf.getChildCount(); i++) {
            View child = ptrf.getChildAt(i);
            if (child instanceof RecyclerView) {
                return (RecyclerView) child;
            }
        }
        return null;
    }

    /**
     * 刷新新的数据源（清空之前所有数据）.
     */
    public void setNewData(List data) {
        /* 数据源为空时，并且没添加过EmptyView，添加空数据View */
        mAdapter.setEnableLoadMore(true);
        if (ObjectUtils.isEmpty(data)) {
            if (mEmptyView == null) {
               getEmptyView().setEmptyType(EMPTY_DATA_COMMON);
            }
            mAdapter.setEmptyView(mEmptyView);
        }
        /* 设置数据源 */
        mAdapter.setNewData(data);
    }

    /**
     * 添加数据源.
     * 目前没有特殊处理，为防止像{@link #setNewData}一样未来可能的封装，统一使用该方法.
     */
    public void addData(Collection data) {
        if (ObjectUtils.isEmpty(data)) return;
        mAdapter.addData(data);
    }

    /**
     * 是否显示LoadMoreView.
     */
    public void disableLoadMore() {
        mAdapter.setOnLoadMoreListener(null, mRv);
        mAdapter.loadMoreEnd(true);
    }

    public EmptyView getEmptyView() {
        Context context = mRv.getContext();
        if (mEmptyView == null) {
            mEmptyView = new EmptyView(context);
        }
        return mEmptyView;
    }

}
