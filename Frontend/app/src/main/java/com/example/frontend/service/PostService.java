package com.example.frontend.service;

import com.example.frontend.request.Post.RequestCreatePost;
import com.example.frontend.request.Post.RequestPostByUserId;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Post.PostResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PostService {

    // create Post
    @POST("post/createPost")
    Call<ApiResponse<String>> createPost(@Body RequestCreatePost requestCreatePost, @Query("userId") String userId);

    // get list post by userId
    @GET("post/getListPostByUserId")
    Call<ApiResponse<List<RequestPostByUserId>>> getListPostByUserId(@Query("userId") String userId);

    // add user like post
    @POST("post/addLike")
    Call<ApiResponse<PostResponse>> addLike(@Query("postId") String postId, @Query("userId") String userId);

    // get posts by search query
    @GET("post/getListPostsBySearchQuery")
    Call<ApiResponse<List<RequestPostByUserId>>> getListPostsBySearchQuery(@Query("id") String id, @Query("searchQuery") String searchQuery);
 }
