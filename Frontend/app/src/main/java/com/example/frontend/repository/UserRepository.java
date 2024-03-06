package com.example.frontend.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.frontend.request.User.RequestCreateAccount;
import com.example.frontend.response.ApiResponse.ApiResponse;
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

    public MutableLiveData<ApiResponse<Object>> registerUser(RequestCreateAccount request) {
        MutableLiveData<ApiResponse<Object>> mutableLiveData = new MutableLiveData<>();

        userService.registerUser(request).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<Object> apiResponse = response.body();
                    mutableLiveData.setValue(apiResponse);
                } else {
                    // Xử lý khi phản hồi không thành công
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                // Xử lý khi gọi API thất bại
            }
        });

        return mutableLiveData;
    }

    public String sendOTP(String email) {
        return "";
    }
}
