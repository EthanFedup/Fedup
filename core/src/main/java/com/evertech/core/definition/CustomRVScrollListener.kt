package com.evertech.core.definition

import androidx.recyclerview.widget.RecyclerView

import io.reactivex.functions.Function3

/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/2/2020 4:03 PM
 *    desc   :
 */
class CustomRVScrollListener(private val mCallBack: Function3<ScrollState, ScrollDirection, Boolean, Int>?) :
    RecyclerView.OnScrollListener() {

    private var mDirection: ScrollDirection? = null

    private var mState: Int = 0

    private var isStateChange: Boolean = false

    enum class ScrollDirection {
        LEFT, UP, RIGHT, DOWN
    }

    enum class ScrollState {
        IDLE, DRAGGING, SETTLING
    }

    fun resetState() {
        mState = RecyclerView.INVALID_TYPE
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (mState != newState) {
            isStateChange = true
        }

        when (newState) {
            RecyclerView.SCROLL_STATE_IDLE -> {
                mState = RecyclerView.SCROLL_STATE_IDLE
                applyCallback()
            }
            RecyclerView.SCROLL_STATE_DRAGGING -> {
                mState = RecyclerView.SCROLL_STATE_DRAGGING
                applyCallback()
            }
            RecyclerView.SCROLL_STATE_SETTLING -> {
                mState = RecyclerView.SCROLL_STATE_SETTLING
                applyCallback()
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (dx > 0) {
            mDirection = ScrollDirection.RIGHT
            applyCallback()
        } else if (dx < 0) {
            mDirection = ScrollDirection.LEFT
            applyCallback()
        }

        if (dy > 0) {
            mDirection = ScrollDirection.DOWN
            applyCallback()
        } else if (dy < 0) {
            mDirection = ScrollDirection.UP
            applyCallback()
        }
    }

    private fun applyCallback() {
        if (mCallBack == null) return
        try {
            mDirection?.let { mCallBack.apply(mapScrollSate(), it, isStateChange) }
        } catch (e: Exception) {

        }

    }

    private fun mapScrollSate(): ScrollState {
        when (mState) {
            RecyclerView.SCROLL_STATE_IDLE -> return ScrollState.IDLE
            RecyclerView.SCROLL_STATE_DRAGGING -> return ScrollState.DRAGGING
            RecyclerView.SCROLL_STATE_SETTLING -> return ScrollState.SETTLING
            else -> return ScrollState.IDLE
        }
    }

}