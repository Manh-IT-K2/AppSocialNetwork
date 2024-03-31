package com.example.Backend.Service.PrivateChat;

import com.example.Backend.Request.PrivateChat.RequestChatPrtivate;
import com.example.Backend.Request.PrivateChat.RequestCreatePrivateChat;
import com.example.Backend.Response.ApiResponse.PrivateChatResponse.PrivateChatResponse;
import com.example.Backend.Response.ApiResponse.PrivateChatResponse.PrivateChatWithMessagesResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PrivateChatService {
    PrivateChatResponse createPrivateChat(RequestCreatePrivateChat requestCreatePrivateChat);
    PrivateChatWithMessagesResponse getMessagesByPrivateChatId(String id) throws Exception;
    PrivateChatWithMessagesResponse SendMessage(RequestChatPrtivate requestChatPrtivate) throws Exception;
    List<PrivateChatWithMessagesResponse> getListChat(String id);
    PrivateChatWithMessagesResponse getMessagesByPrivate(String creatorId,String recipientId) throws Exception;


}
