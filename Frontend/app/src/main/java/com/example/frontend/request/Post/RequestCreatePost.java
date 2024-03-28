package com.example.frontend.request.Post;

import java.util.Date;
import java.util.List;

public class RequestCreatePost {
    List<String> imagePost;
    String userId;
    String description;
    String location;
    String createAt;

    public RequestCreatePost() {
    }

    public RequestCreatePost(List<String> imagePost, String userId, String description, String location, String createAt) {
        this.imagePost = imagePost;
        this.userId = userId;
        this.description = description;
        this.location = location;
        this.createAt = createAt;
    }

    public List<String> getImagePost() {
        return imagePost;
    }

    public void setImagePost(List<String> imagePost) {
        this.imagePost = imagePost;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}