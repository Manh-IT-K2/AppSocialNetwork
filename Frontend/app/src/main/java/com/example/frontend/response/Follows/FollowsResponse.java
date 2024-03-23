package com.example.frontend.response.Follows;

import java.util.Date;

public class FollowsResponse {
    private String idFollows;

    private String idFollower;

    private String idFollowing;

    private Date created_at;

    private Date update_at;

    private Date delete_at;

    private int status;

    public FollowsResponse(String idFollows, String idFollower, String idFollowing, Date created_at, Date update_at, Date delete_at, int status) {
        this.idFollows = idFollows;
        this.idFollower = idFollower;
        this.idFollowing = idFollowing;
        this.created_at = created_at;
        this.update_at = update_at;
        this.delete_at = delete_at;
        this.status = status;
    }

    public String getIdFollows() {
        return idFollows;
    }

    public void setIdFollows(String idFollows) {
        this.idFollows = idFollows;
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

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(Date update_at) {
        this.update_at = update_at;
    }

    public Date getDelete_at() {
        return delete_at;
    }

    public void setDelete_at(Date delete_at) {
        this.delete_at = delete_at;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
