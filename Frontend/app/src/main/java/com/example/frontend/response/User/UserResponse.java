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
    private String website;
    private String bio;
    private String gender;
    private String name;
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public UserResponse(String id, String username, String password, boolean isFromGoogle, String email, String phoneNumber, String avatarImg, int followers, int following, boolean status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isFromGoogle = isFromGoogle;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.avatarImg = avatarImg;
        this.followers = followers;
        this.following = following;
        this.status = status;
    }

    public UserResponse(String id, String username, String password, boolean isFromGoogle, String email, String phoneNumber, String avatarImg, int followers, int following, String website, String bio, String gender,String name) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isFromGoogle = isFromGoogle;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.avatarImg = avatarImg;
        this.followers = followers;
        this.following = following;
        this.website = website;
        this.bio = bio;
        this.gender = gender;
        this.name = name;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}