package com.kotensky.exchangerate.model.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Stas on 12.02.2017.
 */

public class ApiModule {

    private static final String url = "https://bank.gov.ua";

    public static IREST getApiInterface() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        return builder.build().create(IREST.class);
    }
}
