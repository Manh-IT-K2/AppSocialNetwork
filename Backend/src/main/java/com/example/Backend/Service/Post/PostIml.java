package com.example.Backend.Service.Post;

import com.example.Backend.Entity.Post;
import com.example.Backend.Request.Post.RequestPost;
import com.example.Backend.Request.Post.RequestPostByUserId;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import java.util.List;

@Service
public class PostIml implements PostService{

    @Autowired
    private MongoTemplate mongoTemplate;

    public PostIml(){
    }

    // create post
    @Override
    public void createPost(RequestPost requestPost, String userId) throws Exception {
        Post post = new Post();
        ObjectId objectId = new ObjectId(userId);
        post.setUserId(objectId);
        post.setImagePost(requestPost.getImagePost());
        post.setDescription(requestPost.getDescription());

        // Kiểm tra xem trường location có giá trị không
        if (!requestPost.getLocation().isEmpty()) {
            post.setLocation(requestPost.getLocation());
        } else {
            // Xử lý khi location là null
            // Ví dụ, gán một giá trị mặc định cho location hoặc thực hiện hành động khác tùy thuộc vào logic của bạn
            post.setLocation("Unknown Location");
        }

        post.setCreateAt(requestPost.getCreateAt());
        mongoTemplate.insert(post, "post");
    }


    // select post by userId
    @Override
    public ApiResponse<List<RequestPostByUserId>> getListPostsByUserId(String userId) {
        ObjectId objectId = new ObjectId(userId);
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("post")
                .localField("_id")
                .foreignField("userId")
                .as("posts");

        MatchOperation matchOperation = Aggregation.match(Criteria.where("_id").is(objectId));
        AggregationOperation unwindOperation = Aggregation.unwind("posts");
        AggregationOperation projectOperation = Aggregation.project()
                .andExpression("_id").as("userId")
                .andExpression("username").as("userName")
                .andExpression("avatarImg").as("avtImage")
                .andExpression("posts._id").as("idPost")
                .andExpression("posts.imagePost").as("imagePost")
                .andExpression("posts.description").as("description")
                .andExpression("posts.location").as("location")
                .andExpression("posts.createAt").as("createAt");


        Aggregation aggregation = Aggregation.newAggregation(lookupOperation, matchOperation, unwindOperation, projectOperation);

        List<RequestPostByUserId> list = mongoTemplate.aggregate(aggregation, "users", RequestPostByUserId.class).getMappedResults();
        return new ApiResponse<List<RequestPostByUserId>>(true, "", list);
    }
}
