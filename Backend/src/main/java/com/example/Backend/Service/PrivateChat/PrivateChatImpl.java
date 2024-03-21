package com.example.Backend.Service.PrivateChat;

import com.example.Backend.Entity.Message;
import com.example.Backend.Entity.PrivateChat;
import com.example.Backend.Entity.model.User;
import com.example.Backend.Request.PrivateChat.RequestCreatePrivateChat;
import com.example.Backend.Response.ApiResponse.PrivateChatResponse.PrivateChatResponse;
import com.example.Backend.Response.ApiResponse.PrivateChatResponse.PrivateChatWithMessagesResponse;
import com.example.Backend.Service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrivateChatImpl implements PrivateChatService{
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    UserService userService;

    @Override
    public PrivateChatResponse createPrivateChat(RequestCreatePrivateChat requestCreatePrivateChat) {
        String creatorId = requestCreatePrivateChat.getCreatorId();
        String recipientId = requestCreatePrivateChat.getRecipientId();

        Criteria criteria = new Criteria().orOperator(
                Criteria.where("creatorId").is(creatorId).and("recipientId").is(recipientId),
                Criteria.where("creatorId").is(recipientId).and("recipientId").is(creatorId)
        );

        Query query = new Query(criteria);
        PrivateChat privateChat = mongoTemplate.findOne(query, PrivateChat.class);

        PrivateChatResponse privateChatResponse = new PrivateChatResponse();

        if( privateChat != null){
            privateChatResponse.setId(privateChat.getId());
            privateChatResponse.setRecipient(userService.findUserById(privateChat.getRecipientId()));
            privateChatResponse.setCreator(userService.findUserById(privateChat.getCreatorId()));
            return privateChatResponse;
        }else{
            PrivateChat newPrivateChat = new PrivateChat();
            newPrivateChat.setCreatorId(creatorId);
            newPrivateChat.setRecipientId(recipientId);
            mongoTemplate.save(newPrivateChat);

            privateChatResponse.setId(newPrivateChat.getId());
            privateChatResponse.setRecipient(userService.findUserById(newPrivateChat.getRecipientId()));
            privateChatResponse.setCreator(userService.findUserById(newPrivateChat.getCreatorId()));
            return privateChatResponse;
        }
    }

    @Override
    public PrivateChatWithMessagesResponse getMessagesByPrivateChatId(String id) {
        Criteria criteria = Criteria.where("privateChatId").is(id);

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.lookup("users", "senderId", "_id", "sender"),
                Aggregation.unwind("sender"),
                Aggregation.project("id", "content", "createdAt", "urlFile")
        );
        AggregationResults<PrivateChatWithMessagesResponse> result = mongoTemplate.aggregate(aggregation, "messages", PrivateChatWithMessagesResponse.class);
        return (PrivateChatWithMessagesResponse) result.getMappedResults();
    }
}
