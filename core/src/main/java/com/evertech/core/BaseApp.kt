package com.evertech.core

import android.app.Application
import android.content.Context
import androidx.annotation.CallSuper
import androidx.multidex.MultiDex

import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.Utils
import com.hjq.toast.ToastUtils


/**
 * author : Ethan
 * e-mail : Ethan@fedup.cn
 * date   : 7/3/2020 5:04 PM
 * desc   :
 */
open class BaseApp : Application() {


    override fun onCreate() {
        super.onCreate()
        app = this
        commonInit()
    }

    /**
     * Very important , or will cause many indescribable problems,
     * such as: [https://github.com/google/dagger/issues/831](http://google.com)
     */
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    @CallSuper
    protected open fun commonInit() {
        Utils.init(this)
        // 在Application中初始化
        ToastUtils.init(this)
        initRouter()

    }

    private fun initRouter() {
        ARouter.openDebug()
        ARouter.openLog()
        ARouter.init(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        ARouter.getInstance().destroy()
    }

    companion object {
        lateinit var app: BaseApp
            protected set
    }

}
