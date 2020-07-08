package com.evertech.Fedup.login.view

import android.view.View
import butterknife.OnClick
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.evertech.Fedup.R
import com.evertech.Fedup.login.Presenter.LoginPresenter
import com.evertech.Fedup.login.contract.LoginContract
import com.evertech.Fedup.login.model.ParamRegister
import com.evertech.Fedup.login.model.ResponseLogin
import com.evertech.Fedup.login.model.ResponseRegister
import com.evertech.Fedup.login.model.ResponseVerifyCode
import com.evertech.core.app.Path
import com.evertech.core.mvp.view.BaseActivity
import com.evertech.core.router.Router
import com.evertech.core.util.AESUtils
import com.hjq.toast.ToastUtils
import kotlinx.android.synthetic.main.activity_full_info.*

/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/6/2020 4:20 PM
 *    desc   : 注册第二步，完善个人信息
 */
@Route(path = Path.Login.REGISTER_FULL_INFO)
class FullInfoActivity : BaseActivity(), LoginContract.View {

    private val loginPresenter = LoginPresenter()

    @Autowired
    @JvmField
    var paramRegister: ParamRegister? = null

    override fun layoutResId() = R.layout.activity_full_info

    override fun initData() {
        super.initData()
        addPresenter(loginPresenter)
    }

    override fun initViews() {
        titleBar.setLeftIconType(0)
    }

    @OnClick(R.id.tvRegister)
    fun viewClick(v: View) {
        when (v.id) {
            R.id.tvRegister -> {
                verifyDataEmpty()
            }
        }
    }

    private fun verifyDataEmpty() {
        val textUserName= etUserName.text.toString()
        if (textUserName.isEmpty()) {
            ToastUtils.show("请输入用户名！")
            return
        }
        val textEmail = etEmail.text.toString()
        if (textEmail.isEmpty()) {
            ToastUtils.show("请输入您的邮箱！")
            return
        }

        LogUtils.d("verifyDataEmpty- 000--")
        if(null!=paramRegister){
            LogUtils.d("verifyDataEmpty- 111--")

            paramRegister!!.name = textUserName
            paramRegister!!.deactivation = false
            paramRegister!!.appleIdent = ""
            paramRegister!!.email = AESUtils.getInstance().encrypt(textEmail)
        }
        LogUtils.d("verifyDataEmpty- 222--")

        paramRegister?.let { loginPresenter.userRegister(it) }
        LogUtils.d("verifyDataEmpty- 333--")

    }

    override fun onLoginResult(responseLogin: ResponseLogin, stateCode: Int) {

    }

    override fun onRegisterResult(responseRegister: ResponseRegister, stateCode: Int) {

        if(responseRegister.success){
            ToastUtils.show(responseRegister.msg)

            Router.build(Path.Login.LOGIN)?.navigation(this)
        }else{
           ToastUtils.show(responseRegister.msg)
        }
    }

    override fun onGetVerifyCodeResult(responseVerifyCode: ResponseVerifyCode, stateCode: Int) {

    }
}
