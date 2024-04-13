package com.example.frontend.service;

import com.example.frontend.request.Notification.Notification;
import com.example.frontend.response.User.NotificationResponse;
import com.example.frontend.request.User.RequestChangePW;
import com.example.frontend.request.User.RequestChangePass;
import com.example.frontend.request.User.RequestCreateAccount;
import com.example.frontend.request.User.RequestLogin;
import com.example.frontend.request.User.RequestUpdateTokenFCM;
import com.example.frontend.request.User.RequestUpdateUser;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.GetAllUserByFollowsResponse;
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
    Call<ApiResponse<String>> sendOTP_forgotpassword(@Query("email") String email);

    @GET("user/allUsers")
    Call<ApiResponse<List<UserResponse>>> getAllUsers();
    @GET("user/allUsersByFollows")
    Call<ApiResponse<List<GetAllUserByFollowsResponse>>> getAllUsersByFollows(@Query("id") String id);
    @GET("user/requestTracking")
    Call<ApiResponse<List<UserResponse>>> getRequestTrackingUser();
    @POST("user/changePass")
    Call<ApiResponse<UserResponse>> changePass(@Body RequestChangePass request);
    @POST("user/changePW")
    Call<ApiResponse<UserResponse>> changePW(@Body RequestChangePW request);
    @GET("user/detailUserById")
    Call<ApiResponse<UserResponse>> getDetailUserById(@Query("id") String id);
    @POST("user/updateUser")
    Call<ApiResponse<UserResponse>> updateUser(@Body RequestUpdateUser request);
    @GET("user/getListUserName")
    Call<ApiResponse<List<String>>> getListUserName();
    @GET("user/getUser_privateChat")
    Call<ApiResponse<List<UserResponse>>> findUser_privatechat(@Query("u") String keyword);
    @POST("user/updateTokenFCM")
    Call<ApiResponse<UserResponse>> updateTokenFCM(@Body RequestUpdateTokenFCM request);

    @GET("user/getTokenFCM")
    Call<ApiResponse<String>> getTokenFCM(@Query("id") String id);
    @POST("user/addNotification")
    Call<ApiResponse<String>> addNotification(@Body Notification notification);

    @GET("user/getNotification")
    Call<ApiResponse<List<NotificationResponse>>> getNotification(@Query("id") String id);
}
