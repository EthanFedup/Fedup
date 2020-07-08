package com.evertech.core.fetch

import android.annotation.SuppressLint

import com.blankj.utilcode.util.NetworkUtils
import com.evertech.core.fetch.handler.BaseResultHandler
import com.evertech.core.fetch.handler.PageResultHandler
import com.evertech.core.fetch.strategy.BaseFetchStrategy
import com.evertech.core.fetch.strategy.PageFetchStrategy
import com.evertech.core.mvp.view.IView
import com.evertech.core.util.ThreadHelper
import io.reactivex.Flowable

/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/2/2020 5:04 PM
 *    desc   :
 */
class PageFetcher<R>(source: Flowable<R>) : BaseFetcher<R>(source) {
    
    
   

    /* UI关联操作汇总 */
    protected var mFS: PageFetchStrategy

    init {
        mFS = PageFetchStrategy()
    }

    override fun buildResultHandler(): BaseResultHandler<R> {
        return PageResultHandler(mOnSuccess, mOnBizError, mOnError, mFS)
    }

    override fun rawStart(handler: BaseResultHandler<R>) {
        /* 没有绑定IView，却需要IView相关的交互 */
        require(!(!hasIView() && needIView())) { "需要先绑定IView实现UI关联操作" }
        /* 需要无网提示. 言外之意IView不为空 */
        if (!NetworkUtils.isConnected()) {
            postNoNetException(handler)
            return
        }
        rebuildSource().subscribe(handler)
    }


    private fun rebuildSource(): Flowable<R>{
        if (mFS.enableCommonLoadingView || mFS.enableCommonLoadingViewWithoutDismiss) {
            mFS.view!!.loadingView.show()
        }
        if (hasIView()) {
            mSource = attachToLifecycle()
        }
        return mSource!!.compose(ThreadHelper.APPLY_SCHEDULERS) as  Flowable<R>
    }

    /**
     * 可选，若无需[IView]关联的操作，可以不设置。
     */
    fun withView(view: IView?): PageFetcher<R> {
        mFS.view = view
        if (view != null && view!!.stateView != null) {
            view!!.stateView.setFetcher(this)
        }
        return this
    }

    /**
     * 可选，关闭显示加载页。默认显示。
     * 与[.enableCommonLoadingViewWithoutDismiss]互斥。
     */
    fun disableCommonLoadingView(): PageFetcher<R> {
        mFS.enableCommonLoadingView = false
        mFS.enableCommonLoadingViewWithoutDismiss = false
        return this
    }

    /**
     * 可选，显示加载页，但不让框架取消。默认显示。
     * 与[.disableCommonLoadingView]互斥。
     */
    fun enableCommonLoadingViewWithoutDismiss(): PageFetcher<R> {
        mFS.enableCommonLoadingViewWithoutDismiss = true
        mFS.enableCommonLoadingView = true
        return this
    }

    /**
     * 可选，关闭"无网”提示的Toast。默认显示。
     */
    fun disableNoNetToast(): PageFetcher<R> {
        mFS.enableNoNetToast = false
        return this
    }

    /**
     * 可选，可以显示"无网”的状态提示页。默认关闭。
     */
    fun enableNoNetPage(enableNoNetPage: Boolean): PageFetcher<R> {
        mFS.enableNoNetPage = enableNoNetPage
        return this
    }

    /**
     * 可选，关闭“其他异常”的Toast。默认显示。
     */
    fun disableErrorToast(): PageFetcher<R> {
        mFS.enableErrorToast = false
        return this
    }

    /**
     * 可选，可以显示“其他异常”的状态提示页。默认关闭。
     */
    fun enableErrorPage(enableErrorPage: Boolean): PageFetcher<R> {
        mFS.enableErrorPage = enableErrorPage
        return this
    }

    /**
     * 可选，关闭“业务逻辑错误”的Toast。默认显示。
     */
    fun disableBizErrorToast(): PageFetcher<R> {
        mFS.enableBizErrorToast = false
        return this
    }

    override fun fs(): BaseFetchStrategy {
        return mFS
    }

    private fun needIView(): Boolean {
        return mFS.enableCommonLoadingView || mFS.enableCommonLoadingViewWithoutDismiss || mFS.enableErrorPage || mFS.enableNoNetPage
    }

}
