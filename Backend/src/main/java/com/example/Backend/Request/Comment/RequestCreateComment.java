package com.example.Backend.Request.Comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestCreateComment {
    String idPost;
    String idUser;
    String content;
    Boolean isReplyComment;
    String idComment;
    String idUserReply;
}
