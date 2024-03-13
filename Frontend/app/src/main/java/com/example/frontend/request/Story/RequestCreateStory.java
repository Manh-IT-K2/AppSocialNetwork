package com.example.frontend.request.Story;

import java.time.LocalTime;

public class RequestCreateStory {
    String userId;
    String contentMedia;
    String createdAt;

    public RequestCreateStory() {
    }

    public RequestCreateStory(String userId, String contentMedia, String createdAt) {
        this.userId = userId;
        this.contentMedia = contentMedia;
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
