package com.evertech.Fedup.login.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import butterknife.OnClick
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.evertech.Constant
import com.evertech.Constant.REQUEST_COUNTRY_CODE
import com.evertech.Fedup.R
import com.evertech.Fedup.login.Presenter.LoginPresenter
import com.evertech.Fedup.login.contract.LoginContract
import com.evertech.Fedup.login.model.*
import com.evertech.core.app.Path
import com.evertech.core.mvp.view.BaseActivity
import com.evertech.core.router.Router
import com.evertech.core.util.AESUtils
import com.evertech.core.util.AesCryptUtil
import com.evertech.core.util.StringUtil
import com.evertech.core.util.ViewHelper
import com.hjq.toast.ToastUtils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.include_account_password_layout.*


/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/3/2020 2:58 PM
 *    desc   :
 */
@Route(path = Path.Login.LOGIN)
class LoginActivity : BaseActivity(), LoginContract.View {

    private val loginPresenter = LoginPresenter()

    override fun layoutResId() = R.layout.activity_login

    override fun initData() {
        super.initData()
        addPresenter(loginPresenter)
    }

    override fun initViews() {

        ViewHelper.setUnderLine(tvForgotPassword)

    }

    @OnClick(R.id.tvLogin, R.id.tvToRegister, R.id.tvCountryCode)
    fun viewClick(v: View) {
        when (v.id) {
            R.id.tvLogin -> {
                login()
            }
            R.id.tvToRegister -> {
                LogUtils.d("---tvToRegister-000--")
                Router.build(Path.Login.REGISTER)?.navigation(this)
                LogUtils.d("---tvToRegister-111--")

            }
            R.id.tvCountryCode -> {
                Router.build(Path.Login.COUNTRY_CODE)!!
                    .navigation(this, REQUEST_COUNTRY_CODE)
            }

        }
    }

    private fun verifyPhoneNumEmpty(): String {
        val textPhoneNum = etPhoneNumber.text.toString()
        if (textPhoneNum.isEmpty()) {
            ToastUtils.show("请输入手机号码！")
        }
        return textPhoneNum
    }

    private fun verifyPassEmpty(): String {
        val textPassword = etPassword.text.toString()
        if (textPassword.isEmpty()) {
            ToastUtils.show("请输入密码！")
        }
        return textPassword
    }

    private fun login() {
        val textPhoneNum = etPhoneNumber.text.toString()
        if (textPhoneNum.isEmpty()) {
            ToastUtils.show("请输入手机号码！")
            return
        }
        val textPassword = etPassword.text.toString()
        if (textPassword.isEmpty()) {
            ToastUtils.show("请输入密码！")
            return
        }

        val countryCode = StringUtil.getCountry(tvCountryCode)
        val paramLogin = ParamLogin()


        paramLogin.code = AESUtils.getInstance().encrypt(countryCode)
        paramLogin.password = AESUtils.getInstance().encrypt(textPassword)
        paramLogin.phone = AESUtils.getInstance().encrypt(textPhoneNum)



/*
       paramLogin.code = AesCryptUtil.encrypt(countryCode)
        paramLogin.password =  AesCryptUtil.encrypt(textPassword)
        paramLogin.phone = AesCryptUtil.encrypt(textPhoneNum)
*/


        LogUtils.d("--countryCode--"+paramLogin.code)
        LogUtils.d("--textPassword--"+paramLogin.password)
        LogUtils.d("--textPhoneNum--"+paramLogin.phone)


        LogUtils.d("--countryCode--"+ AESUtils.getInstance().decrypt(paramLogin.code))
        LogUtils.d("--textPassword--"+ AESUtils.getInstance().decrypt(paramLogin.password))
        LogUtils.d("--textPhoneNum--"+ AESUtils.getInstance().decrypt(paramLogin.phone))
        loginPresenter.userLogin(paramLogin)
    }

    override fun onLoginResult(responseLogin: ResponseLogin, stateCode: Int) {

        if (responseLogin.success) {
            ToastUtils.show("登录成功！")
        }else{
            ToastUtils.show(responseLogin.msg)
        }
    }

    override fun onRegisterResult(responseRegister: ResponseRegister, stateCode: Int) {

    }

    override fun onGetVerifyCodeResult(responseVerifyCode: ResponseVerifyCode, stateCode: Int) {

    }


    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_COUNTRY_CODE && resultCode == Activity.RESULT_OK) {

            val country = data!!.getParcelableExtra<Country>("codeBean")

            if (null != country) {
                LogUtils.d("onActivityResult----000---")
                tvCountryCode.text = "+" + country.number.toString()
                LogUtils.d("onActivityResult----333---" + country.number)
            } else {
                LogUtils.d("onActivityResult----555---" + country)

            }

        }
    }


}
