package com.example.frontend.request.Comment;

public class RequestLikeComment {
    String idComment;
    String idUser;
    Boolean isReplyComment;
    String idReplyComment;

    public RequestLikeComment(){

    }

    public RequestLikeComment(String idComment, String idUser, Boolean isReplyComment, String idReplyComment) {
        this.idComment = idComment;
        this.idUser = idUser;
        this.isReplyComment = isReplyComment;
        this.idReplyComment = idReplyComment;
    }

    public String getIdComment() {
        return idComment;
    }

    public void setIdComment(String idComment) {
        this.idComment = idComment;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public Boolean getReplyComment() {
        return isReplyComment;
    }

    public void setReplyComment(Boolean replyComment) {
        isReplyComment = replyComment;
    }

    public String getIdReplyComment() {
        return idReplyComment;
    }

    public void setIdReplyComment(String idReplyComment) {
        this.idReplyComment = idReplyComment;
    }
}
