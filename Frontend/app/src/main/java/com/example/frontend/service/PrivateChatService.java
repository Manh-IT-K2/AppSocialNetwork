package com.example.frontend.service;

import com.example.frontend.request.PrivateChat.RequestPrivateChat;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.PrivateChat.PrivateChatWithMessagesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PrivateChatService {
    @GET("private_chat/get_list_mess")
    Call<ApiResponse<List<PrivateChatWithMessagesResponse>>> getListChat(@Query("id") String id);

    @POST("private_chat/send_mess")
    Call<ApiResponse<PrivateChatWithMessagesResponse>>SendMessage(@Body RequestPrivateChat request);

    @GET("private_chat/get_mess_private")
    Call<ApiResponse<PrivateChatWithMessagesResponse>>getMessagesByPrivate(@Query("creatorId") String creatorId,@Query("recipientId") String recipientId);
}