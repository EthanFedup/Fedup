package com.evertech.Fedup.login.view

import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.evertech.Fedup.R
import com.evertech.core.app.Path
import com.evertech.core.mvp.view.BaseActivity
import com.evertech.core.router.Router

import io.reactivex.Observable
import io.reactivex.disposables.Disposable

import java.util.concurrent.TimeUnit

import io.reactivex.Observer


/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/3/2020 11:05 AM
 *    desc   :
 */
class SplashActivity : BaseActivity() {

    override fun layoutResId() = R.layout.activity_splash

    override fun initViews() {
        titleBar.visibility = View.GONE
//        Thread.sleep(1000)
//        Router.build(Path.Login.WELCOME).navigation()


        toWelcome()
    }

    private fun toWelcome() {
        LogUtils.d("toWelcome--0000--")
        Observable.timer(1000, TimeUnit.MILLISECONDS)
            .subscribe(object : Observer<Long> {
                override fun onNext(t: Long) {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                    LogUtils.d("toWelcome--111--")

                    Router.build(Path.Login.WELCOME)!!.navigation()
                    LogUtils.d("toWelcome--222--")

                    finish()
                    LogUtils.d("toWelcome--333--")
                }
            })

    }


}
