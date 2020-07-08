package com.evertech.core.fetch


import com.evertech.core.fetch.handler.BaseResultHandler
import com.evertech.core.fetch.strategy.BaseFetchStrategy
import com.evertech.core.mvp.view.BaseActivity
import com.evertech.core.mvp.view.BaseFragment
import com.evertech.core.mvp.view.IView
import com.evertech.core.net.exception.NoNetworkException
import com.evertech.core.net.response.BizMsg
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.FragmentEvent

import java.util.concurrent.Callable

import io.reactivex.Flowable
import io.reactivex.functions.Consumer

/**
 * author : Ethan
 * e-mail : Ethan@fedup.cn
 * date   : 7/2/2020 3:57 PM
 * desc   :
 */
abstract class BaseFetcher<R>( source: Flowable<R>) {
    /* 业务逻辑成功回调 */
    protected lateinit var mOnSuccess: Consumer<R>

    /* 业务逻辑失败回调 */
    protected lateinit var mOnBizError: Consumer<BizMsg>

    /* 其他异常回调 */
    protected lateinit var mOnError: Consumer<Throwable>

    /* 数据源（本地＋网络/纯本地/纯网络) */
    protected var mSource: Flowable<R>?

    /**
     * 开始获取（请求/加载）数据
     */
    fun start() {
        requireNotNull(mSource) { "数据源不能为空" }
        rawStart(buildResultHandler())
    }

    /**
     * 设置“业务逻辑成功”回调
     */
    fun onSuccess(onSuccess: Consumer<R>): BaseFetcher<R> {
        mOnSuccess = onSuccess
        return this
    }

    /**
     * 设置“业务逻辑错误”回调
     */
    fun onBizError(onBizError: Consumer<BizMsg>): BaseFetcher<R> {
        mOnBizError = onBizError
        return this
    }

    /**
     * 设置“其他异常”回调
     */
    fun onError(onError: Consumer<Throwable>): BaseFetcher<R> {
        mOnError = onError
        return this
    }

    /**
     * 抛出网络异常，交给[BaseResultHandler]处理
     */
    protected fun postNoNetException(handler: BaseResultHandler<R>) {
        Flowable.fromCallable { throw NoNetworkException() }.subscribe(handler)
    }

    protected fun attachToLifecycle(): Flowable<R>? {
        val iView = fs().view
        if (iView != null) {
            if (iView is BaseActivity) {
                return mSource!!.compose(
                    iView
                        .bindUntilEvent<Any>(ActivityEvent.DESTROY)
                ) as Flowable<R>
            } else if (iView is BaseFragment) {
                return mSource!!.compose(
                    iView
                        .bindUntilEvent<Any>(FragmentEvent.DESTROY)
                ) as Flowable<R>
            }
        }
        return null
    }

    protected abstract fun rawStart(handler: BaseResultHandler<R>)

    protected abstract fun buildResultHandler(): BaseResultHandler<R>

    protected abstract fun fs(): BaseFetchStrategy

    protected fun hasIView(): Boolean {
        return fs().view != null
    }

    init {
        mSource = source
    }

}
