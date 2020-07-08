package com.evertech.Fedup.login.contract

import com.evertech.Fedup.login.model.*
import com.evertech.core.mvp.presenter.IPresenter
import com.evertech.core.mvp.view.IView
import com.evertech.core.net.response.BizMsg

/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/6/2020 2:11 PM
 *    desc   :
 */
interface LoginContract {

    interface View:IView{
        fun onLoginResult(responseLogin: ResponseLogin,stateCode: Int)

        fun onRegisterResult(responseRegister: ResponseRegister,stateCode: Int)

        fun onGetVerifyCodeResult(responseVerifyCode: ResponseVerifyCode,stateCode: Int)
    }

    interface Presenter:IPresenter{

        fun userLogin(paramLogin: ParamLogin)

        fun userRegister(paramRegister: ParamRegister)

        fun getVerifyCode(paramLoginVerifyCode: ParamLoginVerifyCode)
    }
}