package com.example.frontend.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.frontend.request.User.RequestCreateAccount;
import com.example.frontend.service.UserService;
import com.example.frontend.utils.CallApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserRepository {
    UserService userService;
    public UserRepository() {
        Retrofit retrofit = CallApi.retrofit;

        userService = retrofit.create(UserService.class);
    }

    public void registerUser(RequestCreateAccount request) {
        Call<String> call = userService.registerUser(request);
//        String data = "";
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (response.isSuccessful()) {
//                    data = response.body();
//                    // Xử lý phản hồi thành công
//                } else {
//                    // Xử lý khi gặp lỗi
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                // Xử lý khi gặp lỗi
//            }
//        });
//        return data;
    }

    public String sendOTP(String email) {
        return "";
    }
}
