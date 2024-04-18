package com.example.Backend.Request.Comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestLikeComment {
    String idComment;
    String idUser;
    Boolean isReplyComment;
    String idReplyComment;
}
