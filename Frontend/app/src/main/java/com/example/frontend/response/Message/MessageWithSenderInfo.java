package com.example.frontend.response.Message;

import com.example.frontend.response.User.UserResponse;
import com.pusher.client.channel.User;

import java.util.Date;

public class MessageWithSenderInfo {
    private String id;
    private String content;
    private Date createdAt;
    private UserResponse sender;
    private String privateChatId;
    private String groupChatId;
    private String urlFile;
    private String urlSticker;

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

    public UserResponse getSender() {
        return sender;
    }

    public void setSender(UserResponse sender) {
        this.sender = sender;
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

    public String getUrlSticker() {
        return urlSticker;
    }

    public void setUrlSticker(String urlSticker) {
        this.urlSticker = urlSticker;
    }

    public MessageWithSenderInfo(String id, String content, Date createdAt, UserResponse sender, String privateChatId, String groupChatId, String urlFile, String urlSticker) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.sender = sender;
        this.privateChatId = privateChatId;
        this.groupChatId = groupChatId;
        this.urlFile = urlFile;
        this.urlSticker = urlSticker;
    }
}
