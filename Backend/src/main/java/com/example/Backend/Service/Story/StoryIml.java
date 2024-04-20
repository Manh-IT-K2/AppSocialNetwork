package com.example.Backend.Service.Story;

import com.example.Backend.Entity.*;
import com.example.Backend.Entity.model.User;
import com.example.Backend.Request.Story.RequestCreateStory;
import com.example.Backend.Request.Story.RequestStoryByUserId;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class StoryIml implements StoryService{

    @Autowired
    private MongoTemplate mongoTemplate;

    public StoryIml() {
    }
    //create story
    @Override
    public void createStory(RequestCreateStory requestStory, String userId) {
        ObjectId objectId = new ObjectId(userId);
        Story story = new Story();
        story.setUserId(objectId);
        story.setStatus(0);
        story.setImage(requestStory.getImage());
        story.setCreatedAt(requestStory.getCreatedAt());

        if (!requestStory.getContentMedia().isEmpty()) {
            List<Story.ContentMedia> contentMediaList = requestStory.getContentMedia().stream()
                    .map(contentMedia -> new Story.ContentMedia(contentMedia.getContent(), contentMedia.getX(), contentMedia.getY()))
                    .collect(Collectors.toList());
            story.setContentMedia(contentMediaList);
        }

        if (!requestStory.getStickers().isEmpty()) {
            List<Story.Stickers> stickersList = requestStory.getStickers().stream()
                    .map(stickers -> new Story.Stickers(stickers.getUriSticker(), stickers.getX(), stickers.getY()))
                    .collect(Collectors.toList());
            story.setStickers(stickersList);
        }

        mongoTemplate.insert(story,"stories");
    }


    @Override
    public ApiResponse<List<RequestStoryByUserId>> getListStoryByUserId(String userId) {
        ObjectId objectId = new ObjectId(userId);

        // Tìm các story có trạng thái là 0 và userId truyền vào
        Query query = new Query(Criteria.where("userId").is(objectId).and("status").is(0));
        List<Story> userStories = mongoTemplate.find(query, Story.class, "stories");

        // Tìm tất cả các theo dõi của userId truyền vào
        Query followsQuery = new Query(Criteria.where("idFollower").is(objectId));
        List<Follows> followsList = mongoTemplate.find(followsQuery, Follows.class, "follows");
        List<ObjectId> followingIds = followsList.stream().map(Follows::getIdFollowing).collect(Collectors.toList());


        // Tìm các story của những người mà userId truyền vào đang theo dõi có trạng thái là 0
        Query followingQuery = new Query(Criteria.where("userId").in(followingIds).and("status").is(0));
        List<Story> followingStories = mongoTemplate.find(followingQuery, Story.class, "stories");
        userStories.addAll(followingStories);

        // Kiểm tra và cập nhật trạng thái cho các câu chuyện có createdAt lớn hơn 24 giờ
        for (Story story : userStories) {
            if (isStoryOlderThan24Hours(story.getCreatedAt())) {
                // Cập nhật trạng thái của câu chuyện
                Update update = new Update().set("status", 1);
                Query updateQuery = new Query(Criteria.where("_id").is(story.getId()));
                mongoTemplate.updateFirst(updateQuery, update, Story.class, "stories");
            }
        }

        // Tiến hành Aggregation để định dạng lại dữ liệu
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("stories")
                .localField("_id")
                .foreignField("userId")
                .as("listStory");

        MatchOperation matchOperation = Aggregation.match(Criteria.where("_id").in(userStories.stream().map(Story::getUserId).collect(Collectors.toList())));

        AggregationOperation unwindOperation = Aggregation.unwind("listStory");

        AggregationOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "createdAt");

        AggregationOperation projectOperation = Aggregation.project()
                .andExpression("_id").as("userId")
                .andExpression("username").as("userName")
                .andExpression("avatarImg").as("avtUser")
                .andExpression("listStory._id").as("idStory")
                .andExpression("listStory.contentMedia").as("contentMedia")
                .andExpression("listStory.image").as("image")
                .andExpression("listStory.stickers").as("stickers")
                .andExpression("listStory.createdAt").as("createdAt")
                .andExpression("listStory.status").as("status")
                .andExpression("listStory.seen").as("seen");

        Aggregation aggregation = Aggregation.newAggregation(lookupOperation, matchOperation, unwindOperation, projectOperation, sortOperation);

        List<RequestStoryByUserId> list = mongoTemplate.aggregate(aggregation, "users", RequestStoryByUserId.class).getMappedResults();
        return new ApiResponse<>(true, "", list);
    }

    // Phương thức để kiểm tra xem một câu chuyện có được tạo hơn 24 giờ không
    private boolean isStoryOlderThan24Hours(Date createdAt) {
        // Lấy thời điểm hiện tại
        Date currentTime = new Date();
        // Tính toán thời gian chênh lệch giữa thời điểm hiện tại và thời điểm câu chuyện được tạo
        long timeDifferenceInMillis = currentTime.getTime() - createdAt.getTime();
        // Chuyển đổi thời gian từ milliseconds sang giờ
        long hoursDifference = TimeUnit.MILLISECONDS.toHours(timeDifferenceInMillis);
        // Kiểm tra nếu thời gian chênh lệch lớn hơn 24 giờ
        return hoursDifference > 24;
    }

    // delete story
    @Override
    public void deleteStoryById(String idStory) {
        Query query = new Query(Criteria.where("_id").is(idStory));
        Story story = mongoTemplate.findOne(query, Story.class, "stories");
        if (story == null) {
            System.out.println("Không tìm thấy story với ID: " + idStory);
            return;
        }
        mongoTemplate.remove(story, "stories");
        System.out.println("Story với ID " + idStory + " đã được xóa thành công.");
    }

    // add viewer
    @Override
    public void addViewedStory(String storyId, String userId) {
        ObjectId storyIdOb = new ObjectId(storyId);
        ObjectId userIdOb = new ObjectId(userId);
        Query query = new Query(Criteria.where("_id").is(storyIdOb));
        Story story = mongoTemplate.findOne(query, Story.class,"stories");

        Query queryUser = new Query(Criteria.where("_id").is(userIdOb));
        User user = mongoTemplate.findOne(queryUser, User.class,"users");

        if (story != null) {
            if (story.getSeen() == null) {
                story.setSeen(new ArrayList<>());
            }

            boolean userExists = false;

            for (User u : story.getSeen()) {
                if (u.getId().equals(userId)) {
                    userExists = true;
                    break;
                }
            }

            // Kiểm tra xem story có phải của người dùng hiện tại không
            boolean isOwnStory = story.getUserId().equals(userIdOb);

            if (!userExists && !isOwnStory) {
                story.getSeen().add(user);
                mongoTemplate.save(story);
                System.out.println("Người dùng với ID " + userId + " đã xem tin của bạn.");
            } else if (isOwnStory) {
                System.out.println("Đây là story của bạn!");
            } else {
                System.out.println("Người dùng với ID " + userId + " đã xem story này trước đó.");
            }
        }
    }

}
