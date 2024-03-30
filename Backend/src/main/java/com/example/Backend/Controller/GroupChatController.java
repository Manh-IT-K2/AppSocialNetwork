package com.example.Backend.Controller;

import com.example.Backend.Request.GroupChat.*;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import com.example.Backend.Response.ApiResponse.GroupChatResponse.GroupChatResponse;
import com.example.Backend.Response.ApiResponse.GroupChatResponse.GroupChatWithMessagesResponse;
import com.example.Backend.Service.GroupChat.GroupChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/{groupChatId}/add_member")
    public ResponseEntity<ApiResponse<String>> addMemberToGroupChat(@PathVariable String groupChatId, @RequestBody RequestAddMemberToGroupChat request) throws Exception {
        request.setGroupId(groupChatId);
        ApiResponse<String> response = groupChatService.addMemberToGroupChat(request);
        return new ResponseEntity<ApiResponse<String>>(response, HttpStatus.OK);
    }

    @PostMapping("/{groupChatId}/remove_member")
    public ResponseEntity<ApiResponse<String>> removeMemberFromGroupChat(@PathVariable String groupChatId, @RequestBody RequestRemoveMemberFromGroupChat request) throws Exception {
        request.setGroupId(groupChatId);
        ApiResponse<String> response = groupChatService.removeMemberFromGroupChat(request);
        return new ResponseEntity<ApiResponse<String>>(response, HttpStatus.OK);
    }

    @PostMapping("/{groupChatId}/rename")
    public ResponseEntity<ApiResponse<String>> renameGroupChat(@PathVariable String groupChatId, @RequestBody RequestRenameGroupChat request) throws Exception {
        request.setGroupId(groupChatId);
        ApiResponse<String> response = groupChatService.renameGroupChat(request);
        return new ResponseEntity<ApiResponse<String>>(response, HttpStatus.OK);
    }
    @DeleteMapping("/{groupChatId}/delete")
    public ResponseEntity<ApiResponse<String>> deleteGroupChat(@PathVariable String groupChatId) throws Exception {
        RequestDeleteGroupChat request = new RequestDeleteGroupChat();
        request.setGroupId(groupChatId);
        ApiResponse<String> response = groupChatService.deleteGroupChat(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/{groupChatId}/update_last_message")
    public ResponseEntity<ApiResponse<String>> updateLastMessage(@PathVariable String groupChatId, @RequestBody String lastMessage) {
        groupChatService.updateLastMessage(groupChatId, lastMessage);
        return new ResponseEntity<>(new ApiResponse<>(true, "Last message updated successfully", null), HttpStatus.OK);
    }


}
