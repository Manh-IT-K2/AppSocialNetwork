package com.example.frontend.response.PrivateChat;

import com.example.frontend.response.User.UserResponse;
import com.google.firebase.firestore.auth.User;

public class PrivateChatResponse {
    private String id;
    private UserResponse recipient;
    private UserResponse creator;

    public UserResponse getCreator() {
        return creator;
    }

    public void setCreator(UserResponse creator) {
        this.creator = creator;
    }

    public PrivateChatResponse(String id, UserResponse recipient, UserResponse creator) {
        this.id = id;
        this.recipient = recipient;
        this.creator = creator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserResponse getRecipient() {
        return recipient;
    }

    public void setRecipient(UserResponse recipient) {
        this.recipient = recipient;
    }
}
