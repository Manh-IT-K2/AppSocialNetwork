package com.example.frontend.response.Post;

import com.example.frontend.response.User.UserResponse;

public class ResponsePostById {
    PostResponse post;
    UserResponse user;
    boolean isLike;

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public ResponsePostById() {
    }

    public PostResponse getPost() {
        return post;
    }

    public void setPost(PostResponse post) {
        this.post = post;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}
