package com.evertech.Fedup.login.contract

import com.evertech.Fedup.login.model.*
import com.evertech.core.mvp.presenter.IPresenter
import com.evertech.core.mvp.view.IView

/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/7/2020 11:09 AM
 *    desc   : 国家代码
 */
interface CountryCodeContract {
    interface View : IView {
        fun onCountryCodeResult(countryCodeList: MutableList<WrapCountryData>, stateCode: Int)
    }

    interface Presenter : IPresenter {

        fun getCountryCodeList()
    }
}