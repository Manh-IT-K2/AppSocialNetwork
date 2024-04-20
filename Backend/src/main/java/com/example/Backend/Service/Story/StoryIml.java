package com.example.Backend.Service.Story;

import com.example.Backend.Entity.Comment;
import com.example.Backend.Entity.Follows;
import com.example.Backend.Entity.Story;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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


    // get list story by userId
    @Override
    public ApiResponse<List<RequestStoryByUserId>> getListStoryByUserId(String userId) {
        ObjectId objectId = new ObjectId(userId);

        // Tìm các story của userId truyền vào
        Query query = new Query(Criteria.where("userId").is(objectId));
        List<Story> userStories = mongoTemplate.find(query, Story.class, "stories");

        // Tìm tất cả các theo dõi của userId truyền vào
        Query followsQuery = new Query(Criteria.where("idFollower").is(objectId));
        List<Follows> followsList = mongoTemplate.find(followsQuery, Follows.class, "follows");
        List<ObjectId> followingIds = new ArrayList<>();
        for (Follows follows : followsList) {
            followingIds.add(follows.getIdFollowing());
        }

        // Tìm các story của những người mà userId truyền vào đang theo dõi
        Query followingQuery = new Query(Criteria.where("userId").in(followingIds));
        List<Story> followingStories = mongoTemplate.find(followingQuery, Story.class, "stories");
        userStories.addAll(followingStories);

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
                .andExpression("listStory.createdAt").as("createdAt");

        Aggregation aggregation = Aggregation.newAggregation(lookupOperation, matchOperation, unwindOperation, projectOperation, sortOperation);

        List<RequestStoryByUserId> list = mongoTemplate.aggregate(aggregation, "users", RequestStoryByUserId.class).getMappedResults();
        return new ApiResponse<List<RequestStoryByUserId>>(true, "", list);
    }

}
