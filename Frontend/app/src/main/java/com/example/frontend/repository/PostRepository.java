package com.example.frontend.repository;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.frontend.request.Post.RequestCreatePost;
import com.example.frontend.request.Post.RequestPostByUserId;
import com.example.frontend.request.User.RequestCreateAccount;
import com.example.frontend.request.User.RequestLogin;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Post.PostResponse;
import com.example.frontend.response.Post.ResponseCreatePost;
import com.example.frontend.response.Post.ResponsePostById;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.service.PostService;
import com.example.frontend.utils.CallApi;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostRepository {
    PostService postService;

    public PostRepository() {
        postService = CallApi.getRetrofitInstance().create(PostService.class);
    }

    // create post
    public MutableLiveData<ApiResponse<ResponseCreatePost>> createPost(RequestCreatePost request, String userId) {
        MutableLiveData<ApiResponse<ResponseCreatePost>> mutableLiveData = new MutableLiveData<>();
        postService.createPost(request, userId).enqueue(new Callback<ApiResponse<ResponseCreatePost>>() {
            @Override
            public void onResponse(Call<ApiResponse<ResponseCreatePost>> call, Response<ApiResponse<ResponseCreatePost>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<ResponseCreatePost> apiResponse = response.body();
                    mutableLiveData.setValue(apiResponse);
                    Log.e("create post","success: "+ new Gson().toJson(apiResponse.getData()));
                } else {
                    int errorCode = response.code();
                    Log.e("create post",String.valueOf(errorCode));
                    String errorMessage = "Error occurred with code: " + errorCode;
                    ApiResponse<ResponseCreatePost> errorResponse = new ApiResponse<>(false,errorMessage, null);
                    mutableLiveData.setValue(errorResponse);
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<ResponseCreatePost>> call, Throwable t) {
                String errorMessage = "Failed to create post: " + t.getMessage();
                Log.e("create post", errorMessage);
                ApiResponse<ResponseCreatePost> errorResponse = new ApiResponse<>(false, errorMessage, null);
                mutableLiveData.setValue(errorResponse);
            }
        });
        return mutableLiveData;
    }

    // get list post
    public MutableLiveData<ApiResponse<List<RequestPostByUserId>>> getListPostByUserId(String userId) {
        MutableLiveData<ApiResponse<List<RequestPostByUserId>>> mutableLiveData = new MutableLiveData<>();
        postService.getListPostByUserId(userId).enqueue(new Callback<ApiResponse<List<RequestPostByUserId>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<RequestPostByUserId>>> call, Response<ApiResponse<List<RequestPostByUserId>>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<List<RequestPostByUserId>> apiResponse = response.body();
                    Gson gson = new Gson();
                    String json = gson.toJson(apiResponse);
                    mutableLiveData.setValue(apiResponse);
                } else {
                    mutableLiveData.setValue(new ApiResponse<List<RequestPostByUserId>>(false, "Failed to get data from server", null));
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<List<RequestPostByUserId>>> call, Throwable t) {
                mutableLiveData.setValue(new ApiResponse<List<RequestPostByUserId>>(false, "Error: " + t.getMessage(), null));
            }
        });
        return mutableLiveData;
    }

    // add user like post
    public MutableLiveData<ApiResponse<PostResponse>> addLike(String postId, String userId) {
        MutableLiveData<ApiResponse<PostResponse>> mutableLiveData = new MutableLiveData<>();
        postService.addLike(postId, userId).enqueue(new Callback<ApiResponse<PostResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<PostResponse>> call, Response<ApiResponse<PostResponse>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<PostResponse> apiResponse = response.body();
                    mutableLiveData.setValue(apiResponse);
                    Gson gson = new Gson();
                    String data = gson.toJson(apiResponse);
                    Log.d("add",data);
                } else {
                    mutableLiveData.setValue(new ApiResponse<>(false,"Failed add like", null));
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<PostResponse>> call, Throwable t) {
                mutableLiveData.setValue(new ApiResponse<PostResponse>(false, "Error add like: " + t.getMessage(), null));
            }
        });
        return mutableLiveData;
    }

    // get posts by search query
    public MutableLiveData<ApiResponse<List<RequestPostByUserId>>> getListPostsBySearchQuery(String userId, String searchQuery) {
        MutableLiveData<ApiResponse<List<RequestPostByUserId>>> mutableLiveData = new MutableLiveData<>();
        postService.getListPostsBySearchQuery(userId, searchQuery).enqueue(new Callback<ApiResponse<List<RequestPostByUserId>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<RequestPostByUserId>>> call, Response<ApiResponse<List<RequestPostByUserId>>> response) {
                if (response.isSuccessful()) {
                    try {
                        ApiResponse<List<RequestPostByUserId>> listApiResponse = response.body();
                        mutableLiveData.setValue(listApiResponse);
                        Log.d("posts by search query", mutableLiveData.getValue().getData().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<RequestPostByUserId>>> call, Throwable t) {
                Log.i(TAG, "Unable to get data posts by search query");
            }
        });

        return mutableLiveData;
    }

    public MutableLiveData<ApiResponse<ResponsePostById>> getPostById(String id) {
        MutableLiveData<ApiResponse<ResponsePostById>> mutableLiveData = new MutableLiveData<>();
        postService.getPostById(id).enqueue(new Callback<ApiResponse<ResponsePostById>>() {
            @Override
            public void onResponse(Call<ApiResponse<ResponsePostById>> call, Response<ApiResponse<ResponsePostById>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<ResponsePostById> apiResponse = response.body();
                    mutableLiveData.setValue(apiResponse);
                    Gson gson = new Gson();
                    String data = gson.toJson(apiResponse);
                    Log.d("add",data);
                } else {
                    mutableLiveData.setValue(new ApiResponse<>(false,"Failed add like", null));
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<ResponsePostById>> call, Throwable t) {
                mutableLiveData.setValue(new ApiResponse<ResponsePostById>(false, "Error add like: " + t.getMessage(), null));
            }
        });
        return mutableLiveData;
    }
    public MutableLiveData<ApiResponse<List<RequestPostByUserId>>> getListPostUserLiked(String id) {
        MutableLiveData<ApiResponse<List<RequestPostByUserId>>> mutableLiveData = new MutableLiveData<>();
        postService.getListPostUserLiked(id).enqueue(new Callback<ApiResponse<List<RequestPostByUserId>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<RequestPostByUserId>>> call, Response<ApiResponse<List<RequestPostByUserId>>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<List<RequestPostByUserId>> apiResponse = response.body();
                    mutableLiveData.setValue(apiResponse);
                } else {
                    mutableLiveData.setValue(new ApiResponse<>(false,"Failed get list posts", null));
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<List<RequestPostByUserId>>> call, Throwable t) {
                mutableLiveData.setValue(new ApiResponse<List<RequestPostByUserId>>(false, "Error get list posts:" + t.getMessage(), null));
            }
        });
        return mutableLiveData;
    }
}
