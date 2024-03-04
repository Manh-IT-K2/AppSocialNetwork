package com.example.frontend.service;

import com.example.frontend.request.User.RequestCreateAccount;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface  UserService {
    @POST("/user/createAccount")
    Call<String> registerUser(@Body RequestCreateAccount request);
    @GET("/user/sendOTP")
    Call<String> sendOTP(@Query("email") String email);
}
