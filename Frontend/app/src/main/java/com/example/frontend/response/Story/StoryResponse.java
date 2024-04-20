package com.example.frontend.response.Story;

import com.example.frontend.response.User.UserResponse;

import java.time.LocalTime;
import java.util.List;

public class StoryResponse {

    private String idStory;
    private String userId;
    private String createdAt;
    private String image;

    private int status;
    private List<ContentMedia> contentMedia;
    private List<Stickers> stickers;
    private List<UserResponse> seen;
    public static class ContentMedia {
        private String content;
        private float x;
        private float y;

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

    public StoryResponse() {
    }

    public StoryResponse(String idStory, String userId, String createdAt, String image, int status, List<ContentMedia> contentMedia, List<Stickers> stickers, List<UserResponse> seen) {
        this.idStory = idStory;
        this.userId = userId;
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

    public List<ContentMedia> getContentMedia() {
        return contentMedia;
    }

    public void setContentMedia(List<ContentMedia> contentMedia) {
        this.contentMedia = contentMedia;
    }

    public List<Stickers> getStickers() {
        return stickers;
    }

    public void setStickers(List<Stickers> stickers) {
        this.stickers = stickers;
    }

    public List<UserResponse> getSeen() {
        return seen;
    }

    public void setSeen(List<UserResponse> seen) {
        this.seen = seen;
    }
}
