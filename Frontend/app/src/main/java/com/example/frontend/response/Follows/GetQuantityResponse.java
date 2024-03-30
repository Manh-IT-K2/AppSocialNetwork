package com.example.frontend.response.Follows;

public class GetQuantityResponse {
    private String id;
    private int quantityFollower;
    private int quantityFollowing;

    public GetQuantityResponse(String id, int quantityFollower, int quantityFollowing) {
        this.id = id;
        this.quantityFollower = quantityFollower;
        this.quantityFollowing = quantityFollowing;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantityFollower() {
        return quantityFollower;
    }

    public void setQuantityFollower(int quantityFollower) {
        this.quantityFollower = quantityFollower;
    }

    public int getQuantityFollowing() {
        return quantityFollowing;
    }

    public void setQuantityFollowing(int quantityFollowing) {
        this.quantityFollowing = quantityFollowing;
    }
}
