package com.example.frontend.response.GroupChat;

import com.example.frontend.response.Message.MessageWithSenderInfo;
import com.example.frontend.response.User.UserResponse;

import java.util.List;

public class GroupChatWithMessagesResponse {
    private String id;
    private String groupName;
    private List<UserResponse> members;
    private List<MessageWithSenderInfo> messages;

    public GroupChatWithMessagesResponse() {
    }

    public GroupChatWithMessagesResponse(String id, String groupName, List<UserResponse> members, List<MessageWithSenderInfo> messages) {
        this.id = id;
        this.groupName = groupName;
        this.members = members;
        this.messages = messages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<UserResponse> getMembers() {
        return members;
    }

    public void setMembers(List<UserResponse> members) {
        this.members = members;
    }

    public List<MessageWithSenderInfo> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageWithSenderInfo> messages) {
        this.messages = messages;
    }
}
