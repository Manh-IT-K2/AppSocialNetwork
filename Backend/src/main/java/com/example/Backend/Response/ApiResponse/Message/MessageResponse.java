package com.example.Backend.Response.ApiResponse.Message;

import com.example.Backend.Entity.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private String id;
    private String content;
    private Date createdAt;
    private User sender;
    private String privateChatId;
    private String groupChatId;
    private String urlFile;
    private String urlSticker;
}
