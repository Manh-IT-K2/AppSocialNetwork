package com.example.frontend.viewModel.Message;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.frontend.repository.MessageRepository;
import com.example.frontend.response.PrivateChat.PrivateChatWithMessagesResponse;

public class MessageViewModel extends ViewModel {
    private MessageRepository messageRepository;
    public MessageViewModel (){
        messageRepository = new MessageRepository();
    }
    public MutableLiveData<PrivateChatWithMessagesResponse> getListChat(String id){
        return messageRepository.getListChat(id);
    }

}
