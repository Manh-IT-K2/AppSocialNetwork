package com.example.frontend.request.GroupChat;

import java.util.List;

public class RequestCreateGroupChat {
    private String creatorId;
    private String groupName;
    private List<String> memberIds;

    public RequestCreateGroupChat() {
    }

    public RequestCreateGroupChat(String creatorId, String groupName, List<String> memberIds) {
        this.creatorId = creatorId;
        this.groupName = groupName;
        this.memberIds = memberIds;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<String> memberIds) {
        this.memberIds = memberIds;
    }
}
