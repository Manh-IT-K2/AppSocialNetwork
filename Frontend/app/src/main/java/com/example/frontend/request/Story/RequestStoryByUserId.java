package com.example.frontend.request.Story;

import com.example.frontend.response.User.UserResponse;

import java.time.LocalTime;
import java.util.List;

public class RequestStoryByUserId {
    private String idStory;
    private String userId;
    private String avtUser;
    private String userName;
    private String createdAt;
    private String image;
    private int status;
    private List<RequestCreateStory.ContentMedia> contentMedia;
    private List<RequestCreateStory.Stickers> stickers;
    private List<UserResponse> seen;
    public static class ContentMedia {
        private String content;
        private float x;
        private float y;

        public ContentMedia() {
        }

        public ContentMedia(String content, float x, float y) {
            this.content = content;
            this.x = x;
            this.y = y;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }
    public static class Stickers {
        private String uriSticker;
        private float x;
        private float y;

        public Stickers() {
        }

        public Stickers(String uriSticker, float x, float y) {
            this.uriSticker = uriSticker;
            this.x = x;
            this.y = y;
        }

        public String getUriSticker() {
            return uriSticker;
        }

        public void setUriSticker(String uriSticker) {
            this.uriSticker = uriSticker;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }
    public RequestStoryByUserId() {
    }

    public RequestStoryByUserId(String idStory, String userId, String avtUser, String userName, String createdAt, String image, int status, List<RequestCreateStory.ContentMedia> contentMedia, List<RequestCreateStory.Stickers> stickers, List<UserResponse> seen) {
        this.idStory = idStory;
        this.userId = userId;
        this.avtUser = avtUser;
        this.userName = userName;
        this.createdAt = createdAt;
        this.image = image;
        this.status = status;
        this.contentMedia = contentMedia;
        this.stickers = stickers;
        this.seen = seen;
    }

    public String getIdStory() {
        return idStory;
    }

    public void setIdStory(String idStory) {
        this.idStory = idStory;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvtUser() {
        return avtUser;
    }

    public void setAvtUser(String avtUser) {
        this.avtUser = avtUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<RequestCreateStory.ContentMedia> getContentMedia() {
        return contentMedia;
    }

    public void setContentMedia(List<RequestCreateStory.ContentMedia> contentMedia) {
        this.contentMedia = contentMedia;
    }

    public List<RequestCreateStory.Stickers> getStickers() {
        return stickers;
    }

    public void setStickers(List<RequestCreateStory.Stickers> stickers) {
        this.stickers = stickers;
    }

    public List<UserResponse> getSeen() {
        return seen;
    }

    public void setSeen(List<UserResponse> seen) {
        this.seen = seen;
    }
}
