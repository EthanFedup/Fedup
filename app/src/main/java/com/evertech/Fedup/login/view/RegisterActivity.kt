package com.evertech.Fedup.login.view

import android.app.Activity
import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import butterknife.OnClick
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.LogUtils
import com.evertech.Constant
import com.evertech.Fedup.BuildConfig
import com.evertech.Fedup.R
import com.evertech.Fedup.login.Presenter.LoginPresenter
import com.evertech.Fedup.login.contract.LoginContract
import com.evertech.Fedup.login.model.*
import com.evertech.core.app.CommonPath
import com.evertech.core.app.Path
import com.evertech.core.mvp.view.BaseActivity
import com.evertech.core.router.Router
import com.evertech.core.util.AESUtils
import com.evertech.core.util.StringUtil
import com.hjq.toast.ToastUtils
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.etPassword
import kotlinx.android.synthetic.main.activity_register.etPhoneNumber
import kotlinx.android.synthetic.main.activity_register.tvCountryCode
import kotlinx.android.synthetic.main.include_account_password_layout.*


/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/3/2020 5:40 PM
 *    desc   :
 */
@Route(path = Path.Login.REGISTER)
class RegisterActivity : BaseActivity(), LoginContract.View {


    private val loginPresenter = LoginPresenter()

    override fun layoutResId() = R.layout.activity_register

    override fun initData() {
        super.initData()
        addPresenter(loginPresenter)
    }

    override fun initViews() {
        doAgreement()

        val phoneNum = "18201772200"
        val encrypt = AESUtils.getInstance().encrypt(phoneNum)
        val decryptString = AESUtils.getInstance().decrypt(encrypt)


        LogUtils.d("testAes-000--" + encrypt)
        LogUtils.d("testAes-111--" + decryptString)

    }

    private fun doAgreement() {
        val spannableString = SpannableString("点击即代表您已阅读并同意Fedup网《用户服务协议》 和《隐私权保护政策》")

        spannableString.setSpan(
            clickableSpanUser, 18, 26,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            clickableSpanPrivacy, 28, 37,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tvAgreement.movementMethod = LinkMovementMethod.getInstance()//必须设置才能响应点击事件
        tvAgreement.highlightColor =
            resources.getColor(android.R.color.transparent)//不设置该属性，点击后会有背景色
        tvAgreement.text = spannableString

    }


    var clickableSpanUser: ClickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            LogUtils.d("点击了clickableSpanUser-000--")
            Router.build(CommonPath.WEB)
                ?.withString("title", "用户服务协议")
                ?.withString("url", BuildConfig.WEB_BASE_URL + "index.html?name=service")
                ?.navigation(this@RegisterActivity)
            LogUtils.d("点击了clickableSpanUser-111--")
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = resources.getColor(R.color.colorCommBlue)//设置颜色
            ds.isUnderlineText = false//去掉下划线
        }
    }

    var clickableSpanPrivacy: ClickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            LogUtils.d("clickableSpanPrivacy--000--")

            Router.build(CommonPath.WEB)
                ?.withString("title", "隐私权保护政策")
                ?.withString("url", BuildConfig.WEB_BASE_URL + "index.html?name=privacy")
                ?.navigation(this@RegisterActivity)
            LogUtils.d("clickableSpanPrivacy--111--")

        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = resources.getColor(com.evertech.Fedup.R.color.colorCommBlue)//设置颜色
            ds.isUnderlineText = false//去掉下划线
        }
    }

    @OnClick(R.id.tvGetVerificationCode, R.id.tvNext, R.id.tvToLogin,R.id.tvCountryCode)
    fun viewClick(v: View) {
        when (v.id) {
            R.id.tvGetVerificationCode -> {
                getVerifyCode()
            }
            R.id.tvNext -> {
                verifyDataEmpty()
            }
            R.id.tvToLogin -> {
                Router.build(Path.Login.LOGIN)!!.navigation(this)
            }
            R.id.tvCountryCode -> {
                Router.build(Path.Login.COUNTRY_CODE)!!
                    .navigation(this,Constant.REQUEST_COUNTRY_CODE)
            }
        }
    }

    private fun verifyDataEmpty() {
        val textPhoneNum = etPhoneNumber.text.toString()
        if (textPhoneNum.isEmpty()) {
            ToastUtils.show("请输入电话号码！")
            return
        }
        val textVerifyCode = etVerifyCode.text.toString()
        if (textPhoneNum.isEmpty()) {
            ToastUtils.show("请输入验证码！")
            return
        }
        val textPassword = etPassword.text.toString()
        if (textPhoneNum.isEmpty()) {
            ToastUtils.show("请输入密码！")
            return
        }

        val countryCode = StringUtil.getCountry(tvCountryCode)


        val paramRegister = ParamRegister()
        paramRegister.phone = AESUtils.getInstance().encrypt(textPhoneNum)
        paramRegister.ident_code = textVerifyCode
        paramRegister.area_code = countryCode
        paramRegister.password = AESUtils.getInstance().encrypt(textPassword)

        Router.build(Path.Login.REGISTER_FULL_INFO)
            ?.withParcelable("paramRegister", paramRegister)
            ?.navigation(this)

        /*ARouter.getInstance().build(Path.Login.REGISTER_FULL_INFO)
            .withParcelable("paramRegister", paramRegister)
            .navigation(this)*/

    }

    /**
     * 获取验证码
     */
    private fun getVerifyCode() {

        val textPhoneNum = etPhoneNumber.text.toString()
        if (textPhoneNum.isEmpty()) {
            ToastUtils.show("请输入电话号码！")
            return
        }


       val cbChecked = cb_agreement.isChecked
        if(cbChecked){
            val countryCode = StringUtil.getCountry(tvCountryCode)

            val paramLoginVerifyCode = ParamLoginVerifyCode()
            paramLoginVerifyCode.phone = AESUtils.getInstance().encrypt(textPhoneNum)
            paramLoginVerifyCode.code = AESUtils.getInstance().encrypt(countryCode)
            loginPresenter.getVerifyCode(paramLoginVerifyCode)
        }else{
            ToastUtils.show("请阅读并选中用户协议与隐私协议")
        }


    }


    override fun onLoginResult(responseLogin: ResponseLogin, stateCode: Int) {

    }

    override fun onRegisterResult(responseRegister: ResponseRegister, stateCode: Int) {
        if(responseRegister.success){
            ToastUtils.show("注册成功！")
        }else{
            ToastUtils.show(responseRegister.msg)
        }
    }

    override fun onGetVerifyCodeResult(responseVerifyCode: ResponseVerifyCode, stateCode: Int) {

        if(responseVerifyCode.success){
            ToastUtils.show("验证码已发送至您的手机！")
        }else{
            ToastUtils.show("验证码已发送至您的手机！")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode ==Constant.REQUEST_COUNTRY_CODE&&resultCode == Activity.RESULT_OK){
            val country = data!!.getParcelableExtra<Country>("codeBean")
            tvCountryCode.text = country!!.number.toString()
        }
    }



}
