package com.example.frontend.request.User;

public class RequestChangePW {
    private String newPw; // Thay đổi lại thành NewPass
    private String email;

    public String getNewPw() {
        return newPw;
    }

    public void setNewPw(String newPw) {
        this.newPw = newPw;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RequestChangePW(String newPw, String email) {
        this.newPw = newPw;
        this.email = email;
    }
}
