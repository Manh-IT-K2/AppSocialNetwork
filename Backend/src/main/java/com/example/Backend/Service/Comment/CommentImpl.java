package com.example.Backend.Service.Comment;

import com.example.Backend.Entity.Comment;
import com.example.Backend.Entity.model.User;
import com.example.Backend.Request.Comment.RequestCreateComment;
import com.example.Backend.Request.Comment.RequestDeleteComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public List<Comment> getListCommentByIdPost(String id) throws Exception {
        Query query = new Query(Criteria.where("idPost").is(id));
        return mongoTemplate.find(query, Comment.class, "comments");
    }
}
