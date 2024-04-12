package com.example.Backend.Entity;

import com.example.Backend.Entity.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    String id;
    User user;
    String idPost;
    String text;
    boolean isLikePost;
    boolean isComment;
    String idComment;
    boolean isReplyComment;
    Date createAt;
    boolean isFollow;
    String idRecipient;
    public String generateId() {
        // Tạo một id ngẫu nhiên cho đối tượng Comment
        return UUID.randomUUID().toString();
    }
}
