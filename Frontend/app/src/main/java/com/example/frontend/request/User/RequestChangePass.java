package com.example.frontend.request.User;

public class RequestChangePass {
    String username;
    String currentpass;
    String newpass;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCurrentpass() {
        return currentpass;
    }

    public void setCurrentpass(String currentpass) {
        this.currentpass = currentpass;
    }

    public String getNewpass() {
        return newpass;
    }

    public void setNewpass(String newpass) {
        this.newpass = newpass;
    }

    public RequestChangePass(String username, String currentpass, String newpass) {
        this.username = username;
        this.currentpass = currentpass;
        this.newpass = newpass;
    }
}
