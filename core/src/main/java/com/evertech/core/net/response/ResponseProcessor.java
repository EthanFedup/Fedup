package com.evertech.core.net.response;

import com.blankj.utilcode.util.ObjectUtils;
import com.evertech.core.net.exception.EmptyDataException;
import com.evertech.core.net.exception.PureStringResultException;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.ResponseBody;

public class ResponseProcessor<T> {

    public T process(ResponseBody value, Gson gson, TypeAdapter<T> adapter, boolean enableStringConverter) throws IOException {
        String strValue = value.string();

        if (ObjectUtils.isEmpty(strValue)) {
            throw new EmptyDataException();
        }

        if (enableStringConverter) {
            throw new PureStringResultException(strValue);
        }

        T data;
        try {
            data = adapter.fromJson(strValue);
        } catch (IOException e) {
            throw e;
        }

        if (data == null) {
            throw new EmptyDataException();
        } else {
            return data;
        }
    }

}
