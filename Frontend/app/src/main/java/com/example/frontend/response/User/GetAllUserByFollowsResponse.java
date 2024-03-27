package com.example.frontend.response.User;

public class GetAllUserByFollowsResponse {
    String id;
    String username;
    String avatarImg;
    String name;

    public GetAllUserByFollowsResponse(String id, String username, String avatarImg, String name) {
        this.id = id;
        this.username = username;
        this.avatarImg = avatarImg;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarImg() {
        return avatarImg;
    }

    public void setAvatarImg(String avatarImg) {
        this.avatarImg = avatarImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
