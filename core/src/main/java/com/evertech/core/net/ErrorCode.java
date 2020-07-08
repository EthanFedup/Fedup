package com.evertech.core.net;

import kotlin.text.Regex;

/**
 * @Author Shuo
 * @Create 2018-10-26
 */
public class ErrorCode {

    public static final int BIZ_STATUS_CODE = 403;

    public static final int BIZ_STATUS_CODE2 = 400;

    public static class E401 {
        /**
         * 被登出的情况(两个车载APP间的互登).
         */
        public static String FORCED_LOGOUT = "invalid-session";
        /**
         * 刷新Token时返回的错，原因是Refresh Token过期了.
         */
        public static String TOKEN_REFRESH_EXCEPTION = "token-refresh-exception";
        /* Token无效 */
        public static String TOKEN_INVALID = "token-invalid";
        /* Token Session过期 */
        public static String TOKEN_EXPIRED = "token-expired";
        /* Token Session未激活 */
        public static String TOKEN_SESSION_INACTIVE = "token-session-inactive";
        /* Token Session异常 */
        public static String TOKEN_SESSION_EXCEPTION = "token-session-exception";
        /* 授权错误 */
        public static String UNAUTHORIZED = "unauthorized";
    }

}
