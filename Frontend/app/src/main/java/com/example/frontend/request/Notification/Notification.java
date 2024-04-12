package com.example.frontend.request.Notification;

public class Notification {
    String userId;
    String text;
    String postId;
    boolean isLikePost;
    boolean isComment;
    boolean isReplyComment;
    String idComment;
    boolean isFollow;
    String idRecipient;

    public Notification(){}

    public Notification(String userId, String text, String postId, boolean isLikePost, boolean isComment, boolean isReplyComment, String idComment) {
        this.userId = userId;
        this.text = text;
        this.postId = postId;
        this.isLikePost = isLikePost;
        this.isComment = isComment;
        this.isReplyComment = isReplyComment;
        this.idComment = idComment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
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

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public String getIdRecipient() {
        return idRecipient;
    }

    public void setIdRecipient(String idRecipient) {
        this.idRecipient = idRecipient;
    }
}
