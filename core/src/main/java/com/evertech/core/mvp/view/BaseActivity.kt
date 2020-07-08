package com.evertech.core.mvp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.view.MotionEvent

import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout

import com.blankj.utilcode.util.ObjectUtils
import com.evertech.core.definition.GlobalEvent
import com.evertech.core.definition.RequestPermissionListener
import com.evertech.core.definition.SoftKeyboardFixerForFullscreen
import com.evertech.core.mvp.ActivityTransitionAnim
import com.evertech.core.mvp.FinishStyle
import com.evertech.core.mvp.presenter.IPresenter
import com.evertech.core.mvp.presenter.PresenterDelegate
import com.evertech.core.util.InputMethodUtils
import com.evertech.core.util.PermissionHelper
import com.evertech.core.util.ViewHelper
import com.evertech.core.widget.PageLoadingView
import com.evertech.core.widget.StateView
import com.evertech.core.widget.TitleBar
import com.gyf.barlibrary.ImmersionBar
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.umeng.analytics.MobclickAgent


import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

import butterknife.ButterKnife

import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.evertech.core.definition.GlobalEvent.Companion.TYPE_REVERSE_LAYOUT
import com.evertech.core.mvp.ActivityTransitionAnim.getFinishExitAnim
import com.evertech.core.router.Router
import com.evertech.core.util.ViewHelper.MATCH
import com.evertech.core.util.ViewHelper.WRAP
import com.evertech.core.util.ViewHelper.newFrameParams
import com.evertech.core.util.ViewHelper.newLinearParams


/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/3/2020 11:37 AM
 *    desc   :
 */
abstract class BaseActivity : RxAppCompatActivity(), IView, RequestPermissionListener {
    /**
     * Presenter代理
     */
    private var mPresenterDelegate: PresenterDelegate? = null

    /**
     * 全局加载页
     */
    private var mLoadingView: PageLoadingView? = null

    /**
     * 沉浸式状态栏
     */
    private var mImmersionBar: ImmersionBar? = null

    /**
     * 全局TitleBar
     */
    lateinit var titleBar: TitleBar
        private set

    /**
     * 真正意义上的内容View，不包含TitleBar
     */
    private var mContentView: View? = null

    /**
     * 状态页，包含几种常用状态
     */
    private var mStateView: StateView? = null

