package com.example.Backend.Controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.example.Backend.Request.PrivateChat.RequestCreatePrivateChat;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import com.example.Backend.Response.ApiResponse.PrivateChatResponse.PrivateChatResponse;
import com.example.Backend.Response.ApiResponse.PrivateChatResponse.PrivateChatWithMessagesResponse;
import com.example.Backend.Service.PrivateChat.PrivateChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/private_chat")
public class PrivateChatController {
    @Autowired
    PrivateChatService privateChatService;
    @Autowired
    private final SocketIOServer socketIOServer;
    @Autowired
    public PrivateChatController(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
    }

    @PostMapping()
    public ApiResponse<PrivateChatResponse> createPrivateChat(@RequestBody RequestCreatePrivateChat requestCreatePrivateChat) throws Exception{
        socketIOServer.getBroadcastOperations().sendEvent("createPrivateChat", requestCreatePrivateChat);
        return new ApiResponse<>(true, "OK", privateChatService.createPrivateChat(requestCreatePrivateChat));
    }

    @OnEvent(value = "sendMessage")
    public void onMessage(SocketIOClient client, String message) {
        System.out.println("Received message from client: " + message);

        // Xử lý tin nhắn từ client ở đây
    }

    @GetMapping()
    public ApiResponse<PrivateChatWithMessagesResponse> getMessagesByPrivateChatId(@RequestParam String id){
        return new ApiResponse<PrivateChatWithMessagesResponse>(true, "", privateChatService.getMessagesByPrivateChatId(id));
    }

}
