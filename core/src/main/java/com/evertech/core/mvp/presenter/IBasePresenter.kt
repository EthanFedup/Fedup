package com.evertech.core.mvp.presenter

import android.app.Activity
import androidx.fragment.app.Fragment

import com.evertech.core.mvp.view.IView


/**
 * @Author Shuo
 * @Create 2018-10-26
 */
interface IBasePresenter {
    /**
     * Binds presenter with a view when created. The Presenter will perform initialization here.
     *
     * @param view
     */
    fun init(view: IView)

    /**
     * V's first time fetching of data. Could be both/either remote and/or local data.
     */
    fun openDataFetching()

    /**
     * [Activity.onStart] or [Fragment.onStart]
     */
    fun onStart()

    /**
     * [Activity.onResume] or [Fragment.onResume]
     */
    fun onResume()

    /**
     * [Activity.onPause] or [Fragment.onPause]
     */
    fun onPause()

    /**
     * [Activity.onStop] or [Fragment.onStop]
     */
    fun onStop()

    /**
     * [Activity.onDestroy] or [Fragment.onDestroy]
     */
    fun onDestroy()
}
