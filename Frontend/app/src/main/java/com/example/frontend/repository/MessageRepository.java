package com.example.frontend.repository;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.frontend.request.PrivateChat.RequestPrivateChat;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.PrivateChat.PrivateChatWithMessagesResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.service.PrivateChatService;
import com.example.frontend.utils.CallApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageRepository {
    PrivateChatService privateChatService;

    public MessageRepository() {
        privateChatService = CallApi.getRetrofitInstance().create(PrivateChatService.class);
    }

    public MutableLiveData<List<PrivateChatWithMessagesResponse>> getListChat(String id) {
        MutableLiveData<List<PrivateChatWithMessagesResponse>> mutableLiveData = new MutableLiveData<>();

        privateChatService.getListChat(id).enqueue(new Callback<ApiResponse<List<PrivateChatWithMessagesResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<PrivateChatWithMessagesResponse>>> call, Response<ApiResponse<List<PrivateChatWithMessagesResponse>>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<List<PrivateChatWithMessagesResponse>> apiResponse = response.body();
                    if (apiResponse != null) {
                        if (apiResponse.isSuccess()) {
                            List<PrivateChatWithMessagesResponse> chatList = apiResponse.getData();
                            mutableLiveData.setValue(chatList);
                        } else {
                            // Xử lý khi có phản hồi không thành công
                        }
                    } else {
                        // Xử lý khi phản hồi là null
                    }
                } else {
                    // Xử lý khi phản hồi HTTP không thành công
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<PrivateChatWithMessagesResponse>>> call, Throwable t) {
                Log.i(TAG, "Không thể lấy dữ liệu");
                // Xử lý khi gặp lỗi
            }
        });

        return mutableLiveData;
    }

    public MutableLiveData<ApiResponse<PrivateChatWithMessagesResponse>> SendMessage(RequestPrivateChat requestPrivateChat) {
        MutableLiveData<ApiResponse<PrivateChatWithMessagesResponse>> mutableLiveData = new MutableLiveData<>();

        privateChatService.SendMessage(requestPrivateChat).enqueue(new Callback<ApiResponse<PrivateChatWithMessagesResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<PrivateChatWithMessagesResponse>> call, Response<ApiResponse<PrivateChatWithMessagesResponse>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<PrivateChatWithMessagesResponse> apiResponse = response.body();
                    mutableLiveData.setValue(apiResponse);
                } else {
                    // Xử lý khi phản hồi không thành công
                    String errorMessage = "Request failed: " + response.code();
                    mutableLiveData.setValue(new ApiResponse<>(false, errorMessage, null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PrivateChatWithMessagesResponse>> call, Throwable t) {
                Log.e(TAG, "Có lỗi khi thực hiện cuộc gọi mạng", t);
                // Xử lý khi gặp lỗi
            }
        });
        return mutableLiveData;
    }





}