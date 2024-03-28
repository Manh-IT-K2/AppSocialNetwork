package com.example.frontend.request.PrivateChat;

import java.util.Date;

public class RequestPrivateChat {
    String creatorId;
    String recipientId;
    Date createdAt;
    String lastMessageSent;

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastMessageSent() {
        return lastMessageSent;
    }

    public void setLastMessageSent(String lastMessageSent) {
        this.lastMessageSent = lastMessageSent;
    }

    public RequestPrivateChat(String creatorId, String recipientId, Date createdAt, String lastMessageSent) {
        this.creatorId = creatorId;
        this.recipientId = recipientId;
        this.createdAt = createdAt;
        this.lastMessageSent = lastMessageSent;
    }
}
