package com.example.frontend.request.Follows;

public class RequestCreateFollows {
    String idFollower;
    String idFollowing;
    String created_at;

    public RequestCreateFollows(String idFollower, String idFollowing, String created_at) {
        this.idFollower = idFollower;
        this.idFollowing = idFollowing;
        this.created_at = created_at;
    }

    public String getIdFollower() {
        return idFollower;
    }

    public void setIdFollower(String idFollower) {
        this.idFollower = idFollower;
    }

    public String getIdFollowing() {
        return idFollowing;
    }

    public void setIdFollowing(String idFollowing) {
        this.idFollowing = idFollowing;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
