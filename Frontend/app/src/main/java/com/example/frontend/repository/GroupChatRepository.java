package com.example.frontend.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.GroupChat.GroupChatWithMessagesResponse;
import com.example.frontend.service.GroupChatService;
import com.example.frontend.utils.CallApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupChatRepository {
    private GroupChatService groupChatService;

    public GroupChatRepository() {
        groupChatService = CallApi.getRetrofitInstance().create(GroupChatService.class);
    }

    public MutableLiveData<List<GroupChatWithMessagesResponse>> getListChat(String userId) {
        MutableLiveData<List<GroupChatWithMessagesResponse>> mutableLiveData = new MutableLiveData<>();

        groupChatService.getListChat(userId).enqueue(new Callback<List<GroupChatWithMessagesResponse>>() {
            @Override
            public void onResponse(Call<List<GroupChatWithMessagesResponse>> call, Response<List<GroupChatWithMessagesResponse>> response) {
                if (response.isSuccessful()) {
                    List<GroupChatWithMessagesResponse> chatList = response.body();
                    mutableLiveData.setValue(chatList);
                } else {
                    Log.e("GroupChatRepository", "Phản hồi HTTP không thành công: " + response.code());
                    // Xử lý khi phản hồi HTTP không thành công
                }
            }

            @Override
            public void onFailure(Call<List<GroupChatWithMessagesResponse>> call, Throwable t) {
                Log.e("GroupChatRepository", "Không thể lấy dữ liệu danh sách cuộc trò chuyện nhóm: " + t.getMessage(), t);
                // Xử lý khi gặp lỗi
            }
        });

        return mutableLiveData;
    }
}
