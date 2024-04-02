package com.example.frontend.response.GroupChat;

import com.example.frontend.response.User.UserResponse;

import java.util.List;

public class GroupChatResponse {
    private String id;
    private String groupName;
    private String creatorId; // Thay đổi từ creator thành creatorId
    private List<UserResponse> members;

    public GroupChatResponse() {
    }

    public GroupChatResponse(String id, String groupName, String creatorId, List<UserResponse> members) {
        this.id = id;
        this.groupName = groupName;
        this.creatorId = creatorId; // Thay đổi creator thành creatorId
        this.members = members;
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

    public String getCreatorId() { // Thay đổi getCreator thành getCreatorId
        return creatorId;
    }

    public void setCreatorId(String creatorId) { // Thay đổi setCreator thành setCreatorId
        this.creatorId = creatorId;
    }

    public List<UserResponse> getMembers() {
        return members;
    }

    public void setMembers(List<UserResponse> members) {
        this.members = members;
    }
    @Override
    public String toString() {
        return "GroupChatResponse{" +
                "id='" + id + '\'' +
                ", groupName='" + groupName + '\'' +
                ", creator='" + creatorId + '\'' +
                ", members=" + members +
                '}';
    }
}



