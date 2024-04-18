package com.example.frontend.request.Story;

import java.time.LocalTime;
import java.util.List;

public class RequestCreateStory {
    private String userId;
    private String createdAt;
    private String image;
    private List<ContentMedia> contentMedia;
    private List<Stickers> stickers;
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
    public RequestCreateStory() {
    }

    public RequestCreateStory(String userId, String createdAt, String image, List<ContentMedia> contentMedia, List<Stickers> stickers) {
        this.userId = userId;
        this.createdAt = createdAt;
        this.image = image;
        this.contentMedia = contentMedia;
        this.stickers = stickers;
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
}
