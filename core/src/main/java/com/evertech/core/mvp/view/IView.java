package com.evertech.core.mvp.view;

import android.view.View;

import com.evertech.core.widget.PageLoadingView;
import com.evertech.core.widget.StateView;
import com.trello.rxlifecycle2.LifecycleTransformer;


/**
 * The Base 'V' in 'MVP'
 */
public interface IView {

    /* 获取内容页 */
    View getContentView();

    /* 获取状态页 */
    StateView getStateView();

    /* 获加载页 */
    PageLoadingView getLoadingView();

    /* 绑定生命周期 */
    <T> LifecycleTransformer<T> lifecycleTransformer();

}
