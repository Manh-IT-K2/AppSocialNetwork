package com.example.Backend.Entity;

import com.example.Backend.Entity.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "messages")
public class Message {
    @Id
    private String id;
    private String content;
    private Date createdAt;
    private String senderId;
    private String privateChatId;
    private String groupChatId;
    private String urlFile;
    private String urlSticker;
}
