package com.example.Backend.Response.ApiResponse.PrivateChatResponse;

import com.example.Backend.Entity.Message;
import com.example.Backend.Entity.model.Message.MessageWithSenderInfo;
import com.example.Backend.Entity.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrivateChatWithMessagesResponse {
    private User recipient;
    private String lastMessage;
    private Date lastMessageCreatedAt;
    private List<MessageWithSenderInfo> messages;
}
