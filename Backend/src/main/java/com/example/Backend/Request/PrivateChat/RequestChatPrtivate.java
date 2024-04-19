package com.example.Backend.Request.PrivateChat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestChatPrtivate {
    String creatorId;
    String recipientId;
    Date createdAt;
    String lastMessageSent;
    String urlFile;
    String urlSticker;
}
