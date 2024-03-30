package com.example.frontend.request.GroupChat;

public class RequestRenameGroupChat {
    private String groupId;
    private String newGroupName;

    public RequestRenameGroupChat() {
    }

    public RequestRenameGroupChat(String groupId, String newGroupName) {
        this.groupId = groupId;
        this.newGroupName = newGroupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getNewGroupName() {
        return newGroupName;
    }

    public void setNewGroupName(String newGroupName) {
        this.newGroupName = newGroupName;
    }
}
