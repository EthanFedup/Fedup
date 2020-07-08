package com.evertech.core.net;


import com.evertech.core.net.converter.GsonConverterFactory;
import com.evertech.core.net.ssl.SSLSocketClient;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import static com.evertech.core.app.CommonCacheData.getAccessToken;
import static com.evertech.core.net.token.TokenHelper.ALLOW_NET_LOGS;


/**
 * @Author Shuo
 * @Create 2018-10-26
 */
public class NetManager {

    /* 通用超时时间 */
    public static final long COMMON_CONNECT_TIMEOUT = 15l;

    /* 不限时超时时间 */
    public static final long MAX_CONNECTION_TIMEOUT = 2 * 60l;

    /**
     * 最新的Gson Converter Retrofit.
     */
    private Retrofit mRetrofit;

    private NetManager() {
    }

    private static class Singleton {
        private static NetManager sInstance = new NetManager();
    }

    /**
     * 唯一入口.
     */
    public static NetManager getInstance() {
        return Singleton.sInstance;
    }

    /**
     * @return 新建ServiceBuilder实例对象.
     */
    public static ServiceBuilder newServiceBuilder() {
        return new ServiceBuilder().timeout(COMMON_CONNECT_TIMEOUT);
    }


    /**
     * 根据BaseUrl构建APIService对象.
     */
    public <T> T createAPIService(String baseUrl, Class<T> clazz) {
        ServiceBuilder builder = newServiceBuilder().baseUrl(baseUrl);
        return createAPIService(builder, clazz);
    }

    /**
     * 根据ServiceBuilder构建APIService对象.
     */
    public <T> T createAPIService(ServiceBuilder builder, Class<T> clazz) {
        if (builder == null) return null;
        return newRetrofit(builder).create(clazz);
    }


    /**
     * 新建Retrofti对象.
     */
    private Retrofit newRetrofit(ServiceBuilder builder) {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();

        Retrofit retrofit = retrofitBuilder.baseUrl(builder.baseUrl)
                .client(newOkHttpClient(builder))
                .addConverterFactory(GsonConverterFactory.create(builder.enableStringConverter))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        if (!builder.enableStringConverter) {
            mRetrofit = retrofit;
        }

        return retrofit;
    }

    /**
     * 新建OkHttpClient对象.
     */
    private OkHttpClient newOkHttpClient(ServiceBuilder serviceBuilder) {
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();

        setHttpProtocols(okBuilder);
        setTimeout(okBuilder, serviceBuilder);
        addTokenInterceptor(okBuilder, serviceBuilder);
        addLoggingInterceptor(okBuilder);
        addSslSocketFactory(okBuilder);

        return okBuilder.build();
    }

    /**
     * 设置请求协议.
     */
    private void setHttpProtocols(OkHttpClient.Builder okBuilder) {
        List<Protocol> protocols = new ArrayList<>();
        protocols.add(Protocol.HTTP_1_1);
        okBuilder.protocols(protocols);
    }

    /**
     * 设置超时时间.
     */
    private void setTimeout(OkHttpClient.Builder okBuilder, ServiceBuilder serviceBuilder) {
        long timeoutTime = serviceBuilder.timeout <= 0 ? MAX_CONNECTION_TIMEOUT : COMMON_CONNECT_TIMEOUT;
        okBuilder.connectTimeout(timeoutTime, TimeUnit.SECONDS);
        okBuilder.readTimeout(timeoutTime, TimeUnit.SECONDS);
        okBuilder.writeTimeout(timeoutTime, TimeUnit.SECONDS);
    }

    /**
     * 重点：设置Token拦截器.
     */
    public void addTokenInterceptor(OkHttpClient.Builder okBuilder, ServiceBuilder serviceBuilder) {
        if (serviceBuilder.ignoreTokenInterceptor) {
            okBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("Authorization", getAccessToken())
                            .addHeader("Accept", "application/json")
                            .addHeader("Platform","android")
                            .build();
                    return chain.proceed(request);
                }
            });
        } else {
//            okBuilder.addInterceptor(new TokenInterceptor());
        }
    }

    /**
     * 添加Log监听器.
     */
    public void addLoggingInterceptor(OkHttpClient.Builder okBuilder) {
        if (ALLOW_NET_LOGS) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okBuilder.addInterceptor(interceptor);
        }
    }

    /**
     * Socket配置.
     */
    private void addSslSocketFactory(OkHttpClient.Builder okBuilder) {
        okBuilder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory());
        okBuilder.hostnameVerifier(SSLSocketClient.getHostnameVerifier());
    }

    /**
     * 根据T转换获取Body对象.
     */
    public <T> T convertBody(Type type, ResponseBody body) {
        if (mRetrofit != null) {
            Converter<ResponseBody, T> converter = mRetrofit.responseBodyConverter(type, new Annotation[0]);
            try {
                return converter.convert(body);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 网络服务构造器.
     */
    public static class ServiceBuilder {
        /**
         * Base URL
         */
        public String baseUrl;

        /**
         * 超时时间
         */
        public long timeout;

        /**
         * 不添加Token拦截器
         */
        public boolean ignoreTokenInterceptor;

        /**
         * Response结果返回原字符串
         */
        public boolean enableStringConverter;

        private ServiceBuilder() {
        }

        /**
         * Base URL.
         */
        public ServiceBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        /**
         * 请求超时时间，单位（秒）.
         * 注意：如果时间小于等于0，则视为不限制超时时间.
         */
        public ServiceBuilder timeout(long timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * 不添加Token拦截器.
         */
        public ServiceBuilder ignoreTokenInterceptor() {
            ignoreTokenInterceptor = true;
            return this;
        }

        /**
         * Response结果返回原字符串，而非Gson解析后的对象.
         */
        public ServiceBuilder enableStringConverter() {
            enableStringConverter = true;
            return this;
        }
    }

}
