package com.example.frontend.request.Story;

import java.time.LocalTime;

public class RequestStoryByUserId {
    private String userId;
    private String avtUser;
    private String userName;
    private String contentMedia;
    private String createdAt;

    public RequestStoryByUserId() {
    }

    public RequestStoryByUserId(String userId, String avtUser, String userName, String contentMedia, String createdAt) {
        this.userId = userId;
        this.avtUser = avtUser;
        this.userName = userName;
        this.contentMedia = contentMedia;
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvtUser() {
        return avtUser;
    }

    public void setAvtUser(String avtUser) {
        this.avtUser = avtUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContentMedia() {
        return contentMedia;
    }

    public void setContentMedia(String contentMedia) {
        this.contentMedia = contentMedia;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
