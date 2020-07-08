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
 *    date   : 7/3/2020 4:45 PM
 *    desc   :
 */
interface LoginApiService {


    /**
     * 注册获取验证码
     */
    @POST("app_sendsms.php")
    fun getVerifyCode(@Header("Platform") platform: String
                      ,@Header("Version") version: String
                      ,@Header("Accept-Language") acceptLanguage: String
                      ,@Body paramLoginVerifyCode: ParamLoginVerifyCode)
            :Flowable<ResponseVerifyCode>




    /**
     * 注册
     */
    @POST("app_register.php")
    fun userRegister(@Body param: ParamRegister):Flowable<ResponseRegister>


    /**
     * 登录
     */
    @POST("app_login.php")
    fun userLogin(@Body param: ParamLogin):Flowable<ResponseLogin>

    /**
     * 获取国家代码
     */
    @GET("common/contryNumberSelect")
    fun getCountryCodeList(): Flowable<ResponseCountryCode>


}