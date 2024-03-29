package com.example.Backend.Response.ApiResponse.PrivateChatResponse;

import com.example.Backend.Response.ApiResponse.GroupChatResponse.GroupChatWithMessagesResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatListsResponse {
    private List<PrivateChatWithMessagesResponse> privateChats;
    private List<GroupChatWithMessagesResponse> groupChats;
}
