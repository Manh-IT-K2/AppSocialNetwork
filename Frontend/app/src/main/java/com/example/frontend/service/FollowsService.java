package com.example.frontend.service;

import com.example.frontend.request.Follows.RequestCreateFollows;
import com.example.frontend.request.Follows.RequestUpdateFollows;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Follows.FollowsResponse;
import com.example.frontend.response.Follows.GetQuantityResponse;

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
}
