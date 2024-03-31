package com.example.frontend.viewModel.Message;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.frontend.repository.GroupChatRepository;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.GroupChat.GroupChatResponse;
import com.example.frontend.response.GroupChat.GroupChatWithMessagesResponse;
import com.example.frontend.request.GroupChat.*;

import java.util.List;

public class GroupChatViewModel extends ViewModel {
    private final GroupChatRepository groupChatRepository;

    public GroupChatViewModel() {
        groupChatRepository = new GroupChatRepository();
    }

    public LiveData<ApiResponse<GroupChatResponse>> createGroupChat(RequestCreateGroupChat request) {
        return groupChatRepository.createGroupChat(request);
    }

    public LiveData<ApiResponse<GroupChatWithMessagesResponse>> getMessagesByGroupChatId(String groupChatId) {
        return groupChatRepository.getMessagesByGroupChatId(groupChatId);
    }

    public LiveData<ApiResponse<GroupChatWithMessagesResponse>> sendMessage(String groupChatId, RequestChatGroup request) {
        return groupChatRepository.sendMessage(groupChatId, request);
    }

    public LiveData<ApiResponse<String>> addMemberToGroupChat(String groupChatId, RequestAddMemberToGroupChat request) {
        return groupChatRepository.addMemberToGroupChat(groupChatId, request);
    }

    public LiveData<ApiResponse<String>> removeMemberFromGroupChat(String groupChatId, RequestRemoveMemberFromGroupChat request) {
        return groupChatRepository.removeMemberFromGroupChat(groupChatId, request);
    }

    public LiveData<ApiResponse<String>> renameGroupChat(String groupChatId, RequestRenameGroupChat request) {
        return groupChatRepository.renameGroupChat(groupChatId, request);
    }

    public LiveData<ApiResponse<String>> deleteGroupChat(String groupChatId) {
        return groupChatRepository.deleteGroupChat(groupChatId);
    }

    public LiveData<List<GroupChatWithMessagesResponse>> getListChat(String userId) {
        return groupChatRepository.getListChat(userId);
    }
}
