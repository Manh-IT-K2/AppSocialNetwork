package com.example.frontend.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.frontend.request.User.RequestCreateAccount;
import com.example.frontend.request.User.RequestLogin;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.service.UserService;
import com.example.frontend.utils.CallApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserRepository {
    UserService userService;
    public UserRepository() {
        userService = CallApi.getRetrofitInstance().create(UserService.class);
    }

    public void registerUser(RequestCreateAccount request) {
        MutableLiveData<ApiResponse<String>> mutableLiveData = new MutableLiveData<>();

        userService.registerUser(request).enqueue(new Callback<ApiResponse<String>>() {
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
    }

    public MutableLiveData<ApiResponse<UserResponse>> login(RequestLogin request) {
        MutableLiveData<ApiResponse<UserResponse>> mutableLiveData = new MutableLiveData<>();
        Log.d("log1", request.isFromGoogle()+"");
        userService.login(request).enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<UserResponse> apiResponse = response.body();
                    mutableLiveData.setValue(apiResponse);
                } else {
                    // Xử lý khi phản hồi không thành công
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                // Xử lý khi gọi API thất bại
            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<ApiResponse<String>> sendOTP(String email) {
        MutableLiveData<ApiResponse<String>> mutableLiveData = new MutableLiveData<>();

        userService.sendOTP(email).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<String> apiResponse = response.body();
                    mutableLiveData.setValue(apiResponse);
                    Log.d("sendOTP1", mutableLiveData.getValue().getData());
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
