package com.example.Backend.Service.GroupChat;

import com.example.Backend.Request.GroupChat.*;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import com.example.Backend.Response.ApiResponse.GroupChatResponse.GroupChatResponse;
import com.example.Backend.Response.ApiResponse.GroupChatResponse.GroupChatWithMessagesResponse;

public interface GroupChatService {
    GroupChatResponse createGroupChat(RequestCreateGroupChat requestCreateGroupChat);
    GroupChatWithMessagesResponse getMessagesByGroupChatId(String id) throws Exception;
    GroupChatWithMessagesResponse sendMessage(RequestChatGroup requestChatGroup) throws Exception;
    ApiResponse<String> addMemberToGroupChat(RequestAddMemberToGroupChat requestGroupMember) throws Exception;
    ApiResponse<String> removeMemberFromGroupChat(RequestRemoveMemberFromGroupChat requestGroupMember) throws Exception;
    ApiResponse<String> renameGroupChat(RequestRenameGroupChat requestGroupChangeName) throws Exception;
    ApiResponse<String> deleteGroupChat(RequestDeleteGroupChat requestDeleteGroupChat) throws Exception;
    void updateLastMessage(String groupChatId, String lastMessage);
}
