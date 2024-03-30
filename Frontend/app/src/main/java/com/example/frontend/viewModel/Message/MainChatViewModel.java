package com.example.frontend.viewModel.Message;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.frontend.repository.GroupChatRepository;
import com.example.frontend.response.GroupChat.GroupChatWithMessagesResponse;

import java.util.List;

public class MainChatViewModel extends ViewModel {
    private GroupChatRepository groupChatRepository;
    private MutableLiveData<List<GroupChatWithMessagesResponse>> groupChatList;

    public MainChatViewModel() {
        groupChatRepository = new GroupChatRepository();
        groupChatList = new MutableLiveData<>();
    }

    public LiveData<List<GroupChatWithMessagesResponse>> getListChat(String userId) {
        groupChatList = groupChatRepository.getListChat(userId);
        return groupChatList;
    }
}
