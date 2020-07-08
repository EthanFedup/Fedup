package com.evertech.Fedup.login.repository

import com.evertech.Fedup.BuildConfig
import com.evertech.Fedup.login.model.*
import com.evertech.core.net.NetManager
import io.reactivex.Flowable

/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/6/2020 1:58 PM
 *    desc   :
 */
class NetLoginDataSource :LoginDataSource{


    private val mApiService:LoginApiService = NetManager.getInstance().createAPIService(BuildConfig.BASE_URL,LoginApiService::class.java)

    init {

    }

    override fun getVerifyCode(
        platform: String,
        version: String,
        acceptLanguage: String,
        paramLoginVerifyCode: ParamLoginVerifyCode
    ): Flowable<ResponseVerifyCode> {

        return mApiService.getVerifyCode(platform,version, acceptLanguage, paramLoginVerifyCode)
    }


    override fun userRegister(param: ParamRegister): Flowable<ResponseRegister> {
      return  mApiService.userRegister(param)
    }

    override fun userLogin(param: ParamLogin): Flowable<ResponseLogin> {
        return  mApiService.userLogin(param)
    }

    override fun getCountryCodeList(): Flowable<ResponseCountryCode> {
        return mApiService.getCountryCodeList()
    }

}