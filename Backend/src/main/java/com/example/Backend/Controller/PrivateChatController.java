package com.example.Backend.Controller;

import com.example.Backend.Config.PusherConfig;
import com.example.Backend.Request.PrivateChat.RequestChatPrtivate;
import com.example.Backend.Request.PrivateChat.RequestCreatePrivateChat;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import com.example.Backend.Response.ApiResponse.GroupChatResponse.GroupChatWithMessagesResponse;
import com.example.Backend.Response.ApiResponse.PrivateChatResponse.PrivateChatResponse;
import com.example.Backend.Response.ApiResponse.PrivateChatResponse.PrivateChatWithMessagesResponse;
import com.example.Backend.Service.PrivateChat.PrivateChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        pusherConfig.triggerEvent("newmess", "send", response);
        return new ApiResponse<>(true, "OK", response);
    }
    @GetMapping("/get_list_mess")
    public ApiResponse<List<PrivateChatWithMessagesResponse>> getListChat(@RequestParam String id) {
        List<PrivateChatWithMessagesResponse> list = privateChatService.getListChat(id);
        return new ApiResponse<>(true, "OK", list);
    }


}
