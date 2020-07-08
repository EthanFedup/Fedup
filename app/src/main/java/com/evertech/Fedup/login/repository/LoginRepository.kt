package com.evertech.Fedup.login.repository

import com.evertech.Constant
import com.evertech.Fedup.login.model.*
import io.reactivex.Flowable

/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/6/2020 1:57 PM
 *    desc   :
 */
class LoginRepository : LoginDataSource {


    private val mNetDs: NetLoginDataSource = NetLoginDataSource()

    override fun getVerifyCode(
        platform: String,
        version: String,
        acceptLanguage: String,
        paramLoginVerifyCode: ParamLoginVerifyCode
    ): Flowable<ResponseVerifyCode> {
        return mNetDs.getVerifyCode(platform,version,acceptLanguage, paramLoginVerifyCode)
    }


    override fun userRegister(param: ParamRegister): Flowable<ResponseRegister> {
        return mNetDs.userRegister(param)
    }

    override fun userLogin(param: ParamLogin): Flowable<ResponseLogin> {
        return mNetDs.userLogin(param)
    }

    override fun getCountryCodeList(): Flowable<ResponseCountryCode> {
        return mNetDs.getCountryCodeList()
    }
}