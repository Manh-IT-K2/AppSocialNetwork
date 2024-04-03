package com.example.frontend.request.User;

public class RequestChangePW {
    private String NewPass; // Thay đổi lại thành NewPass
    private String email;

    public RequestChangePW(String email, String newPass) {
        this.email = email;
        this.NewPass = newPass; // Thay đổi thành NewPass
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPass() {
        return NewPass; // Thay đổi thành NewPass
    }

    public void setNewPass(String newPass) {
        this.NewPass = newPass; // Thay đổi thành NewPass
    }
}
