package com.evertech.core.app;

import com.blankj.utilcode.util.SPUtils;
import com.evertech.core.net.token.TokenInfo;

import java.util.Set;

import static com.evertech.core.net.token.TokenHelper.addTokenLogs;


/**
 * @Author Shuo
 * @Create 2019-02-12
 * <p>
 * @Desc 通用的本地数据.
 */
public class CommonCacheData {

    protected static final SPUtils SP_UTILS = SPUtils.getInstance("share_date");

    /* 用户RefreshToken */
    private static final String USER_REFRESH_TOKEN = "user_refresh_token";

    /* 匿名用户RefreshToken */
    private static final String ANONYMOUS_REFRESH_TOKEN = "user_refresh_token";

    /* 匿名用户AccessToken */
    private static final String ANONYMOUS_ACCESS_TOKEN = "anonymous_access_token";

    /* 用户AccessToken */
    private static final String USER_ACCESS_TOKEN = "user_access_token";

    /* 是否登录 */
    private static final String KEY_IS_LOGIN = "isLogin";

    /* TAG */
    public static final String OK_HTTP = "OkHttp";

    /* 优惠券 */
    public static final String KEY_COUPON = "key_coupon";

    /**
     * 对外暴露的刷新Token信息.
     */
    public static void refreshTokenInfo(TokenInfo tokenInfo, boolean isLogin) {
        setIsLogin(isLogin);
        if (isLogin) {
            refreshUserTokenInfo(tokenInfo);
        }
    }




    /**
     * 刷新用户Token信息.
     */
    private static void refreshUserTokenInfo(TokenInfo tokenInfo) {
        setRefreshToken(USER_REFRESH_TOKEN, tokenInfo.refreshToken);
        setAccessToken(USER_ACCESS_TOKEN, tokenInfo.accessToken);
    }

    /**
     * 设置RefreshToken
     */
    private static void setRefreshToken(String key, String refreshToken) {
        addTokenLogs("setRefreshToken", refreshToken);
        SP_UTILS.put(key, refreshToken);
    }

    /**
     * 设置AccessToken
     */
    private static void setAccessToken(String key, String accessToken) {
        accessToken = String.format("Bearer %s", accessToken);
        addTokenLogs("setAccessToken", accessToken);
        SP_UTILS.put(key, accessToken);
    }

    /**
     * 获取RefreshToken
     */
    public static String getRefreshToken() {
        return getIsLogin() ? SP_UTILS.getString(USER_REFRESH_TOKEN, "") : SP_UTILS.getString(ANONYMOUS_ACCESS_TOKEN, "");
    }


    /**
     * 获取AccessToken
     */
    public static String getAccessToken() {
        return getIsLogin() ? SP_UTILS.getString(USER_ACCESS_TOKEN, "") : SP_UTILS.getString(ANONYMOUS_ACCESS_TOKEN, "");
    }

    /**
     * 是否登录
     */
    public static void setIsLogin(boolean isLogin) {
        SP_UTILS.put(KEY_IS_LOGIN, isLogin);
        refreshPushInfo(isLogin);
    }

    /**
     * 刷新推送信息.
     */
    private static void refreshPushInfo(boolean isLogin) {
        if (isLogin) {
//            JPushHelper.refreshAliasAndTagsAfterLogin(true);
        } else {
//            JPushHelper.refreshAliasAndTagsOnAnonymity(true);
        }
    }

    /* 最近一次保存的推送Tag */
    public static void savePushTag(Set<String> tags) {
        SP_UTILS.put("push_tag", tags);
    }

    /* 最近一次保存的推送Tag */
    public static Set<String> getPushTag() {
        return SP_UTILS.getStringSet("push_tag");
    }

    /**
     * 是否登录
     */
    public static boolean getIsLogin() {
        return SP_UTILS.getBoolean(KEY_IS_LOGIN, false);
    }

    /**
     * UserID
     *
     * @param
     */
    public static void setUserId(String userId) {
        SP_UTILS.put("userId", userId);
    }

    /**
     * UserID
     *
     * @return
     */
    public static String getUserId() {
        return SP_UTILS.getString("userId", "");
    }



    /**
     * 清除有效信息
     */
    public static void clearData() {
        setIsLogin(false);
        setUserId("");
    }


}
