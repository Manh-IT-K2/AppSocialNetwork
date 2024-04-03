package com.example.frontend.request.Comment;

public class RequestDeleteComment {
    String idPost;
    String idComment;
    Boolean isReplyComment;
    String idReplyComment;

    public RequestDeleteComment() {
    }

    public RequestDeleteComment(String idPost, String idComment, Boolean isReplyComment, String idReplyComment) {
        this.idPost = idPost;
        this.idComment = idComment;
        this.isReplyComment = isReplyComment;
        this.idReplyComment = idReplyComment;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public String getIdComment() {
        return idComment;
    }

    public void setIdComment(String idComment) {
        this.idComment = idComment;
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
