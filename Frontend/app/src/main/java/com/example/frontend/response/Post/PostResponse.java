package com.example.frontend.response.Post;

import java.util.Date;

public class PostResponse {
    private String idPost;
    private String userId;
    private String imagePost;
    private String description;
    private String location;
    private Date createAt;

    public PostResponse() {
    }

    public PostResponse(String idPost, String userId, String imagePost, String description, String location, Date createAt) {
        this.idPost = idPost;
        this.userId = userId;
        this.imagePost = imagePost;
        this.description = description;
        this.location = location;
        this.createAt = createAt;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImagePost() {
        return imagePost;
    }

    public void setImagePost(String imagePost) {
        this.imagePost = imagePost;
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

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
