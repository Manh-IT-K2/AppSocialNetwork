package com.example.frontend.utils;

import com.denzcoskun.imageslider.constants.ScaleTypes;

public class CustomSlideModel {
    private String imageUrl;
    private ScaleTypes scaleType;
    private boolean issNoDots;

    public CustomSlideModel(String imageUrl, ScaleTypes scaleType, boolean issNoDots) {
        this.imageUrl = imageUrl;
        this.scaleType = scaleType;
        this.issNoDots = issNoDots;
    }

    // Getter và setter cho imageUrl và scaleType

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ScaleTypes getScaleType() {
        return scaleType;
    }

    public void setScaleType(ScaleTypes scaleType) {
        this.scaleType = scaleType;
    }

    public boolean isIssNoDots() {
        return issNoDots;
    }

    public void setIssNoDots(boolean issNoDots) {
        this.issNoDots = issNoDots;
    }
}

