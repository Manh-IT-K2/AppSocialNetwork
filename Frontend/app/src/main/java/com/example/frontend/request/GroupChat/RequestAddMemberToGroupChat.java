package com.example.frontend.request.GroupChat;

import java.util.List;

public class RequestAddMemberToGroupChat {
    private String groupId;
    private List<String> memberIds;

    public RequestAddMemberToGroupChat() {
    }

    public RequestAddMemberToGroupChat(String groupId, List<String> memberIds) {
        this.groupId = groupId;
        this.memberIds = memberIds;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<String> memberIds) {
        this.memberIds = memberIds;
    }
}
