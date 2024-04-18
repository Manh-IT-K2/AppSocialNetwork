package com.example.frontend.request.GroupChat;
public class RequestChatGroup {
    private String groupId;
    private String senderId;
    private String content;
    private String urlSticker;
    private String urlFile;

    public RequestChatGroup(String groupId, String senderId, String content, String urlSticker, String urlFile) {
        this.groupId = groupId;
        this.senderId = senderId;
        this.content = content;
        this.urlSticker = urlSticker;
        this.urlFile = urlFile;
    }

    public String getUrlSticker() {
        return urlSticker;
    }

    public void setUrlSticker(String urlSticker) {
        this.urlSticker = urlSticker;
    }

    public String getUrlFile() {
        return urlFile;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }



    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
