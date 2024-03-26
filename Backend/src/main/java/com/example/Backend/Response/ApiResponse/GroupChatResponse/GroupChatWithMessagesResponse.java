package com.example.Backend.Response.ApiResponse.GroupChatResponse;

import com.example.Backend.Entity.model.Message.MessageWithSenderInfo;
import com.example.Backend.Entity.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupChatWithMessagesResponse {
    private String id;
    private String groupName;
    private List<User> members;
    private List<MessageWithSenderInfo> messages;
}
