package com.example.frontend.service;

import com.example.frontend.request.User.RequestChangePW;
import com.example.frontend.request.User.RequestChangePass;
import com.example.frontend.request.User.RequestCreateAccount;
import com.example.frontend.request.User.RequestLogin;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface  UserService {
    @POST("user/createAccount")
    Call<ApiResponse<String>> registerUser(@Body RequestCreateAccount request);
    @POST("user/login")
    Call<ApiResponse<UserResponse>> login(@Body RequestLogin request);
    @GET("user/sendOTP")
    Call<ApiResponse<String>> sendOTP(@Query("email") String email);
    @GET("user/sendOTP_forgotpassword")
    Call<ApiResponse<String>> sendOtp_forgotpassword(@Query("email") String email);

    @GET("user/allUsers")
    Call<ApiResponse<List<UserResponse>>> getAllUsers();
    @POST("user/changePass")
    Call<ApiResponse<UserResponse>> changePass(@Body RequestChangePass request);
    @POST("user/changePW")
    Call<ApiResponse<UserResponse>> changePW(@Body RequestChangePW request);
}
