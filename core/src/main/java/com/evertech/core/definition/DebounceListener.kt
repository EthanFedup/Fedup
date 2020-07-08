package com.evertech.core.definition

import android.view.View

/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/2/2020 4:01 PM
 *    desc   :
 */
class DebounceListener : View.OnClickListener {

    override fun onClick(v: View) {
        val currTime = System.currentTimeMillis()

        if (currTime - mLastTriggerTime > DEBOUNCE_INTERVAL) {
            mLastTriggerTime = currTime
            performClick(v)
        }
    }

    /**
     * 点击的回调.
     */
    fun performClick(v: View) {

    }

    companion object {

        /* 上次点击时间 */
        var mLastTriggerTime: Long = 0

        /* 最小点击间隔 */
        val DEBOUNCE_INTERVAL: Long = 1200
    }

}
