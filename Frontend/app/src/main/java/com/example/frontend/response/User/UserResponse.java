package com.example.frontend.response.User;

public class UserResponse {
    private String id;
    private String username;
    private String password;
    private boolean isFromGoogle;
    private String email;
    private String phoneNumber;
    private String avatarImg;
    private int followers;
    private int following;

    public UserResponse(String id, String username, String password, boolean isFromGoogle, String email, String phoneNumber, String avatarImg, int followers, int following) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isFromGoogle = isFromGoogle;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.avatarImg = avatarImg;
        this.followers = followers;
        this.following = following;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isFromGoogle() {
        return isFromGoogle;
    }

    public void setFromGoogle(boolean fromGoogle) {
        isFromGoogle = fromGoogle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatarImg() {
        return avatarImg;
    }

    public void setAvatarImg(String avatarImg) {
        this.avatarImg = avatarImg;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }
}
