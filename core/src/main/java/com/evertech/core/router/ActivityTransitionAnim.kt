package com.evertech.core.router

import android.content.Context
import androidx.core.app.ActivityOptionsCompat
import android.view.View

import com.evertech.core.R


/**
 * author : Ethan
 * e-mail : Ethan@fedup.cn
 * date   : 7/6/2020 10:00 AM
 * desc   :
 */
object ActivityTransitionAnim {
    /**
     * Enter from left , exit to right.
     */
    private val LEFT_IN_ANIM = intArrayOf(R.anim.slide_in_from_left, R.anim.slide_out_to_right)

    /**
     * Enter from right , exit to left.
     */
    private val RIGHT_IN_ANIM = intArrayOf(R.anim.slide_in_from_right, R.anim.slide_out_to_left)

    private val HOME_FINISH = intArrayOf(R.anim.home_enter, R.anim.home_exit)

    /**
     * @param source
     * @param startStyle
     */
    fun getCompatWithView(source: View, startStyle: StartStyle): ActivityOptionsCompat {
        return getCompatWithoutView(source.context, startStyle)
    }

    /**
     * @param context
     * @param startStyle
     */
    fun getCompatWithoutView(
        context: Context, startStyle: StartStyle
    ): ActivityOptionsCompat {
        return ActivityOptionsCompat.makeCustomAnimation(
            context, getStartEnterAnim(startStyle), getStartExitAnim(startStyle)
        )
    }

    /**
     * Default enter_anim is from right to left.
     */
    fun getStartEnterAnim(startStyle: StartStyle?): Int {
        if (startStyle == null) {
            return getStartEnterAnim(StartStyle.COMMON)
        }

        when (startStyle) {
            StartStyle.COMMON -> return RIGHT_IN_ANIM[0]
            StartStyle.REVERSE -> return LEFT_IN_ANIM[0]
            else -> return RIGHT_IN_ANIM[0]
        }
    }

    /**
     * Default exit_anim is from left to right.
     */
    fun getStartExitAnim(startStyle: StartStyle?): Int {
        if (startStyle == null) {
            return getStartExitAnim(StartStyle.COMMON)
        }

        when (startStyle) {
            StartStyle.COMMON -> return RIGHT_IN_ANIM[1]
            StartStyle.REVERSE -> return LEFT_IN_ANIM[1]
            else -> return RIGHT_IN_ANIM[1]
        }
    }

    /**
     * Default enter_anim is from left to right.
     */
    fun getFinishEnterAnim(finishStyle: FinishStyle?): Int {
        if (finishStyle == null) {
            return getFinishEnterAnim(FinishStyle.COMMON)
        }

        when (finishStyle) {
            FinishStyle.COMMON -> return LEFT_IN_ANIM[0]
            FinishStyle.HOME -> return HOME_FINISH[0]
            else -> return LEFT_IN_ANIM[0]
        }
    }

    /**
     * Default exit_anim is from right to left.
     */
    fun getFinishExitAnim(finishStyle: FinishStyle?): Int {
        if (finishStyle == null) {
            return getFinishExitAnim(FinishStyle.COMMON)
        }

        when (finishStyle) {
            FinishStyle.COMMON -> return LEFT_IN_ANIM[1]
            FinishStyle.HOME -> return HOME_FINISH[1]
            else -> return LEFT_IN_ANIM[1]
        }
    }
}
