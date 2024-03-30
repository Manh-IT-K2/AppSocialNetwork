package com.example.frontend.response.Message;

public class PrivateChat {
    private String id;
    private String creatorId;
    private String recipientId;
    private String lastMessageSent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getLastMessageSent() {
        return lastMessageSent;
    }

    public void setLastMessageSent(String lastMessageSent) {
        this.lastMessageSent = lastMessageSent;
    }

    public PrivateChat(String id, String creatorId, String recipientId, String lastMessageSent) {
        this.id = id;
        this.creatorId = creatorId;
        this.recipientId = recipientId;
        this.lastMessageSent = lastMessageSent;
    }
}
