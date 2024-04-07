package com.example.frontend.request.User;

public class RequestChangePass {
    String id;
    String currentpass;
    String newpass;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public RequestChangePass(String id, String currentpass, String newpass) {
        this.id = id;
        this.currentpass = currentpass;
        this.newpass = newpass;
    }
}
