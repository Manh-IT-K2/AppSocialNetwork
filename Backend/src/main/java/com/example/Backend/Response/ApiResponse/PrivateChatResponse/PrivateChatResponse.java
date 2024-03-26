package com.example.Backend.Response.ApiResponse.PrivateChatResponse;

import com.example.Backend.Entity.Message;
import com.example.Backend.Entity.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrivateChatResponse{
    private String id;
    private User recipient;
    private User creator;
}
