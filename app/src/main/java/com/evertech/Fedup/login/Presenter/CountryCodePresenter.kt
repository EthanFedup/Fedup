package com.evertech.Fedup.login.Presenter

import com.evertech.Constant
import com.evertech.Constant.SUCCESS_CODE
import com.evertech.Fedup.login.contract.CountryCodeContract
import io.reactivex.functions.Consumer

/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/7/2020 11:11 AM
 *    desc   :
 */
class CountryCodePresenter : BaseLoginPresenter<CountryCodeContract.View>(),
    CountryCodeContract.Presenter {

    override fun getCountryCodeList() {
        val source = mDs.getCountryCodeList()
        fetch(source).onSuccess(onSuccess = Consumer { data ->
            if(data.code==SUCCESS_CODE&& data.data.isNotEmpty()){
                mView!!.onCountryCodeResult(data.data, SUCCESS_CODE)

            }else{
                mView!!.onCountryCodeResult(ArrayList(), SUCCESS_CODE)
            }
        }).onBizError(onBizError = Consumer {
            mView!!.onCountryCodeResult(ArrayList(), SUCCESS_CODE)

        }).onError(onError = Consumer {
            mView!!.onCountryCodeResult(ArrayList(), SUCCESS_CODE)
        }).start()
    }
}