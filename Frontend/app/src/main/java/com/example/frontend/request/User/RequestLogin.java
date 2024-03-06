package com.example.frontend.request.User;

import android.util.Log;

public class RequestLogin {
    String email;
    String password;
    String username;
    String avatarImg;
    boolean fromGoogle;

    public RequestLogin(String email,String username, String avatarImg, String password, boolean fromGoogle) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.avatarImg = avatarImg;
        this.fromGoogle = fromGoogle;
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
}
