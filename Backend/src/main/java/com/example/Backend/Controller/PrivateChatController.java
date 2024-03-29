package com.example.Backend.Controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.example.Backend.Config.PusherConfig;
import com.example.Backend.Request.PrivateChat.RequestChatPrtivate;
import com.example.Backend.Request.PrivateChat.RequestCreatePrivateChat;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import com.example.Backend.Response.ApiResponse.GroupChatResponse.GroupChatWithMessagesResponse;
import com.example.Backend.Response.ApiResponse.PrivateChatResponse.ChatListsResponse;
import com.example.Backend.Response.ApiResponse.PrivateChatResponse.PrivateChatResponse;
import com.example.Backend.Response.ApiResponse.PrivateChatResponse.PrivateChatWithMessagesResponse;
import com.example.Backend.Service.PrivateChat.PrivateChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/private_chat")
public class PrivateChatController {
    @Autowired
    PrivateChatService privateChatService;
    PusherConfig pusherConfig;
    @Autowired
    public PrivateChatController(PusherConfig pusherService) {
        this.pusherConfig = pusherService;
    }

    @PostMapping()
    public ApiResponse<PrivateChatResponse> createPrivateChat(@RequestBody RequestCreatePrivateChat requestCreatePrivateChat) throws Exception{
        pusherConfig.triggerEvent("privateChat", "getMessage", privateChatService.createPrivateChat(requestCreatePrivateChat));
        return new ApiResponse<>(true, "OK", privateChatService.createPrivateChat(requestCreatePrivateChat));

    }
    @GetMapping()
    public ApiResponse<PrivateChatWithMessagesResponse> getMessagesByPrivateChatId(@RequestParam String id) throws Exception{
        return new ApiResponse<PrivateChatWithMessagesResponse>(true, "", privateChatService.getMessagesByPrivateChatId(id));
    }
    @PostMapping("/send_mess")
    public ApiResponse<PrivateChatWithMessagesResponse> sendMessage(@RequestBody RequestChatPrtivate request) throws Exception {
        PrivateChatWithMessagesResponse response = privateChatService.SendMessage(request);
        pusherConfig.triggerEvent("privateChat", "getMessage", response);
        return new ApiResponse<>(true, "OK", response);
    }
    @GetMapping("/get_list_mess")
    public ApiResponse<ChatListsResponse> getListChat(@RequestParam String id) {
        List<PrivateChatWithMessagesResponse> privateChats = privateChatService.getListChat(id);
        List<GroupChatWithMessagesResponse> groupChats = privateChatService.getListChatGroup(id);

        ChatListsResponse chatListsResponse = new ChatListsResponse(privateChats, groupChats);

        return new ApiResponse<>(true, "OK", chatListsResponse);
    }



}
