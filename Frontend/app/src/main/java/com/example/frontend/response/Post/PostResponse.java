package com.example.frontend.response.Post;

import com.example.frontend.response.User.UserResponse;

import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;


public class PostResponse {
    private String id;
    private ObjectId userId;
    private List<String> imagePost;
    private String description;
    private String location;
    private Date createAt;
    private List<UserResponse> like;

    public PostResponse() {
    }

    public PostResponse(String id, ObjectId userId, List<String> imagePost, String description, String location, Date createAt, List<UserResponse> like) {
        this.id = id;
        this.userId = userId;
        this.imagePost = imagePost;
        this.description = description;
        this.location = location;
        this.createAt = createAt;
        this.like = like;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
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

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public List<UserResponse> getLike() {
        return like;
    }

    public void setLike(List<UserResponse> like) {
        this.like = like;
    }
}
