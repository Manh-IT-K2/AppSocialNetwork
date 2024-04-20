package com.example.frontend.utils;

import java.util.Date;

public class Story {
    private String imageUrl;
    private Date date;

    public Story(String imageUrl, Date date) {
        this.imageUrl = imageUrl;
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
