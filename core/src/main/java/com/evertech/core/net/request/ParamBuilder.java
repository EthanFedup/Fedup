package com.evertech.core.net.request;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

/**
 * @Author Shuo
 * @Create 2018-10-26
 */
public class ParamBuilder {
    private HashMap<String, Object> mParamMap;

    public static ParamBuilder newBuilder() {
        return new ParamBuilder();
    }

    private ParamBuilder() {
    }

    public ParamBuilder put(String key, String value) {
        getParamMap().put(key, value);
        return this;
    }

    private HashMap<String, Object> getParamMap() {
        return mParamMap == null ? mParamMap = new HashMap<>() : mParamMap;
    }

    public HashMap<String, Object> build() {
        return getParamMap();
    }

    /**
     * 只接收String格式的query.
     */
    public static <T> HashMap<String, String> modelToQueryMap(T t) {
        if (t == null) return null;
        String json = new GsonBuilder().create().toJson(t);
        return new GsonBuilder().create().fromJson(json,
                new TypeToken<HashMap<String, String>>() {
                }.getType());
    }

}
