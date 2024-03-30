package com.example.frontend.response.PrivateChat;



import com.example.frontend.response.GroupChat.GroupChatWithMessagesResponse;

import java.util.List;

public class ChatListsResponse {
    private List<PrivateChatWithMessagesResponse> privateChats;
    private List<GroupChatWithMessagesResponse> groupChats;

    public ChatListsResponse(List<PrivateChatWithMessagesResponse> privateChats, List<GroupChatWithMessagesResponse> groupChats) {
        this.privateChats = privateChats;
        this.groupChats = groupChats;
    }

    public List<PrivateChatWithMessagesResponse> getPrivateChats() {
        return privateChats;
    }

    public void setPrivateChats(List<PrivateChatWithMessagesResponse> privateChats) {
        this.privateChats = privateChats;
    }

    public List<GroupChatWithMessagesResponse> getGroupChats() {
        return groupChats;
    }

    public void setGroupChats(List<GroupChatWithMessagesResponse> groupChats) {
        this.groupChats = groupChats;
    }
}
