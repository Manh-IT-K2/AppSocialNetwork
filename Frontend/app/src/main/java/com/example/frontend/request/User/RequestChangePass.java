package com.example.frontend.request.User;

public class RequestChangePass {
    String email;
    String currentpass;
    String newpass;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public RequestChangePass(String email, String currentpass, String newpass) {
        this.email = email;
        this.currentpass = currentpass;
        this.newpass = newpass;
    }
}
