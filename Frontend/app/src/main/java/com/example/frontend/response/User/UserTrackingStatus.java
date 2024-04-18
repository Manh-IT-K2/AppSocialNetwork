package com.example.frontend.response.User;

public class UserTrackingStatus {

    private String id;
    private String username;
    private String name;
    private String status;
    private String avatarImg;

    public UserTrackingStatus(String id, String username, String name, String status, String avatarImg) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.status = status;
        this.avatarImg = avatarImg;
    }

    public UserTrackingStatus() {}

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvatarImg() {
        return avatarImg;
    }

    public void setAvatarImg(String avatarImg) {
        this.avatarImg = avatarImg;
    }
}
