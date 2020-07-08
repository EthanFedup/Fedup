package com.evertech.Fedup.login.Presenter

import com.evertech.Constant
import com.evertech.Constant.acceptLanguage
import com.evertech.Constant.platform
import com.evertech.Constant.version
import com.evertech.Fedup.login.contract.LoginContract
import com.evertech.Fedup.login.model.*
import io.reactivex.functions.Consumer

/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/6/2020 2:10 PM
 *    desc   :
 */
class LoginPresenter : BaseLoginPresenter<LoginContract.View>(), LoginContract.Presenter {


    override fun userLogin(paramLogin: ParamLogin) {
        val source = mDs.userLogin(paramLogin)
        fetch(source)
            .onSuccess(onSuccess = Consumer { data ->
                mView!!.onLoginResult(data, 200)
            })
            .onBizError(onBizError = Consumer {
                mView!!.onLoginResult(ResponseLogin(), 0)
            }).onError(onError = Consumer {
                mView!!.onLoginResult(ResponseLogin(), 0)
            })
            .start()
    }

    override fun userRegister(paramRegister: ParamRegister) {
        val source = mDs.userRegister(paramRegister)
        fetch(source)
            .onSuccess(onSuccess = Consumer { data ->
                mView!!.onRegisterResult(data, 200)
            })
            .onBizError(onBizError = Consumer {
                mView!!.onRegisterResult(ResponseRegister(), 0)
            }).onError(onError = Consumer {
                mView!!.onRegisterResult(ResponseRegister(), 0)
            })
            .start()
    }

    override fun getVerifyCode(paramLoginVerifyCode: ParamLoginVerifyCode) {
        val source = mDs.getVerifyCode(platform,version,acceptLanguage,paramLoginVerifyCode)
        fetch(source)
            .onSuccess(onSuccess = Consumer { data ->
                mView!!.onGetVerifyCodeResult(data, 200)
            })
            .onBizError(onBizError = Consumer {
                mView!!.onGetVerifyCodeResult(ResponseVerifyCode(), 0)
            }).onError(onError = Consumer {
                mView!!.onGetVerifyCodeResult(ResponseVerifyCode(), 0)
            })
            .start()
    }
}