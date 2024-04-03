package com.example.frontend.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.frontend.request.Comment.RequestCreateComment;
import com.example.frontend.request.Comment.RequestDeleteComment;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Comment.CommentResponse;
import com.example.frontend.service.CommentService;
import com.example.frontend.utils.CallApi;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentRepository {

    CommentService commentService;

    //
    public CommentRepository() {
        commentService = CallApi.getRetrofitInstance().create(CommentService.class);
    }

    // create Comment
    public MutableLiveData createComment(RequestCreateComment createComment) {
        MutableLiveData<ApiResponse<CommentResponse>> mutableLiveData = new MutableLiveData<>();
        commentService.createComment(createComment).enqueue(new Callback<ApiResponse<CommentResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<CommentResponse>> call, Response<ApiResponse<CommentResponse>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<CommentResponse> apiResponse = response.body();
                    mutableLiveData.setValue(apiResponse);
                } else {
                    mutableLiveData.setValue(new ApiResponse<>(false, "create comment error", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<CommentResponse>> call, Throwable t) {
                mutableLiveData.setValue(new ApiResponse<>(false, "error" + t.getMessage(), null));
            }
        });
        return mutableLiveData;
    }

    // delete comment
    public MutableLiveData deleteComment(RequestDeleteComment deleteComment) {
        MutableLiveData<ApiResponse<String>> mutableLiveData = new MutableLiveData<>();
        commentService.deleteComment(deleteComment).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<String> apiResponse = response.body();
                    mutableLiveData.setValue(apiResponse);
                } else {
                    int errorCode = response.code();
                    String errorMessage = "Error occurred with code: " + errorCode;
                    ApiResponse<String> errorResponse = new ApiResponse<>(false, "delete comment error", errorMessage);
                    mutableLiveData.setValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                String errorMessage = "Failed to delete Comment: " + t.getMessage();
                ApiResponse<String> errorResponse = new ApiResponse<>(false, "failed", errorMessage);
                mutableLiveData.setValue(errorResponse);
            }
        });
        return mutableLiveData;
    }

    // get list comment by idPost
    public MutableLiveData<ApiResponse<List<CommentResponse>>> getListCommentByIdPost(String id) {
        MutableLiveData<ApiResponse<List<CommentResponse>>> mutableLiveData = new MutableLiveData<>();
        commentService.getListCommentByIdPost(id).enqueue(new Callback<ApiResponse<List<CommentResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CommentResponse>>> call, Response<ApiResponse<List<CommentResponse>>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<List<CommentResponse>> apiResponse = response.body();
                    Gson gson = new Gson();
                    String json = gson.toJson(apiResponse);
                    Log.e("Data Comment", json);
                    mutableLiveData.setValue(apiResponse);
                } else {
                    mutableLiveData.setValue(new ApiResponse<>(false, "Error get data from server", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CommentResponse>>> call, Throwable t) {
                mutableLiveData.setValue(new ApiResponse<>(false, "Failed: " + t.getMessage(), null));
            }
        });
        return mutableLiveData;
    }
}
