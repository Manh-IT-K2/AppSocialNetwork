package com.example.Backend.Service.PrivateChat;

import com.example.Backend.Entity.GroupChat;
import com.example.Backend.Entity.Message;
import com.example.Backend.Entity.PrivateChat;
import com.example.Backend.Entity.model.Message.MessageWithSenderInfo;
import com.example.Backend.Entity.model.User;
import com.example.Backend.Request.PrivateChat.RequestChatPrtivate;
import com.example.Backend.Request.PrivateChat.RequestCreatePrivateChat;
import com.example.Backend.Request.User.RequestCreateAccount;
import com.example.Backend.Response.ApiResponse.GroupChatResponse.GroupChatWithMessagesResponse;
import com.example.Backend.Response.ApiResponse.PrivateChatResponse.PrivateChatResponse;
import com.example.Backend.Response.ApiResponse.PrivateChatResponse.PrivateChatWithMessagesResponse;
import com.example.Backend.Service.User.UserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PrivateChatImpl implements PrivateChatService {
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

        if (privateChat != null) {
            privateChatResponse.setId(privateChat.getId());
            privateChatResponse.setRecipient(userService.findUserById(privateChat.getRecipientId()));
            privateChatResponse.setCreator(userService.findUserById(privateChat.getCreatorId()));
            return privateChatResponse;
        } else {
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

//    @Override
//    public PrivateChatWithMessagesResponse getMessagesByPrivateChatId(String id) throws Exception {
//        Criteria criteria = Criteria.where("privateChatId").is(id);
//
//        Aggregation aggregation = Aggregation.newAggregation(
//                Aggregation.match(criteria),
//                Aggregation.lookup("users", "senderId", "_id", "sender"),
//                Aggregation.unwind("sender"),
//                Aggregation.project("id", "content", "createdAt", "urlFile")
//        );
//        AggregationResults<PrivateChatWithMessagesResponse> result = mongoTemplate.aggregate(aggregation, "messages", PrivateChatWithMessagesResponse.class);
//        return (PrivateChatWithMessagesResponse) result.getMappedResults();
//
//    }
@Override
public PrivateChatWithMessagesResponse getMessagesByPrivateChatId(String id) throws Exception {
    Criteria criteria = Criteria.where("privateChatId").is(id);

    Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.match(criteria),
            Aggregation.lookup("users", "senderId", "_id", "sender"),
            Aggregation.unwind("sender"),
            Aggregation.project()
                    .and("id").as("id")
                    .and("content").as("content")
                    .and("createdAt").as("createdAt")
                    .and("urlFile").as("urlFile")
    );

    AggregationResults<PrivateChatWithMessagesResponse> result = mongoTemplate.aggregate(aggregation, "messages", PrivateChatWithMessagesResponse.class);
    List<PrivateChatWithMessagesResponse> mappedResults = result.getMappedResults();
    if (!mappedResults.isEmpty()) {
        return mappedResults.get(0);
    } else {
        throw new Exception("Private chat not found for id: " + id);
    }
}

    @Override
    public PrivateChatWithMessagesResponse SendMessage(RequestChatPrtivate requestChatPrtivate) throws Exception {
        String creatorId = requestChatPrtivate.getCreatorId();
        String recipientId = requestChatPrtivate.getRecipientId();
        Criteria criteria = new Criteria().orOperator(
            Criteria.where("creatorId").is(creatorId).and("recipientId").is(recipientId),
            Criteria.where("creatorId").is(recipientId).and("recipientId").is(creatorId)
    );
        Query query = new Query(criteria);
        PrivateChat privateChat = mongoTemplate.findOne(query, PrivateChat.class);
        Message newMessage = new Message();
        newMessage.setSenderId(requestChatPrtivate.getCreatorId());
        newMessage.setContent(requestChatPrtivate.getLastMessageSent());
        Date createdAt = Date.from(Instant.now());
        newMessage.setCreatedAt(createdAt);
        newMessage.setPrivateChatId(privateChat.getId());
        mongoTemplate.save(newMessage);
        privateChat.setLastMessageSent(requestChatPrtivate.getLastMessageSent());
        mongoTemplate.save(privateChat);
        List<MessageWithSenderInfo> messages = getMessageList(privateChat);
        PrivateChatWithMessagesResponse response = new PrivateChatWithMessagesResponse();
        response.setMessages(messages);
        return response;
}

    @Override
    public List<PrivateChatWithMessagesResponse> getListChat(String id) {
        List<PrivateChatWithMessagesResponse> responses = new ArrayList<>();
        Criteria criteria = new Criteria().orOperator(
                Criteria.where("creatorId").is(id),
                Criteria.where("recipientId").is(id)
        );
        Query query = new Query(criteria);
        List<PrivateChat> privateChats = mongoTemplate.find(query, PrivateChat.class);
        for (PrivateChat chat : privateChats) {
            String recipientId = chat.getCreatorId().equals(id) ? chat.getRecipientId() : chat.getCreatorId();
            PrivateChatWithMessagesResponse response = new PrivateChatWithMessagesResponse();
            User recipient = userService.findUserById(recipientId);
            response.setRecipient(recipient);
            List<MessageWithSenderInfo> messages = getMessageList(chat);
            if (!messages.isEmpty()) {
                MessageWithSenderInfo lastMessage = messages.get(messages.size() - 1);
                response.setLastMessage(lastMessage.getContent());
            }
            responses.add(response);
        }

        return responses;
    }


    private List<MessageWithSenderInfo> getMessageList(PrivateChat privateChat) {
            List<Message> messages = mongoTemplate.find(Query.query(Criteria.where("privateChatId").is(privateChat.getId())), Message.class);
            List<MessageWithSenderInfo> messageWithSenderInfos = new ArrayList<>();
        for (Message message : messages) {
            MessageWithSenderInfo messageWithSenderInfo = new MessageWithSenderInfo();
            messageWithSenderInfo.setContent(message.getContent());
            messageWithSenderInfo.setId(message.getId());
            messageWithSenderInfo.setPrivateChatId(message.getPrivateChatId());
            messageWithSenderInfo.setCreatedAt(message.getCreatedAt());
            messageWithSenderInfo.setUrlFile(message.getUrlFile());
            messageWithSenderInfo.setGroupChatId(message.getGroupChatId());
            messageWithSenderInfo.setSender(userService.findUserById(message.getSenderId()));
            messageWithSenderInfos.add(messageWithSenderInfo);
        }
        return messageWithSenderInfos;
    }

}



