package com.evertech.core.net.converter;

import com.evertech.core.net.response.ResponseProcessor;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private boolean enableStringConverter;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, boolean enableStringConverter) {
        this.gson = gson;
        this.adapter = adapter;
        this.enableStringConverter = enableStringConverter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            return new ResponseProcessor<T>().process(value, gson, adapter, enableStringConverter);
        } finally {
            value.close();
        }
    }
}
