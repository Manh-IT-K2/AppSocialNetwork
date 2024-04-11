package com.example.Backend.Service.Post;

import com.example.Backend.Entity.Follows;
import com.example.Backend.Entity.Post;
import com.example.Backend.Entity.model.User;
import com.example.Backend.Request.Post.RequestCreatePost;
import com.example.Backend.Request.Post.RequestPostByUserId;
import com.example.Backend.Response.ApiResponse.ApiResponse;
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
                .andExpression("tokenFCM").as("tokenFCM")
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

    public List<String> getFollowedUserIds(ObjectId currentUserId) {
        Criteria criteria = Criteria.where("idFollower").is(currentUserId).and("status").is(1); // Lọc ra các người dùng đã theo dõi
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.project().and("idFollowing").as("idFollowing") // Chọn ra trường idFollowing
        );
        AggregationResults<Follows> results = mongoTemplate.aggregate(aggregation, "follows", Follows.class);
        List<String> followedUserIds = new ArrayList<>();
        for (Follows follows : results.getMappedResults()) {
            followedUserIds.add(String.valueOf(follows.getIdFollowing()));
        }
        return followedUserIds;
    }


    @Override
    public ApiResponse<List<RequestPostByUserId>> getListPostsBySearchQuery(String id, String searchQuery) {

        // Lay danh sach following cua id
        List<String> listFollowedUserIds = new ArrayList<>();
        ObjectId objectId = new ObjectId(id);
        listFollowedUserIds = getFollowedUserIds(objectId);

        // Match để lọc các bài đăng theo điều kiện userId nam trong listFollowedUserIds va description chua searchQuery
        Aggregation aggregation1 = Aggregation.newAggregation(
                // Match để lọc các bài đăng theo điều kiện
                Aggregation.match(
                        Criteria.where("userId").in(listFollowedUserIds).and("description").regex(searchQuery)
                ),
                // Lookup để tham chiếu tới bộ sưu tập "users" và lấy thông tin avatar
                LookupOperation.newLookup()
                        .from("users")
                        .localField("userId")
                        .foreignField("_id")
                        .as("user"),
                // Unwind để giải nén kết quả từ Lookup
                Aggregation.unwind("user"),
                // Project để chọn ra trường avatar từ kết quả của Lookup
                Aggregation.project("idPost", "userId", "description", "imagePost", "location", "createAt", "like")
                        .and("user.avatarImg").as("avtImage")
                        .and("user.username").as("userName"),
                // Sort để sắp xếp danh sách kết quả
                Aggregation.sort(Sort.Direction.DESC, "createAt")
        );

        // Thực hiện truy vấn bằng Aggregation và lấy ra kết quả
        AggregationResults<RequestPostByUserId> result1 = mongoTemplate.aggregate(aggregation1, "post", RequestPostByUserId.class);

        // Lấy danh sách kết quả
        List<RequestPostByUserId> resultList1 = result1.getMappedResults();

        //--------------------------------------------------------------------------------------------------------

        // Match để lọc các bài đăng theo điều kiện userId khong nam trong listFollowedUserIds va description chua searchQuery
        Aggregation aggregation2 = Aggregation.newAggregation(
                // Match để lọc các bài đăng theo điều kiện
                Aggregation.match(
                        Criteria.where("userId").nin(listFollowedUserIds).and("description").regex(searchQuery)
                ),
                // Lookup để tham chiếu tới bộ sưu tập "users" và lấy thông tin avatar
                LookupOperation.newLookup()
                        .from("users")
                        .localField("userId")
                        .foreignField("_id")
                        .as("user"),
                // Unwind để giải nén kết quả từ Lookup
                Aggregation.unwind("user"),
                // Project để chọn ra trường avatar từ kết quả của Lookup
                Aggregation.project("idPost", "userId", "description", "imagePost", "location", "createAt", "like")
                        .and("user.avatarImg").as("avtImage")
                        .and("user.username").as("userName"),
                // Sort để sắp xếp danh sách kết quả
                Aggregation.sort(Sort.Direction.DESC, "createAt")
        );

        // Thực hiện truy vấn bằng Aggregation và lấy ra kết quả
        AggregationResults<RequestPostByUserId> result2 = mongoTemplate.aggregate(aggregation2, "post", RequestPostByUserId.class);

        // Lấy danh sách kết quả
        List<RequestPostByUserId> resultList2 = result2.getMappedResults();

        // Ket hop 2 List
        List<RequestPostByUserId> resultList = new ArrayList<>(resultList2);
        resultList.addAll(0, resultList1);

        // Trả về danh sách các tài liệu kết quả
        return new ApiResponse<List<RequestPostByUserId>>(true, "Lấy posts bằng chuỗi truy vấn tìm kiếm", resultList);
    }
}
