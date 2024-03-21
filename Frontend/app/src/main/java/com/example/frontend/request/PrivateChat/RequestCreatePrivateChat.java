package com.example.frontend.request.PrivateChat;

public class RequestCreatePrivateChat {
    String creatorId;
    String recipientId;

    public RequestCreatePrivateChat(String creatorId, String recipientId) {
        this.creatorId = creatorId;
        this.recipientId = recipientId;
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
}
