package com.example.frontend.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CallApi {
    private static Retrofit retrofit;
    public static String URL_Loi = "https://jskw989d-8080.asse.devtunnels.ms/api/";
    public static String URL_Manh = "https://3rg07cfh-8080.asse.devtunnels.ms/api/";
    public static String URL_Long = "https://nbbwn98c-8080.asse.devtunnels.ms/api/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL_Long)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
