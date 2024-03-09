package com.example.frontend.request.Post;

public class RequestCreatePost {
     String imagePost;
     String userId;
     String description;

    public RequestCreatePost(String imagePost, String userId, String description) {
        this.imagePost = imagePost;
        this.userId = userId;
        this.description = description;
    }

    public RequestCreatePost() {
    }

    public String getImagePost() {
        return imagePost;
    }

    public void setImagePost(String imagePost) {
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
}
