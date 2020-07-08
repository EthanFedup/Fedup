package com.evertech.core.fetch.handler

import android.view.View
import com.blankj.utilcode.util.LogUtils

import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.StringUtils
import com.evertech.core.fetch.strategy.BaseFetchStrategy
import com.evertech.core.fetch.strategy.BaseFetchStrategy.Companion.DEFAULT_ERROR_TOAST
import com.evertech.core.net.NetManager
import com.evertech.core.net.exception.EmptyDataException
import com.evertech.core.net.exception.NoNetworkException
import com.evertech.core.net.exception.PureStringResultException
import com.evertech.core.net.response.BizMsg
import com.evertech.core.widget.StateView
import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.hjq.toast.ToastUtils

import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

import java.net.ConnectException
import java.net.SocketTimeoutException

import io.reactivex.annotations.NonNull
import io.reactivex.exceptions.CompositeException
import io.reactivex.functions.Consumer
import okhttp3.ResponseBody
import retrofit2.HttpException

import com.evertech.core.net.ErrorCode.BIZ_STATUS_CODE
import com.evertech.core.net.ErrorCode.BIZ_STATUS_CODE2


/**
 * @Author Shuo
 * @Create 2018-10-26
 */
abstract class BaseResultHandler<T>(/* 业务逻辑成功回调 */
    @param:NonNull private val mOnSuccess: Consumer<T>?, /* 业务逻辑失败回调 */
    @param:NonNull private val mOnBizError: Consumer<BizMsg>?, /* 其他异常会回调 */
    @param:NonNull private val mOnError: Consumer<Throwable>?
) : Subscriber<T> {

    /* 是否获取到数据. 包含本地缓存的情况 (只针对本次请求) */
    private var hasGotData: Boolean = false

    /* 当前页是否已经有数据(前提是必须要有IView，因为没有IView也就没意义去显示空数据页) */
    protected val isLatestDataEmpty: Boolean
        get() = (hasIView()
                && fs()!!.view!!.stateView != null
                && fs()!!.view!!.stateView.isDataEmpty)

    override fun onSubscribe(s: Subscription) {
        s.request(Integer.MAX_VALUE.toLong())
    }

    override fun onNext(t: T) {
        /* 获取到的数据是否为空 */
        if (!ObjectUtils.isEmpty(t)) hasGotData = true
        handleOnSuccess(t)
    }

    override fun onComplete() {
        /* 如果没数据，则不会走onNext */
        if (!hasGotData) handleOnSuccess(null)
        onFinalComplete()
    }

    override fun onError(e: Throwable) {
        /* 异常分析 */
        if (e is CompositeException) {
            /* Composite异常 */
            handleCompositeException(e)
        } else if (e is NoNetworkException) {
            /* 无网情况 */
            handleNoNetError(e)
        } else if (e is HttpException) {
            /* 特殊处理 */
            handleHttpError(e)
        } else if (e is SocketTimeoutException || e is ConnectException) {
            /* 网络异常 */
            handleOtherError(e)
        } else if (e is JsonIOException || e is JsonSyntaxException || e is JsonParseException) {
            /* 解析异常 */
            handleOtherError(e)
        } else if (e is EmptyDataException) {
            /* 空数据 */
            handleEmptyDataException(e)
        } else if (e is PureStringResultException) {
            handlePureStringResultException(e)
        } else {
            /* 其他异常 */
            handleOtherError(e)
        }
        onFinalError()
    }

    /**
     * “业务逻辑成功”处理.
     * 大概率发生在[.onNext]中，小概率[.onComplete].
     */
    protected fun handleOnSuccess(t: T?) {
        if (mOnSuccess != null) {
            try {
                mOnSuccess.accept(t)
            } catch (e: Exception) {
                /* 非常重要，减少crash率！ 对于业务逻辑成功 的处理过程中引发的异常，都被拦截在此 */
                e.printStackTrace()
                /* 执行过程中又出现的异常，最后还是会传给onError处理 */
                //                handleOtherError(e);
            }

        }
    }


    /**
     * 汇总的异常.
     */
    private fun handleCompositeException(e: Throwable) {
        val ce = e as CompositeException
        val exceptions = ce.exceptions
        for (innerException in exceptions) {
            if (innerException is HttpException) {
                handleHttpError(innerException)
                break
            } else if (innerException is EmptyDataException) {
                handleEmptyDataException(innerException)
                break
            }
        }
    }

    /**
     * 无网的处理.
     */
    private fun handleNoNetError(e: Throwable) {
        handleOtherError(e)
        commonNoNetHandling()
    }

    /**
     * 如果错误码是401/403，则意味着“业务逻辑错误”.
     * 该情况下需要特殊处理.
     */
    private fun handleHttpError(e: Throwable) {
        val t = e as HttpException
        /* String url = t.response().raw().request().url().url().toString(); */
        if (t.code() == BIZ_STATUS_CODE || t.code() == BIZ_STATUS_CODE2) {
            val body = e.response()!!.errorBody()
            val bizMsg = NetManager.getInstance().convertBody<BizMsg>(BizMsg::class.java, body)
            if (bizMsg != null) {
                handleBizError(bizMsg)
                return
            }
        }
        handleOtherError(t)
    }

    /**
     * “业务逻辑错误”处理.
     * 只会发生在[.onError]中.
     */
    private fun handleBizError(bizMsg: BizMsg) {
        if (mOnBizError != null) {
            try {
                if (!onBizIntercept(bizMsg)) {
                    mOnBizError.accept(bizMsg)
                }
            } catch (e: Exception) {
                /* 非常重要，减少crash率！ 对于业务逻辑错误 的处理过程中引发的异常 都被拦截于此 */
                e.printStackTrace()
                /* 执行过程中又出现的异常，最后还是会传给onError处理 */
                //                handleOtherError(e);
            }

        }
        /* 结束前给个提示 */
        showBizToast(bizMsg)
    }

    /**
     * 空数据异常.
     * 只会发生在[.onError]中.
     */
    private fun handleEmptyDataException(t: Throwable) {
        handleOnSuccess(null)
        onFinalComplete()
    }

    /**
     * 不需要解析，只要纯的请求结果.
     * 只会发生在[.onError]中.
     */
    private fun handlePureStringResultException(t: Throwable) {
        val stringResult = (t as PureStringResultException).stringResult
        handleOnSuccess(stringResult as T)
        onFinalComplete()
    }

    /**
     * “其他错误”处理.
     */
    protected fun handleOtherError(t: Throwable) {
        if (mOnError != null) {
            try {
                mOnError.accept(t)
            } catch (e: Exception) {
                /* 非常重要，减少crash率！ 对于其他异常 的处理过程中又引发的异常 都被拦截于此.
                   如果异常处理执行过程中还出现异常，那么神都救不了了. */
                e.printStackTrace()
            }

        }
        if (t !is NoNetworkException) {
            /* 最后给出提示信息 */
            commonErrorHandling()
        }

    }

    /**
     * 需要进行拦截的“业务逻辑错误”.
     *
     * @param bizMsg 业务逻辑信息.
     * @return 默认false不拦截.
     */
    protected fun onBizIntercept(bizMsg: BizMsg): Boolean {
        /* Token拦截不在这里处理 */
        return false
    }

    abstract fun fs(): BaseFetchStrategy?

    /**
     * 通用的无网处理
     */
    protected fun commonNoNetHandling() {
        if (hasIView()) {
            if (fs()!!.enableNoNetPage) {
                /* 开启无网提示页的情况 */
                val stateView = fs()!!.view?.stateView
                if (stateView != null && stateView.isDataEmpty) {
                    /* 当前是空数据 */
                    stateView.showNoNetworkView()
                } else {
                    /* 当前有数据 */
                    showNoNetToast()
                }
            } else {
                /* 关闭无网提示页的情况 */
                showNoNetToast()
            }
        } else {
            /* 没有IView的情况 */
            showNoNetToast()
        }
    }

    /**
     * 通用的“其他错误”提示信息.
     */
    protected open fun commonErrorHandling() {
        if (hasIView()) {
            if (isLatestDataEmpty && fs()!!.enableErrorPage) {
                /* 有View且之前没数据，并且没有禁止显示状态页 */
                fs()!!.view!!.stateView.showErrorView()
            } else {
                /* 有View且之前有数据，或者了禁止状态页 */
                showOtherErrorToast()
            }
        } else {
            /* 没有View的情况 */
            showOtherErrorToast()
        }
    }

    /* 成功后最后要处理的事 */
    protected abstract fun onFinalComplete()

    /* 失败后最后要处理的事 */
    protected abstract fun onFinalError()

    /* “无网”Toast */
    private fun showNoNetToast() {
        if (fs()!!.enableNoNetToast) {
            ToastUtils.show("无网络连接")
        }
    }

    /* “业务逻辑错误”Toast */
    private fun showBizToast(bizMsg: BizMsg) {
        if (!fs()!!.enableBizErrorToast) {
            return
        }
        if (StringUtils.isTrimEmpty(bizMsg.msg)) {
            bizMsg.msg = BaseFetchStrategy.DEFAULT_BIZ_ERROR_TOAST
        }
        ToastUtils.show(bizMsg.msg)
    }

    /* “其他错误”Toast */
    private fun showOtherErrorToast() {
        if (fs()!!.enableErrorToast) {
            ToastUtils.show(DEFAULT_ERROR_TOAST)
        }
    }

    /* 是否有IView */
    protected fun hasIView(): Boolean {
        return fs() != null && fs()!!.view != null
    }

    /* 隐藏状态页 */
    protected fun hideStateView() {
        if (hasIView() && fs()!!.view!!.stateView != null) {
            fs()!!.view!!.stateView.visibility = View.GONE
        }
    }

}
