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
@Document(collection = "private_chat")
public class PrivateChat {
    @Id
    private String id;
    private String creatorId;
    private String recipientId;
    private String lastMessageSent;
}
