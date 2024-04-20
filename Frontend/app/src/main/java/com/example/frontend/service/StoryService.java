package com.example.frontend.service;

import com.example.frontend.request.Story.RequestCreateStory;
import com.example.frontend.request.Story.RequestStoryByUserId;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Story.StoryResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface StoryService {

    // create Post
    @POST("story/createStory")
    Call<ApiResponse<String>> createStory(@Body RequestCreateStory createStory, @Query("userId") String userId);

    // get list post by userId
    @GET("story/getListStoryByUserId")
    Call<ApiResponse<List<RequestStoryByUserId>>> getListStoryByUserId(@Query("userId") String userId);

    // add viewer
    @POST("story/addViewer")
    Call<ApiResponse<String>> addViewerStory(@Query("storyId") String storyId, @Query("userId") String userId);

    // delete story
    @POST("story/deleteStoryById")
    Call<ApiResponse<String>> deleteStoryById(@Query("idStory") String idStory);
}
