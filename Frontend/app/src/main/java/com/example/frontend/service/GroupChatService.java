package com.example.frontend.service;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.GroupChat.GroupChatResponse;
import com.example.frontend.response.GroupChat.GroupChatWithMessagesResponse;
import com.example.frontend.request.GroupChat.*;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
public interface GroupChatService {
    @POST("/api/group_chat/create")
    Call<ApiResponse<GroupChatResponse>> createGroupChat(@Body RequestCreateGroupChat request);

    @GET("/api/group_chat/{groupChatId}/messages")
    Call<ApiResponse<GroupChatWithMessagesResponse>> getMessagesByGroupChatId(@Path("groupChatId") String groupChatId);

    @POST("/api/group_chat/{groupChatId}/send_message")
    Call<ApiResponse<GroupChatWithMessagesResponse>> sendMessage(@Path("groupChatId") String groupChatId, @Body RequestChatGroup request);

    @POST("/api/group_chat/{groupChatId}/add_member")
    Call<ApiResponse<String>> addMemberToGroupChat(@Path("groupChatId") String groupChatId, @Body RequestAddMemberToGroupChat request);

    @POST("/api/group_chat/{groupChatId}/remove_member")
    Call<ApiResponse<String>> removeMemberFromGroupChat(@Path("groupChatId") String groupChatId, @Body RequestRemoveMemberFromGroupChat request);

    @POST("/api/group_chat/{groupChatId}/rename")
    Call<ApiResponse<String>> renameGroupChat(@Path("groupChatId") String groupChatId, @Body RequestRenameGroupChat request);

    @DELETE("/api/group_chat/{groupChatId}/delete")
    Call<ApiResponse<String>> deleteGroupChat(@Path("groupChatId") String groupChatId);

}
