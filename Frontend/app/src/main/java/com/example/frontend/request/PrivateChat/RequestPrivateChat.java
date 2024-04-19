package com.example.frontend.request.PrivateChat;

import java.util.Date;

public class RequestPrivateChat {
    String creatorId;
    String recipientId;
    String lastMessageSent;
    String urlFile;
    String urlSticker;

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

    public String getUrlFile() {
        return urlFile;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }

    public String getUrlSticker() {
        return urlSticker;
    }

    public void setUrlSticker(String urlSticker) {
        this.urlSticker = urlSticker;
    }

    public RequestPrivateChat(String creatorId, String recipientId, String lastMessageSent, String urlFile, String urlSticker) {
        this.creatorId = creatorId;
        this.recipientId = recipientId;
        this.lastMessageSent = lastMessageSent;
        this.urlFile = urlFile;
        this.urlSticker = urlSticker;
    }
}
