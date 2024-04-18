package com.example.frontend.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.frontend.request.GroupChat.RequestAddMemberToGroupChat;
import com.example.frontend.request.GroupChat.RequestChatGroup;
import com.example.frontend.request.GroupChat.RequestCreateGroupChat;
import com.example.frontend.request.GroupChat.RequestRemoveMemberFromGroupChat;
import com.example.frontend.request.GroupChat.RequestRenameGroupChat;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.GroupChat.GroupChatResponse;
import com.example.frontend.response.GroupChat.GroupChatWithMessagesResponse;
import com.example.frontend.response.Message.Message;
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

    public LiveData<ApiResponse<GroupChatResponse>> createGroupChat(RequestCreateGroupChat request) {
        MutableLiveData<ApiResponse<GroupChatResponse>> mutableLiveData = new MutableLiveData<>();

        groupChatService.createGroupChat(request).enqueue(new Callback<ApiResponse<GroupChatResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<GroupChatResponse>> call, Response<ApiResponse<GroupChatResponse>> response) {
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body());
                } else {
                    Log.e("GroupChatRepository", "Phản hồi HTTP không thành công: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<GroupChatResponse>> call, Throwable t) {
                Log.e("GroupChatRepository", "Failed to create group chat: " + t.getMessage(), t);
            }
        });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<GroupChatWithMessagesResponse>> getMessagesByGroupChatId(String groupChatId) {
        MutableLiveData<ApiResponse<GroupChatWithMessagesResponse>> mutableLiveData = new MutableLiveData<>();

        groupChatService.getMessagesByGroupChatId(groupChatId).enqueue(new Callback<ApiResponse<GroupChatWithMessagesResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<GroupChatWithMessagesResponse>> call, Response<ApiResponse<GroupChatWithMessagesResponse>> response) {
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body());
                } else {
                    Log.e("GroupChatRepository", "Phản hồi HTTP không thành công: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<GroupChatWithMessagesResponse>> call, Throwable t) {
                Log.e("GroupChatRepository", "Failed to get messages for group chat: " + t.getMessage(), t);
            }
        });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<Message>> sendMessage(String groupChatId, RequestChatGroup request) {
        MutableLiveData<ApiResponse<Message>> mutableLiveData = new MutableLiveData<>();

        groupChatService.sendMessage(groupChatId, request).enqueue(new Callback<ApiResponse<Message>>() {
            @Override
            public void onResponse(Call<ApiResponse<Message>> call, Response<ApiResponse<Message>> response) {
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body());
                } else {
                    Log.e("GroupChatRepository", "Phản hồi HTTP không thành công: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Message>> call, Throwable t) {
                Log.e("GroupChatRepository", "Failed to send message to group chat: " + t.getMessage(), t);
            }
        });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<String>> addMemberToGroupChat(String groupChatId, RequestAddMemberToGroupChat request) {
        MutableLiveData<ApiResponse<String>> mutableLiveData = new MutableLiveData<>();

        groupChatService.addMemberToGroupChat(groupChatId, request).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body());
                } else {
                    Log.e("GroupChatRepository", "Phản hồi HTTP không thành công: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Log.e("GroupChatRepository", "Failed to add member to group chat: " + t.getMessage(), t);
            }
        });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<String>> removeMemberFromGroupChat(String groupChatId, RequestRemoveMemberFromGroupChat request) {
        MutableLiveData<ApiResponse<String>> mutableLiveData = new MutableLiveData<>();

        groupChatService.removeMemberFromGroupChat(groupChatId, request).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body());
                } else {
                    Log.e("GroupChatRepository", "Phản hồi HTTP không thành công: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Log.e("GroupChatRepository", "Failed to remove member from group chat: " + t.getMessage(), t);
            }
        });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<String>> renameGroupChat(String groupChatId, RequestRenameGroupChat request) {
        MutableLiveData<ApiResponse<String>> mutableLiveData = new MutableLiveData<>();

        groupChatService.renameGroupChat(groupChatId, request).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body());
                } else {
                    Log.e("GroupChatRepository", "Phản hồi HTTP không thành công: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Log.e("GroupChatRepository", "Failed to rename group chat: " + t.getMessage(), t);
            }
        });

        return mutableLiveData;
    }

    public LiveData<ApiResponse<String>> deleteGroupChat(String groupChatId) {
        MutableLiveData<ApiResponse<String>> mutableLiveData = new MutableLiveData<>();

        groupChatService.deleteGroupChat(groupChatId).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body());
                } else {
                    Log.e("GroupChatRepository", "Phản hồi HTTP không thành công: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Log.e("GroupChatRepository", "Failed to delete group chat: " + t.getMessage(), t);
            }
        });

        return mutableLiveData;
    }
    public MutableLiveData<GroupChatResponse> getGroupChatById(String groupChatId) {
        MutableLiveData<GroupChatResponse> mutableLiveData = new MutableLiveData<>();

        groupChatService.getGroupChatById(groupChatId).enqueue(new Callback<GroupChatResponse>() {
            @Override
            public void onResponse(Call<GroupChatResponse> call, Response<GroupChatResponse> response) {
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body());
                } else {
                    Log.e("GroupChatRepository", "Phản hồi HTTP không thành công: " + response.code());
                    // Xử lý khi phản hồi HTTP không thành công
                }
            }

            @Override
            public void onFailure(Call<GroupChatResponse> call, Throwable t) {
                Log.e("GroupChatRepository", "Không thể lấy dữ liệu nhóm chat: " + t.getMessage(), t);
                // Xử lý khi gặp lỗi
            }
        });

        return mutableLiveData;
    }




}
