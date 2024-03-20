package com.example.frontend.request.User;

public class RequestTrackingUser {
    String idUser;
    String idNameUser;
    String nameUser;
    String status;

    String imageUser;

    public RequestTrackingUser(String idUser, String idNameUser, String nameUser, String status,String imageUser) {
        this.idUser = idUser;
        this.idNameUser = idNameUser;
        this.nameUser = nameUser;
        this.status = status;
        this.imageUser = imageUser;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdNameUser() {
        return idNameUser;
    }

    public void setIdNameUser(String idNameUser) {
        this.idNameUser = idNameUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUser() {
        return imageUser;
    }

    public void setImageUser(String imageUser) {
        this.imageUser = imageUser;
    }
}
