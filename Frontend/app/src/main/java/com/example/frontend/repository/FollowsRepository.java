package com.example.frontend.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.frontend.request.Follows.RequestCreateFollows;
import com.example.frontend.request.Follows.RequestUpdateFollows;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Follows.FollowsResponse;
import com.example.frontend.response.Follows.GetQuantityResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.service.FollowsService;
import com.example.frontend.utils.CallApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowsRepository {
    FollowsService followsService;

    public FollowsRepository() {
        followsService = CallApi.getRetrofitInstance().create(FollowsService.class);
    }

    public MutableLiveData<ApiResponse<String>> createFollows(RequestCreateFollows requestCreateFollows) {
        MutableLiveData<ApiResponse<String>> mutableLiveData = new MutableLiveData<>();

        followsService.createFollows(requestCreateFollows).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<String> apiResponse = response.body();
                    mutableLiveData.setValue(apiResponse);
                } else {
                    // Xử lý khi phản hồi không thành công
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                // Xử lý khi gọi API thất bại
            }
        });
        return mutableLiveData;
    }
    public MutableLiveData<ApiResponse<GetQuantityResponse>> getQuantityFollows(String id) {
        MutableLiveData<ApiResponse<GetQuantityResponse>> mutableLiveData = new MutableLiveData<>();

        followsService.getQuantityFollows(id).enqueue(new Callback<ApiResponse<GetQuantityResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<GetQuantityResponse>> call, Response<ApiResponse<GetQuantityResponse>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<GetQuantityResponse> apiResponse = response.body();
                    mutableLiveData.setValue(apiResponse);
                } else {
                    // Xử lý khi phản hồi không thành công
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<GetQuantityResponse>> call, Throwable t) {
                // Xử lý khi gọi API thất bại
            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<ApiResponse<String>> updateFollows(RequestUpdateFollows requestUpdateFollows) {
        MutableLiveData<ApiResponse<String>> mutableLiveData = new MutableLiveData<>();

        followsService.updateFollows(requestUpdateFollows).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<String> apiResponse = response.body();
                    mutableLiveData.setValue(apiResponse);
                } else {
                    // Xử lý khi phản hồi không thành công
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                // Xử lý khi gọi API thất bại
            }
        });
        return mutableLiveData;
    }
}
