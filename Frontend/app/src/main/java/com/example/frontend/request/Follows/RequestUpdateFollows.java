package com.example.frontend.request.Follows;

public class RequestUpdateFollows {
    String id;
    int follower;
    int following;

    public RequestUpdateFollows(String id, int follower, int following) {
        this.id = id;
        this.follower = follower;
        this.following = following;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }
}
