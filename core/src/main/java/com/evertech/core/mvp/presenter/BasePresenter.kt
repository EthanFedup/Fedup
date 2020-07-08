package com.evertech.core.mvp.presenter


import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import com.evertech.core.definition.GlobalEvent
import com.evertech.core.fetch.PageFetcher
import com.evertech.core.mvp.view.BaseActivity
import com.evertech.core.mvp.view.IView
import com.evertech.core.ptrlm.PtrLmHandler

import io.reactivex.Flowable

import com.evertech.core.definition.GlobalEvent.Companion.TYPE_TOKEN_CHANGE
import com.evertech.core.util.ReflectUtils


/**
 * @Author Shuo
 * @Create 2018-10-26
 */
abstract class BasePresenter<T : IView> : IPresenter {
    protected var mView: T? = null

    @CallSuper
    override fun init(view: IView) {
        initTools()
        takeView(view)
    }

    private fun takeView(view: IView) {
        mView = view as T
    }

    fun dropView() {
        mView = null
    }

    override fun acceptData(filedName: String, value: Any) {
        ReflectUtils.setFiledValue(this, filedName, value)
    }

    override fun getmActivity(): BaseActivity{
        if (mView is BaseActivity) {
            return mView as BaseActivity
        } else if (mView is Fragment) {
            val activity = (mView as Fragment).activity
            if (activity is BaseActivity) {
                return activity
            }
        }
        return mView as BaseActivity
    }

    override fun <R> fetch(source: Flowable<R>): PageFetcher<R> {
        return PageFetcher(source)
            .enableErrorPage(true)
            .enableNoNetPage(true)
            .withView(mView)
    }

    override fun <R> fetchWithoutUIHint(source: Flowable<R>): PageFetcher<R> {
        return PageFetcher(source)
            .withView(mView)
    }


    override fun <R> noLoadViewfetch(source: Flowable<R>): PageFetcher<R> {
        return PageFetcher(source)
            .disableCommonLoadingView()
            .enableErrorPage(true)
            .enableNoNetPage(true)
            .withView(mView)
    }

    override fun <R> silenceFetch(source: Flowable<R>): PageFetcher<R> {
        return PageFetcher(source)
            .disableCommonLoadingView()
            .disableBizErrorToast()
            .disableErrorToast()
            .disableNoNetToast()
            .withView(mView)
    }


    override fun validateEmptyData() {
        if (mView != null && mView!!.stateView != null) {
            mView!!.stateView.validateData()
        }
    }

    private fun initTools() {
        EventBus.getDefault().register(this)
    }

    private fun unregisterTools() {
        EventBus.getDefault().unregister(this)
    }

    override fun openDataFetching() {}

    override fun onStart() {}

    override fun onResume() {}

    override fun onPause() {}

    override fun onStop() {}

    @CallSuper
    override fun onDestroy() {
        unregisterTools()
        dropView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGlobalEvent(event: GlobalEvent) {
        if (event.type == TYPE_TOKEN_CHANGE) {
        }
    }
}
