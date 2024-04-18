package com.example.Backend.Service.Post;

import com.example.Backend.Entity.Follows;
import com.example.Backend.Entity.Post;
import com.example.Backend.Entity.Story;
import com.example.Backend.Entity.model.User;
import com.example.Backend.Request.Post.RequestCreatePost;
import com.example.Backend.Request.Post.RequestPostByUserId;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostIml implements PostService{

    @Autowired
    private MongoTemplate mongoTemplate;

    public PostIml(){
    }

    // create post
    @Override
    public void createPost(RequestCreatePost requestPost, String userId) throws Exception {
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

    @Override
    public ApiResponse<List<RequestPostByUserId>> getListPostsByUserId(String userId) {
        ObjectId objectId = new ObjectId(userId);

        // Tìm tất cả các theo dõi của userId truyền vào
        Query followsQuery = new Query(Criteria.where("idFollower").is(objectId));
        List<Follows> followsList = mongoTemplate.find(followsQuery, Follows.class, "follows");
        List<ObjectId> followingIds = new ArrayList<>();
        boolean isUserIdIncluded = false; // Biến để theo dõi xem userId đã được thêm vào danh sách hay không
        for (Follows follows : followsList) {
            ObjectId idFollowing = follows.getIdFollowing();
            followingIds.add(idFollowing);
            if (idFollowing.equals(objectId)) {
                isUserIdIncluded = true; // Đánh dấu userId đã được thêm vào danh sách
            }
        }

        Query q = new Query();
        List<Post> allPosts = mongoTemplate.find(q, Post.class, "post");

        // Tiến hành xác định trạng thái của việc theo dõi
        Query query;
        System.err.println("hihi " + followingIds);
        if (followingIds.isEmpty()) {
            // Nếu không follow ai, load tất cả bài post, bao gồm cả bài post của userId
            for (Post p : allPosts){
                followingIds.add(p.getUserId());
            }
            query = new Query(Criteria.where("userId").in(followingIds));
        } else {
            // Nếu đang follow ai, load bài post của userId và của những người userId đang follow
            if (!isUserIdIncluded) {
                followingIds.add(objectId); // Thêm userId vào danh sách nếu chưa được thêm
            }
            query = new Query(Criteria.where("userId").in(followingIds));
        }

        List<Post> userPosts = mongoTemplate.find(query, Post.class, "post");

        // Tiến hành Aggregation để định dạng lại dữ liệu
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("users")
                .localField("userId")
                .foreignField("_id")
                .as("user");

        MatchOperation matchOperation = Aggregation.match(Criteria.where("userId").in(userPosts.stream().map(Post::getUserId).collect(Collectors.toList())));

        AggregationOperation unwindUserOperation = Aggregation.unwind("user");

        AggregationOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "createAt"); // Sắp xếp theo thứ tự giảm dần của createAt

        AggregationOperation projectOperation = Aggregation.project()
                .andExpression("_id").as("userId")
                .andExpression("user.username").as("userName")
                .andExpression("user.avatarImg").as("avtImage")
                .andExpression("_id").as("idPost")
                .andExpression("imagePost").as("imagePost")
                .andExpression("description").as("description")
                .andExpression("location").as("location")
                .andExpression("createAt").as("createAt")
                .andExpression("like").as("like");

        Aggregation aggregation = Aggregation.newAggregation(
                lookupOperation,
                matchOperation,
                unwindUserOperation,
                sortOperation,
                projectOperation
        );

        List<RequestPostByUserId> list = mongoTemplate.aggregate(aggregation, "post", RequestPostByUserId.class).getMappedResults();
        return new ApiResponse<>(true, "", list);
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
