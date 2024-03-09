package com.example.frontend.request.Post;

public class RequestPostByUserId {

    String idPost;
    String userId;
    String userName;
    String avtImage;
    String imagePost;
    String description;

    public RequestPostByUserId() {
    }

    public RequestPostByUserId(String idPost, String userId, String userName, String avtImage, String imagePost, String description) {
        this.idPost = idPost;
        this.userId = userId;
        this.userName = userName;
        this.avtImage = avtImage;
        this.imagePost = imagePost;
        this.description = description;
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
}
