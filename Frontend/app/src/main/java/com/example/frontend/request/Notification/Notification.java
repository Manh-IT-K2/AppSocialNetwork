package com.example.frontend.request.Notification;

public class Notification {
    String userId;
    String text;
    String postId;
    String idComment;
    String idRecipient;

    public Notification(){}

    public Notification(String userId, String text, String postId, String idComment) {
        this.userId = userId;
        this.text = text;
        this.postId = postId;
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

    public String getIdComment() {
        return idComment;
    }

    public void setIdComment(String idComment) {
        this.idComment = idComment;
    }

    public String getIdRecipient() {
        return idRecipient;
    }

    public void setIdRecipient(String idRecipient) {
        this.idRecipient = idRecipient;
    }
}
