package com.evertech.Fedup.login.Presenter

import com.evertech.Fedup.login.repository.LoginRepository
import com.evertech.core.mvp.presenter.BasePresenter
import com.evertech.core.mvp.view.IView

/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/3/2020 4:43 PM
 *    desc   :
 */
open class BaseLoginPresenter<T:IView> : BasePresenter<T>() {
    @JvmField
    protected var mDs:LoginRepository = LoginRepository()

}