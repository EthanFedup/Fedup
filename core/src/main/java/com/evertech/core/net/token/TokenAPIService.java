package com.evertech.core.net.token;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * @Author Shuo
 * @Create 2019-09-17
 * <p>
 * @Desc
 */
public interface TokenAPIService {

    String LOGIN_API = "token/login";

    String REFRESH_TOKEN_API = "token/refresh";

    /* 刷新AccessToken */
    @Multipart
    @POST(REFRESH_TOKEN_API)
    Call<TokenInfo> refreshAccessToken(@Part("refresh_token") RequestBody refreshToken);

    @POST(LOGIN_API)
    Call<TokenInfo> login(@Body HashMap<String, Object> params);


}
