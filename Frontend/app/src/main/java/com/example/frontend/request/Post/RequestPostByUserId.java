package com.example.frontend.request.Post;

import java.util.List;

public class RequestPostByUserId {

    String idPost;
    String userId;
    String userName;
    String avtImage;
    List<String> imagePost;
    String description;
    String location;
    String createAt;

    public RequestPostByUserId() {
    }

    public RequestPostByUserId(String idPost, String userId, String userName, String avtImage, List<String> imagePost, String description, String location, String createAt) {
        this.idPost = idPost;
        this.userId = userId;
        this.userName = userName;
        this.avtImage = avtImage;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvtImage() {
        return avtImage;
    }

    public void setAvtImage(String avtImage) {
        this.avtImage = avtImage;
    }

    public List<String> getImagePost() {
        return imagePost;
    }

    public void setImagePost(List<String> imagePost) {
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

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
