package com.example.Backend.Service.GroupChat;

import com.example.Backend.Request.GroupChat.RequestChatGroup;
import com.example.Backend.Request.GroupChat.RequestCreateGroupChat;
import com.example.Backend.Response.ApiResponse.GroupChatResponse.GroupChatResponse;
import com.example.Backend.Response.ApiResponse.GroupChatResponse.GroupChatWithMessagesResponse;

public interface GroupChatService {
    GroupChatResponse createGroupChat(RequestCreateGroupChat requestCreateGroupChat);
    GroupChatWithMessagesResponse getMessagesByGroupChatId(String id) throws Exception;
    GroupChatWithMessagesResponse sendMessage(RequestChatGroup requestChatGroup) throws Exception;
}
