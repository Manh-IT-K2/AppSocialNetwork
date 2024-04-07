package com.example.frontend.response.Comment;

import com.example.frontend.response.User.UserResponse;

import java.util.Date;
import java.util.List;

public class CommentResponse {
    private String id;

    private List<CommentResponse> replyComment;

    private String idPost;

    private UserResponse user;

    private String content;

    private String createAt;

    private UserResponse userReply;
    private List<UserResponse> like;
    private boolean isLike;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CommentResponse> getReplyComment() {
        return replyComment;
    }

    public void setReplyComment(List<CommentResponse> replyComment) {
        this.replyComment = replyComment;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public UserResponse getUserReply() {
        return userReply;
    }

    public void setUserReply(UserResponse userReply) {
        this.userReply = userReply;
    }

    public List<UserResponse> getLike() {
        return like;
    }

    public void setLike(List<UserResponse> like) {
        this.like = like;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public CommentResponse(String id, List<CommentResponse> replyComment, String idPost, UserResponse user, String content, String createAt, UserResponse userReply, List<UserResponse> like, boolean isLike) {
        this.id = id;
        this.replyComment = replyComment;
        this.idPost = idPost;
        this.user = user;
        this.content = content;
        this.createAt = createAt;
        this.userReply = userReply;
        this.like = like;
        this.isLike = isLike;
    }

    public CommentResponse() {
    }

}
