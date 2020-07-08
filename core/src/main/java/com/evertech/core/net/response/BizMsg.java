package com.evertech.core.net.response;

import com.google.gson.annotations.SerializedName;

/**
 * @Author Shuo
 * @Create 2018-10-26
 */
public class BizMsg {

    public String code;

    @SerializedName("message")
    public String msg;

    public BizMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BizMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

}
