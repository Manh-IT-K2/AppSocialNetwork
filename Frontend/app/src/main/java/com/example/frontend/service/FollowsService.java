package com.example.frontend.service;

import com.example.frontend.request.Follows.RequestCreateFollows;
import com.example.frontend.request.Follows.RequestUpdateFollows;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Follows.FollowsResponse;
import com.example.frontend.response.Follows.GetQuantityResponse;
import com.example.frontend.response.User.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FollowsService {
    // create Post
    @POST("follows/create")
    Call<ApiResponse<String>> createFollows(@Body RequestCreateFollows requestCreateFollows);
    @GET("follows/getQuantityFollows")
    Call<ApiResponse<GetQuantityResponse>> getQuantityFollows(@Query("id") String id);
    @POST("follows/updateFollows")
    Call<ApiResponse<String>> updateFollows(@Body RequestUpdateFollows requestUpdateFollows);
    @GET("follows/deleteFollow")
    Call<ApiResponse<String>> deleteFollows(@Query("idFollower") String idFollower,@Query("idFollowing") String idFollowing);
    @GET("follows/getUserFollowingById")
    Call<ApiResponse<List<UserResponse>>> getUserFollowingById(@Query("id") String id);
    @GET("follows/getUserFollowerById")
    Call<ApiResponse<List<UserResponse>>> getUserFollowerById(@Query("id") String id);
}
