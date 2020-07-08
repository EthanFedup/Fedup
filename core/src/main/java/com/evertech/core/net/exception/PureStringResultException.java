package com.evertech.core.net.exception;

/**
 * @Author Shuo
 * @Create 2018-12-10
 * <p>
 * @Desc
 */
public class PureStringResultException extends RuntimeException {
    private String mStringResult;

    public PureStringResultException(String stringResult) {
        mStringResult = stringResult;
    }

    public String getStringResult() {
        return mStringResult;
    }
}
