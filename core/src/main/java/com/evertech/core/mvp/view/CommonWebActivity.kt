package com.evertech.core.mvp.view

import android.graphics.Bitmap
import android.view.KeyEvent
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.LinearLayout

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.evertech.core.R
import com.evertech.core.app.CommonPath
import com.hjq.toast.ToastUtils
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient

import com.blankj.utilcode.util.StringUtils.isTrimEmpty
import com.evertech.core.util.StringUtil

/**
 * author : Ethan
 * e-mail : Ethan@fedup.cn
 * date   : 7/3/2020 11:22 AM
 * desc   :
 */
@Route(path = CommonPath.WEB)
class CommonWebActivity : BaseActivity() {

    @Autowired(name = "url")
    @JvmField
    var url: String? = null

    @Autowired(name="title")
    @JvmField
    var title: String? = null

    val textEqu = "\u003d"

    private val mAgentWeb: AgentWeb? = null

    /* 是否已还可以从网页继续返回 */
    var isWebPageBackEnabled: Boolean = false
        private set

    private val mWebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {}
    }

    private val mWebChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {}

        override fun onReceivedTitle(view: WebView, title: String) {
            super.onReceivedTitle(view, title)
                titleBar.setTitle(title)
        }
    }

    override fun layoutResId(): Int {
        return R.layout.activity_web
    }

    override fun initData() {}

    override fun initViews() {

        LogUtils.d("initViews--000-===-$title"+"==")
        LogUtils.d("initViews--111--$url")

        titleBar.setTitle(if (isTrimEmpty(title)) "网页" else title)
        val flContainer = findViewById<FrameLayout>(R.id.fl_container)
        initWebView(flContainer)
    }

    private fun initWebView(viewContainer: ViewGroup) {
        val preAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(viewContainer, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .setWebChromeClient(mWebChromeClient)
            .setWebViewClient(mWebViewClient)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            //                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
            //                .setWebLayout(new WebLayout(this))
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
            .interceptUnkownUrl() //拦截找不到相关页面的Scheme
            .createAgentWeb()
            .ready()

        if (isTrimEmpty(url)) {
            ToastUtils.show("链接为空")
        } else {

            LogUtils.d("initViews--777--$StringUtil.unToString(url!!)")
            LogUtils.d("initViews--888--来了")

            preAgentWeb.go( StringUtil.unToString(url!!))
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (mAgentWeb != null && mAgentWeb.handleKeyEvent(keyCode, event)) {
            isWebPageBackEnabled = true
            return true
        } else {
            isWebPageBackEnabled = false
            return super.onKeyDown(keyCode, event)
        }
    }

    override fun onResume() {
        mAgentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }


    override fun onPause() {
        mAgentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mAgentWeb?.webLifeCycle?.onDestroy()
        super.onDestroy()
    }

}
