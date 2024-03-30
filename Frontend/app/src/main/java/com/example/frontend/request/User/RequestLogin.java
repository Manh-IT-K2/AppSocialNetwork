package com.example.frontend.request.User;

import android.util.Log;

public class RequestLogin {
    String email;
    String password;
    String username;
    String avatarImg;
    String name;
    boolean fromGoogle;

    public RequestLogin(String email,String username, String avatarImg, String password, boolean fromGoogle,String name) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.avatarImg = avatarImg;
        this.fromGoogle = fromGoogle;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isFromGoogle() {
        return fromGoogle;
    }

    public void setFromGoogle(boolean fromGoogle) {
        this.fromGoogle = fromGoogle;
    }

    public String getAvatarImg() {
        return avatarImg;
    }

    public void setAvatarImg(String avatarImg) {
        this.avatarImg = avatarImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
