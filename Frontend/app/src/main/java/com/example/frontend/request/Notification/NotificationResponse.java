package com.example.frontend.request.Notification;

import com.example.frontend.response.User.UserResponse;

public class NotificationResponse {
    UserResponse user;
    String text;
    String idPost;
    boolean isLikePost;
    boolean isComment;
    boolean isReplyComment;
    String idComment;
    private String createAt;
    boolean isFollow;

    public NotificationResponse() {
    }

    public NotificationResponse(UserResponse user, String text, String idPost, boolean isLikePost, boolean isComment, boolean isReplyComment, String idComment, String createAt, boolean isFollow) {
        this.user = user;
        this.text = text;
        this.idPost = idPost;
        this.isLikePost = isLikePost;
        this.isComment = isComment;
        this.isReplyComment = isReplyComment;
        this.idComment = idComment;
        this.createAt = createAt;
        this.isFollow = isFollow;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String postId) {
        this.idPost = postId;
    }

    public boolean isLikePost() {
        return isLikePost;
    }

    public void setLikePost(boolean likePost) {
        isLikePost = likePost;
    }

    public boolean isComment() {
        return isComment;
    }

    public void setComment(boolean comment) {
        isComment = comment;
    }

    public boolean isReplyComment() {
        return isReplyComment;
    }

    public void setReplyComment(boolean replyComment) {
        isReplyComment = replyComment;
    }

    public String getIdComment() {
        return idComment;
    }

    public void setIdComment(String idComment) {
        this.idComment = idComment;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }
}
