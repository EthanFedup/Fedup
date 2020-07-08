package com.evertech.core.router

import android.app.Activity
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import androidx.core.app.ActivityOptionsCompat
import android.view.View

import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.StringUtils
import com.evertech.core.BaseApp
import com.evertech.core.definition.JAction
import com.evertech.core.util.ThreadHelper

import java.io.Serializable
import java.util.ArrayList

/**
 * Router for [Activity] jumping
 */
object Router {
    private var sMockPostcard: MockPostcard? = null

    /**
     * @return
     */
    private val startStyle: StartStyle
        get() = if (sMockPostcard == null) StartStyle.COMMON else sMockPostcard!!.startStyle

    /**
     * @return
     */
    val enterAnim: Int
        get() = ActivityTransitionAnim.getStartEnterAnim(startStyle)

    /**
     * @return
     */
    val exitAnim: Int
        get() = ActivityTransitionAnim.getStartExitAnim(startStyle)

    class MockPostcard {

        /**
         * The real PostCard
         */
        var postcard: Postcard? = null
            private set

        private var withFinish: Boolean = false

        var startStyle = StartStyle.COMMON

        /**
         * @param path
         */
        internal fun build(path: String) {
            postcard = ARouter.getInstance().build(path)
        }

        fun withString(key: String?, value: String?): MockPostcard {
            postcard!!.withString(key, value)
            return this
        }

        fun withBoolean(key: String?, value: Boolean): MockPostcard {
            postcard!!.withBoolean(key, value)
            return this
        }

        fun withByte(key: String?, value: Byte): MockPostcard {
            postcard!!.withByte(key, value)
            return this
        }

        fun withShort(key: String?, value: Short): MockPostcard {
            postcard!!.withShort(key, value)
            return this
        }

        fun withInt(key: String?, value: Int): MockPostcard {
            postcard!!.withInt(key, value)
            return this
        }

        fun withLong(key: String?, value: Long): MockPostcard {
            postcard!!.withLong(key, value)
            return this
        }

        fun withFloat(key: String?, value: Float): MockPostcard {
            postcard!!.withFloat(key, value)
            return this
        }

        fun withDouble(key: String?, value: Double): MockPostcard {
            postcard!!.withDouble(key, value)
            return this
        }

        fun withParcelable(key: String?, value: Parcelable): MockPostcard {
            postcard!!.withParcelable(key, value)
            return this
        }

        /* ParcelableArrayList 不支持注解注入，只能从Bundle里拿，如需注入，请使用withObject */
        fun withParcelableArrayList(key: String?, value: ArrayList<out Parcelable>?): MockPostcard {
            postcard!!.withParcelableArrayList(key, value)
            return this
        }

        fun withStringArrayList(key: String?, value: ArrayList<String>?): MockPostcard {
            postcard!!.withStringArrayList(key, value)
            return this
        }

        fun withSerializable(key: String?, value: Serializable?): MockPostcard {
            postcard!!.withSerializable(key, value)
            return this
        }

        fun withObject(key: String?, value: ArrayList<out Parcelable>?): MockPostcard {
            postcard!!.withObject(key, value)
            return this
        }

        /**
         * In most cases, you will not use this method, cause there are default animation styles.
         *
         * @param startType Animation style when starting Activity.
         */
        fun startStyle(startType: StartStyle): MockPostcard {
            this.startStyle = startType
            return this
        }

        /**
         * Only works fine with [.navigation]
         */
        fun withFinish(): MockPostcard {
            withFinish = true
            return this
        }

        fun clearTask(): MockPostcard {
            startStyle(StartStyle.REVERSE)
            postcard!!.withFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            return this
        }

        fun clearTop(): MockPostcard {
            postcard!!.withFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return this
        }

        fun singleTop(): MockPostcard {
            postcard!!.withFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            return this
        }

        fun withFlag(key: String?, value: Float): MockPostcard {
            postcard!!.withFloat(key, value)
            return this
        }

        /**
         * @param activity From activity
         */
        fun navigation(activity: Activity) {
            postcard!!.withTransition(enterAnim, exitAnim).navigation(activity)
            if (withFinish) {
                ThreadHelper.runDelayed(object : JAction {
                    override fun run() {
                        activity.finish()
                    }
                }, 500)
            }
            clear()
        }

        /**
         * Eg: From [Notification] or [BroadcastReceiver],
         * which has no [Activity] or [View] references.
         *
         * @param context
         */
        @JvmOverloads
        fun navigation(context: Context = BaseApp.app) {
            if (context is Activity) {
                navigation(context)
                return
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                postcard = postcard!!.withOptionsCompat(getCompatWithoutView(context))
            }
            postcard!!.navigation(context)
            clear()
        }

        /**
         * @param source Trigger of the jump
         */
        fun navigation(source: View) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                postcard = postcard!!.withOptionsCompat(getCompatWithView(source))
            }
            postcard!!.navigation()
            clear()
        }

        /**
         * @param activity    From activity
         * @param requestCode requestCode for [Activity.startActivityForResult]
         */
        fun navigation(activity: Activity, requestCode: Int) {
            postcard!!.withTransition(enterAnim, exitAnim).navigation(activity, requestCode)
            clear()
        }

        /**
         * clear static data
         */
        private fun clear() {
            withFinish = false
            startStyle = StartStyle.COMMON
            postcard = null
            sMockPostcard = null
        }
    }
    /**
     * Situation: No Context or Activity.
     */

    /**
     * @param path Where you go.
     */
    fun build(path: String): MockPostcard? {
        if (StringUtils.isTrimEmpty(path)) return null

        sMockPostcard = if (sMockPostcard == null) MockPostcard() else sMockPostcard
        sMockPostcard!!.build(path)

        return sMockPostcard
    }

    /**
     * @param object
     */
    fun inject(`object`: Any) {
        ARouter.getInstance().inject(`object`)
    }

    /**
     * @param source
     */
    private fun getCompatWithView(source: View): ActivityOptionsCompat {
        return ActivityTransitionAnim.getCompatWithView(source, startStyle)
    }

    /**
     * @param context
     */
    private fun getCompatWithoutView(context: Context): ActivityOptionsCompat {
        return ActivityTransitionAnim.getCompatWithoutView(context, startStyle)
    }

}
