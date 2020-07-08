package com.evertech.core.ptrlm;

import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.evertech.core.R;
import com.evertech.core.util.ReflectUtils;
import com.evertech.core.util.ViewHelper;

import androidx.recyclerview.widget.RecyclerView;

public class CustomLoadMoreView extends LoadMoreView {
    /* RV */
    private RecyclerView mRv;

    /* 加载动画 */
    private Animation mAnim;

    /* 是否初始化过动画相关内容 */
    private boolean afterAnimInit;

    /* Items是否铺满当前屏幕 */
    private boolean isItemsFillScreen;

    /* 控件 */
    private FrameLayout flRoot;
    private ImageView ivAnim;

    public CustomLoadMoreView() {
//        mAnim = AnimationHelper.infiniteRotateAnim(500);
    }

    @Override
    public int getLayoutId() {
        return R.layout.load_more_view;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.ll_loading;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.ll_loading_fail;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.ll_loading_end;
    }

    /**
     * @param recyclerView
     */
    public void setRecyclerView(RecyclerView recyclerView) {
        mRv = recyclerView;
    }

    @Override
    public void convert(BaseViewHolder holder) {
        /* 获取当前状态 */
        int loadMoreStatus = ReflectUtils.getLoadMoreStatus(this);

        flRoot = holder.getView(R.id.fl_root);
        ivAnim = holder.getView(R.id.iv_anim);

        /* 数据是否铺满当前屏幕 */
        isItemsFillScreen = ViewHelper.isItemsFillScreen(mRv);
        /* 当没有铺满屏幕时，隐藏LoadMoreView */
        if (!isItemsFillScreen) {
            super.convert(holder);
            flRoot.setVisibility(View.GONE);
            return;
        }

        /* 动画相关逻辑 */
        if (loadMoreStatus == LoadMoreView.STATUS_LOADING) {
            /* 加载中的动画样式 */
            if (ivAnim != null) {
                ivAnim.startAnimation(mAnim);
            }
        } else {
            /* 非加载中状态，停止动画 */
            if (ivAnim != null) {
                ivAnim.clearAnimation();
            }
        }

        super.convert(holder);
    }

}
