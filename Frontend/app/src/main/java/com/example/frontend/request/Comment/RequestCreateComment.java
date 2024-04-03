package com.example.frontend.request.Comment;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public class RequestCreateComment {
    String idPost;
    String idUser;
    String content;
    Boolean isReplyComment;
    String idComment;
    String idUserReply;

    public RequestCreateComment() {
    }

    public RequestCreateComment(String idPost, String idUser, String content, Boolean isReplyComment, String idComment, String idUserReply) {
        this.idPost = idPost;
        this.idUser = idUser;
        this.content = content;
        this.isReplyComment = isReplyComment;
        this.idComment = idComment;
        this.idUserReply = idUserReply;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getReplyComment() {
        return isReplyComment;
    }

    public void setReplyComment(Boolean replyComment) {
        isReplyComment = replyComment;
    }

    public String getIdComment() {
        return idComment;
    }

    public void setIdComment(String idComment) {
        this.idComment = idComment;
    }

    public String getIdUserReply() {
        return idUserReply;
    }

    public void setIdUserReply(String idUserReply) {
        this.idUserReply = idUserReply;
    }
}