    /**
     * 结束动画样式
     */
    private var mFinishStyle = FinishStyle.COMMON

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performOnCreate(savedInstanceState)
    }

    /**
     * 初始化
     */
    private fun performOnCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            requestPermissions(*arrayOf())
            routineTasks()
        } else {
            onRecycled(savedInstanceState)
        }
    }

    /**
     * 常规初始化内容
     */
    private fun routineTasks() {
        prepareTools()
        prepareDataAndViews()
        preparePresenter()
        onFinishInit()
    }

    /**
     * 系统回收后的处理. 子类可以覆写，进行特殊处理（如恢复先前界面的显示等）
     */
    protected fun onRecycled(savedInstanceState: Bundle) {
        finish()
    }

    /**
     * 如果子类要申请权限，需要覆写该方法.R
     *
     * @param permissions 待申请权限.
     * @return 是否需要申请权限，默认否.
     */
    protected fun requestPermissions(vararg permissions: String): Boolean {
        if (ObjectUtils.isEmpty(permissions)) {
            return false
        } else {
            PermissionHelper().requestPermissions(this, this, permissions as Array<String>)
            return true
        }
    }

    private fun prepareTools() {
        Router.inject(this)
        EventBus.getDefault().register(this)
    }

    private fun prepareDataAndViews() {
        contentView = buildContentView()
        ButterKnife.bind(this)

        /* Presenter代理初始化 */
        mPresenterDelegate = PresenterDelegate()
        /* 全局加载样式 */
        loadingView

        /* 默认沉浸式的样式 */
        mImmersionBar = initImmersionBar()
        mImmersionBar!!.init()

        /* 输入法默认设置 */
        initInputMethod()

        /* 抛给子类自由定制初始化 */
        initData()
        initViews()
    }

    /**
     * 构建布局内容View.
     */
    protected fun buildContentView(): ViewGroup {

        val rootView = LinearLayout(this)
        rootView.orientation = LinearLayout.VERTICAL
        rootView.layoutParams = ViewHelper.newCommonParams(MATCH, MATCH)

        titleBar = TitleBar(this)
        rootView.addView(titleBar, newLinearParams(MATCH, WRAP))
        rootView.addView(buildContainer(), newLinearParams(MATCH, MATCH))

        return rootView

    }

    /**
     * 构建主内容布局View(包含主内容布局和状态页).
     */
    private fun buildContainer(): FrameLayout {
        val container = FrameLayout(this)

        mContentView = layoutInflater.inflate(layoutResId(), null)

        mStateView = StateView(this)
        mStateView!!.visibility = View.GONE

        container.addView(mContentView, newFrameParams(MATCH, MATCH))
        container.addView(mStateView, newFrameParams(MATCH, MATCH))

        return container
    }

    /**
     * 默认沉浸式的样式，可以让子类覆写.
     */
    protected fun initImmersionBar(): ImmersionBar {
        return ImmersionBar.with(this).statusBarDarkFont(true, 0.2f)
    }

    /**
     * 默认输入法相关设置.
     */
    fun initInputMethod() {
//        SoftKeyboardFixerForFullscreen.assistActivity(this)      /* 非常重要 */
        window.setSoftInputMode(SOFT_INPUT_ADJUST_PAN)
    }

    private fun preparePresenter() {
        mPresenterDelegate!!.init(this)
        mPresenterDelegate!!.openDataFetching()
    }

    /**
     * 抛给子类，所有初始化完毕后的回调
     */
    protected open fun onFinishInit() {}

    @LayoutRes
    protected abstract fun layoutResId(): Int

    protected open fun initData() {

    }

    protected abstract fun initViews()

    protected fun addPresenter(presenter: IPresenter) {
        mPresenterDelegate!!.add(presenter)
    }

    @CallSuper
    override fun finish() {
        super.finish()
        overridePendingTransition(
            ActivityTransitionAnim.getFinishEnterAnim(mFinishStyle),
            getFinishExitAnim(mFinishStyle)
        )
    }

    @CallSuper
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Router.inject(this)
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        if (mPresenterDelegate != null) {
            mPresenterDelegate!!.onStart()
        }
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        if (mPresenterDelegate != null) {
            mPresenterDelegate!!.onResume()
        }
        MobclickAgent.onResume(this)
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        if (mPresenterDelegate != null) {
            mPresenterDelegate!!.onPause()
        }
        hideInputMethod()
        MobclickAgent.onResume(this)
    }

    @CallSuper
    override fun onStop() {
        if (mPresenterDelegate != null) {
            mPresenterDelegate!!.onStop()
        }
        super.onStop()
    }

    @CallSuper
    override fun onDestroy() {
        if (mPresenterDelegate != null) {
            mPresenterDelegate!!.onDestroy()
        }
        unregisterTools()
        super.onDestroy()
    }

    private fun unregisterTools() {
        EventBus.getDefault().unregister(this)
        if (mImmersionBar != null) {
            mImmersionBar!!.destroy()
        }
    }

    /**
     * @param finishStyle Animation style when [.finish].
     */
    protected fun setFinishAnimStyle(finishStyle: FinishStyle) {
        mFinishStyle = finishStyle
    }

    /**
     * 关闭输入法.
     */
    fun hideInputMethod() {
        var focusView = currentFocus
        if (focusView == null) {
            focusView = View(this)
        }
        InputMethodUtils.getIMM(this).hideSoftInputFromWindow(focusView.windowToken, 0)
    }

    override fun getContentView(): View? {
        return mContentView
    }

    override fun getStateView(): StateView? {
        return mStateView
    }

    override fun getLoadingView(): PageLoadingView? {
//        return if (mLoadingView == null) PageLoadingView(this) else mLoadingView
        return PageLoadingView.getInstance(this)
    }

    override fun <E> lifecycleTransformer(): LifecycleTransformer<E> {
        return bindToLifecycle()
    }

    override fun onAcceptAllPermissions() {
        /* 默认不拦截，所以交给子类处理 */
    }

    override fun onDenySomePermissions(deniedPermissions: List<String>) {
        /* 交给子类处理 */
    }

    /**
     * 界面存在反转时，需要覆写改方法.
     *
     * @param needToReverse 否：正常；是：需要反转
     */
    protected fun onReverseLayout(needToReverse: Boolean) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGlobalEvent(event: GlobalEvent) {
        if (event.type == TYPE_REVERSE_LAYOUT) {
            var needToReverse: Boolean? = false
            try {
                needToReverse = event.tag as Boolean
            } catch (e: Exception) {
            }

            onReverseLayout(needToReverse!!)
        }
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            val v = currentFocus
            if (isShouldHideInput(v, ev)) {

                hideSoftInput(v!!.windowToken);
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v     v
     * @param event e
     * @return b
     */
    private fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if ((v is EditText)) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l);
            val left = l[0]
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return event.x < left || event.x > right || event.y < top || event.y > bottom
        }

        return false
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token t
     */

    private fun hideSoftInput(token: IBinder?) {

        if (token != null) {
            val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }



}
