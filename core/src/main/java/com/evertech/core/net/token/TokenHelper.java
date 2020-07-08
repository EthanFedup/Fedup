package com.evertech.core.net.token;

import android.util.Log;

import com.evertech.core.app.CommonPath;
import com.evertech.core.net.NetManager;
import com.evertech.core.net.exception.ForcedLogoutException;
import com.evertech.core.net.exception.TokenRefreshErrorException;
import com.evertech.core.net.response.BizMsg;
import com.evertech.core.router.Router;

import java.util.HashMap;

import okhttp3.ResponseBody;

import static com.evertech.core.BuildConfig.BUILD_TYPE;


/**
 * @Author Shuo
 * @Create 2019-09-24
 * <p>
 * @Desc
 */
public class TokenHelper {

    /**
     * 允许打网络相关的Log.
     */
    public static final boolean ALLOW_NET_LOGS = BUILD_TYPE.equals("debug") ;

    /**
     * 根据ResponseBody获取BizMsg.
     */
    public static BizMsg convertBizMsg(ResponseBody responseBody) {
        return NetManager.getInstance().convertBody(BizMsg.class, responseBody);
    }



    /**
     * 添加Log.
     */
    public static void addTokenLogs(String... logs) {
        if (!ALLOW_NET_LOGS) return;

        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("================ ");
        sBuilder.append(logs[0]);
        if (logs.length == 2) {
            sBuilder.append(" : ");
            sBuilder.append(logs[1]);
        }
        sBuilder.append(" ================");

        Log.d("OkHttp", sBuilder.toString());
    }

    public static final int PROMPT_FORCED_LOGOUT = 0x111;

    public static final int PROMPT_COMMON_JUMP = 0x222;

    /**
     * 显示Token相关提示框.
     */
    public static void showTokenPrompt(int type) {
        jumpToLoginPage();
    }

    /**
     * 跳转至登陆页.
     */
    private static void jumpToLoginPage() {
//        CommonCacheData.clearData();
        Router.INSTANCE.build(CommonPath.LOGIN)
                .withBoolean("isClearTask", true)
                .clearTask()
                .navigation();
    }

    public static void throwForcedLogoutError() {
        throw new ForcedLogoutException();
    }

    public static void throwRefreshError() {
        throw new TokenRefreshErrorException();
    }


}
