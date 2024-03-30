package com.example.frontend.response.Message;

import java.util.Date;

public class Message {
    private String id;
    private String content;
    private Date createdAt;
    private String senderId;
    private String privateChatId;
    private String groupChatId;
    private String urlFile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getPrivateChatId() {
        return privateChatId;
    }

    public void setPrivateChatId(String privateChatId) {
        this.privateChatId = privateChatId;
    }

    public String getGroupChatId() {
        return groupChatId;
    }

    public void setGroupChatId(String groupChatId) {
        this.groupChatId = groupChatId;
    }

    public String getUrlFile() {
        return urlFile;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }

    public Message(String id, String content, Date createdAt, String senderId, String privateChatId, String groupChatId, String urlFile) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.senderId = senderId;
        this.privateChatId = privateChatId;
        this.groupChatId = groupChatId;
        this.urlFile = urlFile;
    }
}
