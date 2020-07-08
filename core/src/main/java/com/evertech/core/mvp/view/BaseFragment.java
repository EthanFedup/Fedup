package com.evertech.core.mvp.view;

import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.evertech.core.definition.GlobalEvent;
import com.evertech.core.mvp.presenter.IPresenter;
import com.evertech.core.mvp.presenter.PresenterDelegate;
import com.evertech.core.ptrlm.EmptyView;
import com.evertech.core.widget.PageLoadingView;
import com.evertech.core.widget.StateView;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

import static com.evertech.core.util.ViewHelper.MATCH;
import static com.evertech.core.util.ViewHelper.newFrameParams;

/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/3/2020 11:22 AM
 *    desc   :
 */
public abstract class BaseFragment extends RxFragment implements IView {
    /**
     * Presenter代理.
     */
    private PresenterDelegate mPresenterDelegate;

    /**
     * 复用的View.
     */
    private FrameLayout mView;

    /**
     * 真正意义上的内容View.
     */
    private View mContentView;

    /**
     * 全局加载页
     */
    private PageLoadingView mLoadingView;

    /**
     * 状态页，包含几种常用状态.
     */
    private StateView mStateView;

    /**
     * 空白页.
     */
    private EmptyView mEmptyView;

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            onRecycled(savedInstanceState);
        }
    }

    /**
     * 系统回收后的处理. 子类可以覆写，进行特殊处理（如恢复先前界面的显示等）
     */
    protected void onRecycled(Bundle savedInstanceState) {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = buildContentView();
            routineTasks();
        }
        return mView;
    }

    /**
     * 获取Framgent的根View.
     */
    public FrameLayout getRootView() {
        return mView;
    }

    /**
     * 常规初始化内容
     */
    private void routineTasks() {
        prepareTools();
        prepareDataAndViews();
        preparePresenter();
        onFinishInit();
    }

    private void prepareTools() {
        EventBus.getDefault().register(this);
    }

    private void prepareDataAndViews() {
        ButterKnife.bind(this, mView);
        /* Presenter代理初始化 */
        mPresenterDelegate = new PresenterDelegate();

        /* 抛给子类自由定制初始化 */
        initData();
        initViews();
    }

    protected FrameLayout buildContentView() {
        FrameLayout rootView = new FrameLayout(getActivity());

        mContentView = getActivity().getLayoutInflater().inflate(layoutResID(), rootView, false);

        mStateView = new StateView(getActivity());
        mStateView.setVisibility(View.GONE);

        mEmptyView = new EmptyView(getActivity());
        mEmptyView.setVisibility(View.GONE);

        rootView.addView(mContentView, newFrameParams(MATCH, MATCH));
        rootView.addView(mStateView, newFrameParams(MATCH, MATCH));
        rootView.addView(mEmptyView, newFrameParams(MATCH, MATCH));

        return rootView;
    }

    private void preparePresenter() {
        mPresenterDelegate.init(this);
        mPresenterDelegate.openDataFetching();
    }

    /**
     * 抛给子类，所有初始化完毕后的回调
     */
    protected void onFinishInit() {
    }


    protected void addPresenter(IPresenter presenter) {
        mPresenterDelegate.add(presenter);
    }

    @LayoutRes
    protected abstract int layoutResID();

    protected void initData() {
    }

    protected abstract void initViews();

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    @CallSuper
    public void onStart() {
        super.onStart();
        if (mPresenterDelegate != null) {
            mPresenterDelegate.onStart();
        }
    }

    @Override
    @CallSuper
    public void onResume() {
        super.onResume();
        if (mPresenterDelegate != null) {
            mPresenterDelegate.onResume();
        }
        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    @Override
    @CallSuper
    public void onPause() {
        super.onPause();
        if (mPresenterDelegate != null) {
            mPresenterDelegate.onPause();
        }
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }

    @Override
    @CallSuper
    public void onStop() {
        if (mPresenterDelegate != null) {
            mPresenterDelegate.onStop();
        }
        if (mLoadingView != null) {
            mLoadingView.dismiss();
        }
        super.onStop();
    }

    @Override
    @CallSuper
    public void onDestroy() {
        if (mPresenterDelegate != null) {
            mPresenterDelegate.onDestroy();
        }
        unregisterTools();
        super.onDestroy();
    }

    private void unregisterTools() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View getContentView() {
        return mContentView;
    }

    @Override
    public StateView getStateView() {
        return mStateView;
    }

    public EmptyView getEmptyView() {
        return mEmptyView;
    }

    @Override
    public PageLoadingView getLoadingView() {
        return mLoadingView == null ? mLoadingView = new PageLoadingView(getContext()) : mLoadingView;

    }


    @Override
    public <T> LifecycleTransformer<T> lifecycleTransformer() {
        return bindToLifecycle();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGlobalEvent(GlobalEvent event) {
    }
}
