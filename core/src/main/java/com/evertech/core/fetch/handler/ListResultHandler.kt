package com.evertech.core.fetch.handler


import com.evertech.core.definition.JAction
import com.evertech.core.fetch.strategy.BaseFetchStrategy
import com.evertech.core.fetch.strategy.ListFetchStrategy
import com.evertech.core.net.response.BizMsg
import com.evertech.core.ptrlm.PtrLmHandler
import com.evertech.core.util.ThreadHelper

import io.reactivex.annotations.NonNull
import io.reactivex.functions.Consumer

import com.evertech.core.ptrlm.PtrLmHandler.REFRESH_FINISH_DELAY


/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/2/2020 3:55 PM
 *    desc   :
 */
class ListResultHandler<T>(@NonNull onSuccess: Consumer<T>, @NonNull onBizError: Consumer<BizMsg>, @NonNull onError: Consumer<Throwable>, @param:NonNull private val mFs: ListFetchStrategy) :
    BaseResultHandler<T>(onSuccess, onBizError, onError) {

    private var mPtrLmHandler: PtrLmHandler? = null

    init {
        initPtrLmHandler()
    }

    private fun initPtrLmHandler() {
        mPtrLmHandler = mFs.ptrLmHandler
        checkNotNull(mPtrLmHandler) { "PtrLmHandler不能为空" }
    }

    override fun onFinalComplete() {
        if (mPtrLmHandler!!.isRefreshing) {
            mPtrLmHandler!!.stopRefreshing()
        } else {
            mPtrLmHandler!!.loadMoreOver()
        }
        delayDismissLoading()
        hideStateView()
    }

    override fun onFinalError() {
        if (mPtrLmHandler!!.isRefreshing) {
            mPtrLmHandler!!.stopRefreshing()
        } else {
            mPtrLmHandler!!.loadMoreFail()
        }
        delayDismissLoading()
    }

    private fun delayDismissLoading() {
        ThreadHelper.runDelayed(object : JAction {
            override fun run() {
                try {
                    mFs.view!!.loadingView.dismiss()
                } catch (e: Exception) {
                }

            }
        }, REFRESH_FINISH_DELAY.toLong())
    }

    override fun commonErrorHandling() {
        if (mPtrLmHandler!!.isRefreshing) {
            /* “加载失败”的处理已经在onFinalErrorHandle里处理了，该生命周期只需要处理“刷新失败” */
            super.commonErrorHandling()
        }
    }

    override fun fs(): BaseFetchStrategy? {
        return mFs
    }

}
