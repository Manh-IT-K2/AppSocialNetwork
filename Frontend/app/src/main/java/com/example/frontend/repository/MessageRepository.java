package com.example.frontend.repository;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.PrivateChat.PrivateChatWithMessagesResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.service.PostService;
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

    public MutableLiveData<PrivateChatWithMessagesResponse> getListChat(String id) {
        MutableLiveData<PrivateChatWithMessagesResponse> mutableLiveData = new MutableLiveData<>();

        privateChatService.getListChat(id).enqueue(new Callback<ApiResponse<List<PrivateChatWithMessagesResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<PrivateChatWithMessagesResponse>>> call, Response<ApiResponse<List<PrivateChatWithMessagesResponse>>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<List<PrivateChatWithMessagesResponse>> apiResponse = response.body();
                    if (apiResponse != null) {
                        if (apiResponse.isSuccess()) {
                            List<PrivateChatWithMessagesResponse> chatList = apiResponse.getData();
                            if (chatList != null && !chatList.isEmpty()) {
                                mutableLiveData.setValue(chatList.get(0)); // Assuming you only need the first chat in the list
                            } else {
                                // Handle empty or null chat list
                            }
                        } else {
                            // Handle unsuccessful response
                        }
                    } else {
                        // Handle null response
                    }
                } else {
                    // Handle unsuccessful HTTP response
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<PrivateChatWithMessagesResponse>>> call, Throwable t) {
                Log.i(TAG, "Unable to get data");
                // Handle failure
            }
        });

        return mutableLiveData;
    }
}