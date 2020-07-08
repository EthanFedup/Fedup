package com.evertech.Fedup.login.repository

import com.evertech.Fedup.login.model.*
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/6/2020 1:55 PM
 *    desc   :
 */
interface LoginDataSource {


    /**
     * 注册获取验证码
     */
    fun getVerifyCode(
        platform: String
        , version: String
        , acceptLanguage: String
        , paramLoginVerifyCode: ParamLoginVerifyCode
    )
            : Flowable<ResponseVerifyCode>

    /**
     * 注册
     */
    fun userRegister(param: ParamRegister): Flowable<ResponseRegister>


    /**
     * 登录
     */
    fun userLogin(param: ParamLogin): Flowable<ResponseLogin>



    /**
     * 获取国家代码
     */
    fun getCountryCodeList(): Flowable<ResponseCountryCode>

}