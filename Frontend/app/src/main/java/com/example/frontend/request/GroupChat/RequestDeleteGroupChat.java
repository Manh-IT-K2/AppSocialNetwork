package com.example.frontend.request.GroupChat;

public class RequestDeleteGroupChat {
    private String groupId;

    public RequestDeleteGroupChat() {
    }

    public RequestDeleteGroupChat(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
