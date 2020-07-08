package com.evertech.core.mvp.presenter


import com.evertech.core.fetch.PageFetcher
import com.evertech.core.mvp.view.BaseActivity
import com.evertech.core.ptrlm.PtrLmHandler

import io.reactivex.Flowable

/**
 * The Base 'P' in 'MVP'
 */
interface IPresenter : IBasePresenter {

    /**
     * How a View send data to the relevant Presenter.
     *
     * @param filedName Presenter file name
     * @param value     可以传基本类型或者具体的引用类型的数据，与被赋值字段的类型保持一致。
     */
    fun acceptData(filedName: String, value: Any)

    /**
     * 提供Activity对象.
     */
    fun getmActivity(): BaseActivity

    /**
     * 正常的数据请求，包含UI交互.
     */
    fun <R> fetch(source: Flowable<R>): PageFetcher<R>

    /**
     * 不显示无网提示页和数据错误提示页.
     */
    fun <R> fetchWithoutUIHint(source: Flowable<R>): PageFetcher<R>

    /**
     * 正常的数据请求，包含UI交互.(无加载中的view)
     */
    fun <R> noLoadViewfetch(source: Flowable<R>): PageFetcher<R>

    /**
     * 没有任何UI交互的数据请求.
     */
    fun <R> silenceFetch(source: Flowable<R>): PageFetcher<R>


    /* 重置“空数据”状态 */
    fun validateEmptyData()

}
