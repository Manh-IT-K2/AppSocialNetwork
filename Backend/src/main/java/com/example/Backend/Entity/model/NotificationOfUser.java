package com.example.Backend.Entity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotificationOfUser {
    String userId;
    String userName;
    String avatar;
    String text;
    String idPost;
    String idComment;
    Date createAt;
}
