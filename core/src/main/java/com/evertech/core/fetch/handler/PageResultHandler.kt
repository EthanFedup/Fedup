package com.evertech.core.fetch.handler

import com.evertech.core.fetch.strategy.BaseFetchStrategy
import com.evertech.core.fetch.strategy.PageFetchStrategy
import com.evertech.core.net.response.BizMsg
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Consumer

/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/2/2020 4:06 PM
 *    desc   :
 */
class PageResultHandler<T>(@NonNull onSuccess: Consumer<T>, @NonNull onBizError: Consumer<BizMsg>, @NonNull onError: Consumer<Throwable>, @param:NonNull private val mFS: PageFetchStrategy) :
    BaseResultHandler<T>(onSuccess, onBizError, onError) {

    override fun onFinalComplete() {

        dismissLoading()

        hideStateView()
    }

    override fun onFinalError() {
        dismissLoading()
    }

    /* 关闭loading页 */
    private fun dismissLoading() {

        if (hasIView() && mFS.enableCommonLoadingView && !mFS.enableCommonLoadingViewWithoutDismiss) {
            mFS.view!!.loadingView.dismiss()
        }
    }


    override fun fs(): BaseFetchStrategy? {
        return mFS
    }

}
