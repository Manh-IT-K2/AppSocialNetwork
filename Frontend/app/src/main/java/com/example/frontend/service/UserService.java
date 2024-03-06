package com.example.frontend.service;

import com.example.frontend.request.User.RequestCreateAccount;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface  UserService {
    @POST("/user/createAccount")
    Call<ApiResponse<Object>> registerUser(@Body RequestCreateAccount request);
    @POST("/user/login")
    Call<ApiResponse<Object>> login(@Body RequestCreateAccount request);
    @GET("/user/sendOTP")
    Call<ApiResponse<Object>> sendOTP(@Query("email") String email);
}
