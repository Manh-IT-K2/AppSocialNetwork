package com.example.frontend.response.Post;

import com.example.frontend.response.User.UserResponse;

import java.util.List;

public class ResponseCreatePost {
    String postId;
    List<UserResponse> listFollowers;

    public ResponseCreatePost() {
    }

    public ResponseCreatePost(String postId, List<UserResponse> listFollowers) {
        this.postId = postId;
        this.listFollowers = listFollowers;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public List<UserResponse> getListFollowers() {
        return listFollowers;
    }

    public void setListFollowers(List<UserResponse> listFollowers) {
        this.listFollowers = listFollowers;
    }
}
