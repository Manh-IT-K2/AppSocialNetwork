package com.example.frontend.service;

import com.example.frontend.request.Follows.RequestCreateFollows;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Follows.FollowsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FollowsService {
    // create Post
    @POST("follows/create")
    Call<ApiResponse<String>> createFollows(@Body RequestCreateFollows requestCreateFollows);
}
