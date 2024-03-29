package com.example.frontend.response.GroupChat;

import com.example.frontend.response.User.UserResponse;

import java.util.List;

public class GroupChatResponse {
    private String id;
    private String groupName;
    private UserResponse creator;
    private List<UserResponse> members;

    public GroupChatResponse() {
    }

    public GroupChatResponse(String id, String groupName, UserResponse creator, List<UserResponse> members) {
        this.id = id;
        this.groupName = groupName;
        this.creator = creator;
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

    public UserResponse getCreator() {
        return creator;
    }

    public void setCreator(UserResponse creator) {
        this.creator = creator;
    }

    public List<UserResponse> getMembers() {
        return members;
    }

    public void setMembers(List<UserResponse> members) {
        this.members = members;
    }
}
