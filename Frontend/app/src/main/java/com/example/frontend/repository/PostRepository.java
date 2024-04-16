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
    public void createPost(RequestCreatePost request, String userId) {
        MutableLiveData<ApiResponse<String>> mutableLiveData = new MutableLiveData<>();
        postService.createPost(request, userId).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<String> apiResponse = response.body();
                    mutableLiveData.setValue(apiResponse);
                    Log.d("create","success");
                } else {
                    int errorCode = response.code();
                    Log.d("create",String.valueOf(errorCode));
                    String errorMessage = "Error occurred with code: " + errorCode;
                    ApiResponse<String> errorResponse = new ApiResponse<>(false,"", errorMessage);
                    mutableLiveData.setValue(errorResponse);
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                String errorMessage = "Failed to create post: " + t.getMessage();
                ApiResponse<String> errorResponse = new ApiResponse<>(false,"", errorMessage);
                mutableLiveData.setValue(errorResponse);
            }
        });
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
