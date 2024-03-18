package com.example.frontend.request.User;

public class RequestChangePW {
    String email;
    String newpass;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getNewpass() {
        return newpass;
    }

    public void setNewpass(String newpass) {
        this.newpass = newpass;
    }

    public RequestChangePW(String email, String newpass) {
        this.email = email;
        this.newpass = newpass;
    }
}
