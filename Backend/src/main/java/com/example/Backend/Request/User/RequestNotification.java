package com.example.Backend.Request.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestNotification {
    String userId;
    String text;
    String postId;
    boolean isLikePost;
    boolean isComment;
    boolean isReplyComment;
    String idComment;
    boolean isFollow;
    String idRecipient;
}
