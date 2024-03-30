package com.example.frontend.viewModel.Message;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.frontend.repository.MessageRepository;
import com.example.frontend.request.PrivateChat.RequestPrivateChat;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.PrivateChat.PrivateChatWithMessagesResponse;

import java.util.List;

public class MessageViewModel extends ViewModel {
    private MessageRepository messageRepository;
    public MessageViewModel (){
        messageRepository = new MessageRepository();
    }
    public MutableLiveData<List<PrivateChatWithMessagesResponse>> getListChat(String id){
        return messageRepository.getListChat(id);
    }

    public MutableLiveData<ApiResponse<PrivateChatWithMessagesResponse>> sendMessage(RequestPrivateChat requestPrivateChat){
        return messageRepository.SendMessage(requestPrivateChat);
    }


}
