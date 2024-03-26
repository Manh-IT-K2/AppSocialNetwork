package com.example.Backend.Controller;

import com.example.Backend.Request.GroupChat.RequestChatGroup;
import com.example.Backend.Request.GroupChat.RequestCreateGroupChat;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import com.example.Backend.Response.ApiResponse.GroupChatResponse.GroupChatResponse;
import com.example.Backend.Response.ApiResponse.GroupChatResponse.GroupChatWithMessagesResponse;
import com.example.Backend.Service.GroupChat.GroupChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/group_chat")
public class GroupChatController {

    @Autowired
    private GroupChatService groupChatService;

    @PostMapping("/create")
    public ApiResponse<GroupChatResponse> createGroupChat(@RequestBody RequestCreateGroupChat request) {
        GroupChatResponse response = groupChatService.createGroupChat(request);
        return new ApiResponse<>(true, "Group chat created successfully", response);
    }

    @GetMapping("/{groupChatId}/messages")
    public ApiResponse<GroupChatWithMessagesResponse> getMessagesByGroupChatId(@PathVariable String groupChatId) throws Exception {
        GroupChatWithMessagesResponse response = groupChatService.getMessagesByGroupChatId(groupChatId);
        return new ApiResponse<>(true, "", response);
    }

    @PostMapping("/{groupChatId}/send_message")
    public ApiResponse<GroupChatWithMessagesResponse> sendMessage(@PathVariable String groupChatId, @RequestBody RequestChatGroup request) throws Exception {
        request.setGroupId(groupChatId);
        GroupChatWithMessagesResponse response = groupChatService.sendMessage(request);
        return new ApiResponse<>(true, "Message sent successfully", response);
    }
}
