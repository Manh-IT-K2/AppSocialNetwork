package com.example.Backend.Service.Comment;

import com.example.Backend.Entity.Comment;
import com.example.Backend.Entity.model.User;
import com.example.Backend.Request.Comment.RequestCreateComment;
import com.example.Backend.Request.Comment.RequestDeleteComment;
import com.example.Backend.Request.Comment.RequestLikeComment;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class CommentImpl implements CommentService {
    @Autowired
    private MongoTemplate mongoTemplate;

    // create comment
    @Override
    public Comment createComment(RequestCreateComment addComment) throws Exception {
        Query query = new Query(Criteria.where("_id").is(addComment.getIdUser()));
        User user = mongoTemplate.findOne(query, User.class, "users");

        Comment comment = new Comment();
        comment.setContent(addComment.getContent());
        comment.setUser(user);
        comment.setIdPost(addComment.getIdPost());
        comment.setCreateAt(new Date());

        if(addComment.getIsReplyComment()){ // add reply comment
            query = new Query(Criteria.where("_id").is(addComment.getIdComment()));
            Comment commentQuery = mongoTemplate.findOne(query, Comment.class, "comments");

            if(addComment.getIdUserReply() != null){
                query = new Query(Criteria.where("_id").is(addComment.getIdUserReply()));
                user = mongoTemplate.findOne(query, User.class, "users");
                comment.setUserReply(user);
            }

            if(commentQuery != null){
                if(commentQuery.getReplyComment() == null) {
                    commentQuery.setReplyComment(new ArrayList<>());
                }
                comment.setId(comment.generateId());
                commentQuery.getReplyComment().add(comment);
                return mongoTemplate.save(commentQuery, "comments");
            }
        }
        return mongoTemplate.save(comment, "comments");
    }

    // delete comment
    @Override
    public void deleteComment(RequestDeleteComment deleteComment) throws Exception {
        Query query = new Query(Criteria.where("_id").is(deleteComment.getIdComment()));
        Comment comment = mongoTemplate.findOne(query, Comment.class, "comments");

        if(comment != null){// delete replyComment
            if(deleteComment.getIsReplyComment()){
                comment.getReplyComment().removeIf(c -> c.getId().equals(deleteComment.getIdReplyComment()));
                mongoTemplate.save(comment,"comments");
            }else{//delete comment
                mongoTemplate.remove(comment,"comments");
            }
        }
    }

    // get lít comment by post
    @Override
    public List<Comment> getListCommentByIdPost(String id, String idComment) throws Exception {
        Query query = new Query(Criteria.where("idPost").is(id));
        query.with(Sort.by(Sort.Direction.DESC, "createAt"));

        List<Comment> list = mongoTemplate.find(query, Comment.class, "comments");

        if(!idComment.isEmpty()){
            for (int i = 0; i < list.size(); i++) {
                Comment comment = list.get(i);
                if (comment.getId().equals(idComment)) {
                    list.remove(i); // Xóa sản phẩm khỏi vị trí hiện tại
                    list.add(0, comment); // Thêm sản phẩm vào đầu danh sách
                    break; // Dừng vòng lặp sau khi di chuyển sản phẩm
                }
            }
        }
        return list;
    }

    @Override
    public void likeComment(RequestLikeComment likeComment) throws Exception {
        Query query = new Query(Criteria.where("_id").is(likeComment.getIdUser()));
        User user = mongoTemplate.findOne(query, User.class, "users");

        query = new Query(Criteria.where("_id").is(likeComment.getIdComment()));
        Comment comment = mongoTemplate.findOne(query, Comment.class, "comments");

        boolean userExists = false;
        User userToRemove = null;

        if(likeComment.getIsReplyComment()) { // add like for  replyComment
            int index = -1;
            for (int i = 0; i < Objects.requireNonNull(comment).getReplyComment().size(); i++) {
                if (comment.getReplyComment().get(i).getId().equals(likeComment.getIdReplyComment())) {
                    index = i;
                    break;
                }
            }

            if(index != -1){
                if(comment.getReplyComment().get(index).getLike() == null) comment.getReplyComment().get(index).setLike(new ArrayList<>());
                for (User u : comment.getReplyComment().get(index).getLike()) {
                    if (u.getId().equals(likeComment.getIdUser())) {
                        userExists = true;
                        userToRemove = u;
                        break;
                    }
                }

                if (userExists) {
                    comment.getReplyComment().get(index).getLike().remove(userToRemove);
                } else {
                    comment.getReplyComment().get(index).getLike().add(user);
                }
                mongoTemplate.save(comment);
            }
        }else{
            if(comment.getLike() == null)  comment.setLike(new ArrayList<>());
            for (User u : comment.getLike()) {
                if (u.getId().equals(likeComment.getIdUser())) {
                    userExists = true;
                    userToRemove = u;
                    break;
                }
            }

            if (userExists) {
                comment.getLike().remove(userToRemove);
            } else {
                comment.getLike().add(user);
            }
            mongoTemplate.save(comment);
        }
    }
}
