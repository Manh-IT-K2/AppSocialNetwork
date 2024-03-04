package com.example.frontend.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CallApi {
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Credentials.URL)
            .addConverterFactory(GsonConverterFactory.create()).build();
}
