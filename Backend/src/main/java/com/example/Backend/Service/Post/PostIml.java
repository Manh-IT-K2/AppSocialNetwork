package com.example.Backend.Service.Post;

import com.example.Backend.Entity.Follows;
import com.example.Backend.Entity.Post;
import com.example.Backend.Entity.Story;
import com.example.Backend.Entity.model.User;
import com.example.Backend.Request.Post.RequestCreatePost;
import com.example.Backend.Request.Post.RequestPostByUserId;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import com.example.Backend.Response.ApiResponse.Post.ResponsePostById;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.query.Criteria;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

@Service
public class PostIml implements PostService{

    @Autowired
    private MongoTemplate mongoTemplate;

    public PostIml(){
    }

    // create post
    @Override
    public String createPost(RequestCreatePost requestPost, String userId) throws Exception {
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
        return mongoTemplate.insert(post, "post").getId();
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

    private List<String> getFollowedUserIds(ObjectId currentUserId) {
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

        // Loại bỏ dấu và chuyển đổi sang chữ thường
        String normalizedSearchQuery = Normalizer.normalize(searchQuery.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();

        // Tạo biểu thức chính quy không phân biệt hoa thường và không có dấu
        Pattern pattern = Pattern.compile(normalizedSearchQuery, Pattern.CASE_INSENSITIVE);

        // Match để lọc các bài đăng theo điều kiện userId nam trong listFollowedUserIds va description chua searchQuery
        Aggregation aggregation1 = Aggregation.newAggregation(
                // Match để lọc các bài đăng theo điều kiện
                Aggregation.match(
                        Criteria.where("description").regex(pattern)
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
                Aggregation.sort(Sort .by(Sort.Direction.DESC, "createAt"))
        );

        // Thực hiện truy vấn bằng Aggregation và lấy ra kết quả
        AggregationResults<RequestPostByUserId> result1 = mongoTemplate.aggregate(aggregation1, "post", RequestPostByUserId.class);

        // Lấy danh sách kết quả 1
        List<RequestPostByUserId> resultList1 = result1.getMappedResults();

        // Danh sách kết quả cuối cùng
        List<RequestPostByUserId> resultList = new ArrayList<>(resultList1);

        // Danh sách userId đã kiểm tra
        List<RequestPostByUserId> resultList2 = new ArrayList<>();

        // Lặp qua từng kết quả trong resultList1
        for (RequestPostByUserId post : resultList1) {
            // Lấy userId từ mỗi bài đăng
            String userId = post.getUserId();
            // Kiểm tra xem userId có trong listFollowedUserIds không
            if (listFollowedUserIds.contains(userId)) {
                // Nếu có, thêm userId vào danh sách matchedUserIds
                resultList2.add(post);
                resultList.remove(post);
            }
        }

        // Ket hop 2 List
        resultList.addAll(0, resultList2);

        // Trả về danh sách các tài liệu kết quả
        return new ApiResponse<List<RequestPostByUserId>>(true, "Lấy posts bằng chuỗi truy vấn tìm kiếm", resultList);
    }

    @Override
    public ApiResponse<ResponsePostById> getPostById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        Post post = mongoTemplate.findOne(query, Post.class,"post");

        if(post != null){
            Query queryUser = new Query(Criteria.where("_id").is(post.getUserId()));
            User user = mongoTemplate.findOne(queryUser, User.class,"users");
            ResponsePostById postById = new ResponsePostById();
            postById.setPost(post);
            postById.setUser(user);
            return new ApiResponse<ResponsePostById>(true, "", postById);
        }
        return null;
    }

    @Override
    public ApiResponse<List<RequestPostByUserId>> getListPostUserLiked(String userId) {
        ObjectId objectId = new ObjectId(userId);
        // Tạo một truy vấn để tìm các bài post mà người dùng đã like
        Query likeQuery = new Query(Criteria.where("like.id").is(objectId));
        List<Post> likedPosts = mongoTemplate.find(likeQuery, Post.class, "post");

        // Lấy ra danh sách userId từ các bài post đã like
        List<String> userIds = likedPosts.stream()
                .map(post -> post.getUserId().toString()) // Chuyển đổi ObjectId thành String
                .collect(Collectors.toList());
        for (String id : userIds) {
            System.err.println(id);
        }

        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("post")
                .localField("_id")
                .foreignField("userId")
                .as("posts");

        MatchOperation matchOperation = Aggregation.match(Criteria.where("_id").in(userIds));
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

        List<RequestPostByUserId> resultList = mongoTemplate.aggregate(aggregation, "users", RequestPostByUserId.class).getMappedResults();

        // Trả về ApiResponse tương ứng với kết quả truy vấn
        return !likedPosts.isEmpty() ?
                new ApiResponse<>(true, "Success", resultList) :
                new ApiResponse<>(false, "No data", null);
    }

    @Override
    public ApiResponse<List<RequestPostByUserId>> getListPostsProfile(String userId) {
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
}
