package com.example.frontend.service;

import com.example.frontend.request.Comment.RequestCreateComment;
import com.example.frontend.request.Comment.RequestDeleteComment;
import com.example.frontend.request.Comment.RequestLikeComment;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Comment.CommentResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CommentService {

    // create comment
    @POST("comment/createComment")
    Call<ApiResponse<CommentResponse>> createComment(@Body RequestCreateComment createComment);

    // delete comment
    @POST("comment/deleteComment")
    Call<ApiResponse<String>> deleteComment(@Body RequestDeleteComment deleteComment);

    // get list comment by pot
    @GET("comment/getListCommentByIdPost")
    Call<ApiResponse<List<CommentResponse>>> getListCommentByIdPost(@Query("id") String id) ;

    // like comment
    @POST("comment/likeComment")
    Call<ApiResponse<List<CommentResponse>>> likeComment(@Body RequestLikeComment likeComment);
}
