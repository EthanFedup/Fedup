package com.evertech.Fedup.login.view

import android.view.View
import butterknife.OnClick
import com.alibaba.android.arouter.facade.annotation.Route
import com.evertech.Fedup.R
import com.evertech.core.app.Path
import com.evertech.core.mvp.view.BaseActivity
import com.evertech.core.router.Router

/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/3/2020 1:47 PM
 *    desc   :
 */
@Route(path = Path.Login.WELCOME)
class WelcomeActivity : BaseActivity() {

    override fun layoutResId()=R.layout.activity_welcome

    override fun initViews() {
        titleBar.visibility = View.GONE
    }

    @OnClick(R.id.tv_login,R.id.tv_register)
    fun viewClick(v: View){
        when(v.id){
            R.id.tv_login->{
                Router.build(Path.Login.LOGIN)!!.navigation(this)
            }
            R.id.tv_register -> {
                Router.build(Path.Login.REGISTER)!!.navigation(this)
            }
        }
    }

}
