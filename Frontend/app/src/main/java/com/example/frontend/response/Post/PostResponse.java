package com.example.frontend.response.Post;

public class PostResponse {
    private String postId;
    private String image;
    private String description;
    private String publisher;

    public PostResponse() {
    }

    public PostResponse(String postId, String image, String description, String publisher) {
        this.postId = postId;
        this.image = image;
        this.description = description;
        this.publisher = publisher;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
