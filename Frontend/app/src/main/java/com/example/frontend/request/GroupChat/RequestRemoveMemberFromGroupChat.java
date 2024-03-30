package com.example.frontend.request.GroupChat;

import java.util.List;

public class RequestRemoveMemberFromGroupChat {
    private String groupId;
    private List<String> memberIds;

    public RequestRemoveMemberFromGroupChat() {
    }

    public RequestRemoveMemberFromGroupChat(String groupId, List<String> memberIds) {
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
