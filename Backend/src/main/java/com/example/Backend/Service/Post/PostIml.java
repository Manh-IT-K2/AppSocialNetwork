package com.example.Backend.Service.Post;

import com.example.Backend.Entity.Post;
import com.example.Backend.Entity.model.User;
import com.example.Backend.Request.Post.RequestPost;
import com.example.Backend.Request.Post.RequestPostByUserId;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
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
                .andExpression("posts.createAt").as("createAt")
                .andExpression("posts.like").as("like");

        SortOperation sortOperation = Aggregation.sort(Sort .by(Sort.Direction.DESC, "createAt"));

        Aggregation aggregation = Aggregation.newAggregation(lookupOperation, matchOperation, unwindOperation, projectOperation, sortOperation);

        List<RequestPostByUserId> list = mongoTemplate.aggregate(aggregation, "users", RequestPostByUserId.class).getMappedResults();
        return new ApiResponse<List<RequestPostByUserId>>(true, "", list);
    }

    @Override
    // add user like post
    public ApiResponse<Post> addLikeToPost(String postId, String userId) {
        ObjectId postIdOb = new ObjectId(postId);
        ObjectId userIdOb = new ObjectId(userId);
        // Tìm bài đăng theo postId
        Query query = new Query(Criteria.where("_id").is(postIdOb));
        Post post = mongoTemplate.findOne(query, Post.class,"post");

        Query queryUser = new Query(Criteria.where("_id").is(userIdOb));
        User users = mongoTemplate.findOne(queryUser, User.class,"users");


        // Kiểm tra xem bài đăng có tồn tại không
        if (post != null) {
            // Thêm người dùng vào danh sách likes
            if (post.getLike() == null) {
                post.setLike(new ArrayList<>());
            }


            boolean userExists = false;
            User userToRemove = null;

            for (User u : post.getLike()) {
                System.out.println(u.getId());
                System.out.println(userId);
                if (u.getId().equals(userId)) {
                    userExists = true;
                    userToRemove = u;
                    break;
                }
            }
            System.out.println(userExists);
            if (userExists) {
                post.getLike().remove(userToRemove);
                mongoTemplate.save(post);
                System.out.println("Người dùng với ID " + userId + " đã được xóa khỏi danh sách.");
            } else {
                post.getLike().add(users);
                mongoTemplate.save(post);
                System.out.println("Người dùng với ID " + userId + " đã được thêm vào danh sách.");
            }

            // Cập nhật bài đăng trong cơ sở dữ liệu
            return new ApiResponse<Post>(true,"add like success",post);
        } else {
            // Xử lý trường hợp bài đăng không tồn tại
            // (Có thể throw exception hoặc xử lý theo nhu cầu của ứng dụng)
            return new ApiResponse<Post>(true,"add like success",post);
        }
    }
}
