package com.evertech.core.net.exception;


import com.evertech.core.net.response.BizMsg;

public class BusinessException extends RuntimeException {
    public BizMsg bizMsg;

    public BusinessException(String code, String msg) {
        bizMsg = new BizMsg(code, msg);
    }

    public BizMsg getBizMsg() {
        return bizMsg;
    }

    @Override
    public String toString() {
        return "BusinessException{" +
                "bizMsg=" + bizMsg +
                '}';
    }

}
