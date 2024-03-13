package com.example.frontend.response.Story;

import java.time.LocalTime;

public class StoryResponse {

    String idStory;
    String userId;
    String contentMedia;
    String createdAt;

    public StoryResponse() {
    }

    public StoryResponse(String idStory, String userId, String contentMedia, String createdAt) {
        this.idStory = idStory;
        this.userId = userId;
        this.contentMedia = contentMedia;
        this.createdAt = createdAt;
    }

    public String getIdStory() {
        return idStory;
    }

    public void setIdStory(String idStory) {
        this.idStory = idStory;
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
