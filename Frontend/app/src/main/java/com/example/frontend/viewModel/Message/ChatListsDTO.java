package com.example.frontend.viewModel.Message;

import com.example.frontend.response.GroupChat.GroupChatWithMessagesResponse;
import com.example.frontend.response.PrivateChat.PrivateChatWithMessagesResponse;

import java.util.List;

public class ChatListsDTO {
    private List<PrivateChatWithMessagesResponse> privateChatList;
    private List<GroupChatWithMessagesResponse> groupChatList;

    public ChatListsDTO(List<PrivateChatWithMessagesResponse> privateChatList, List<GroupChatWithMessagesResponse> groupChatList) {
        this.privateChatList = privateChatList;
        this.groupChatList = groupChatList;
    }

    public List<PrivateChatWithMessagesResponse> getPrivateChatList() {
        return privateChatList;
    }

    public List<GroupChatWithMessagesResponse> getGroupChatList() {
        return groupChatList;
    }
}
