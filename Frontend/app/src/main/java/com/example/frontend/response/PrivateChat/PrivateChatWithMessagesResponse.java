package com.example.frontend.response.PrivateChat;

import com.example.frontend.response.Message.MessageWithSenderInfo;
import com.example.frontend.response.User.UserResponse;
import com.pusher.client.channel.User;

import java.util.List;

public class PrivateChatWithMessagesResponse {
    private UserResponse recipient;
    private String lastMessage;
    private List<MessageWithSenderInfo> messages;

    public UserResponse getRecipient() {
        return recipient;
    }

    public void setRecipient(UserResponse recipient) {
        this.recipient = recipient;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public List<MessageWithSenderInfo> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageWithSenderInfo> messages) {
        this.messages = messages;
    }

    public PrivateChatWithMessagesResponse(UserResponse recipient, String lastMessage, List<MessageWithSenderInfo> messages) {
        this.recipient = recipient;
        this.lastMessage = lastMessage;
        this.messages = messages;
    }
}
