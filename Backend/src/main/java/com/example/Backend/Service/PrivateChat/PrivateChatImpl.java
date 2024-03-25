package com.example.Backend.Service.PrivateChat;

import com.example.Backend.Entity.Message;
import com.example.Backend.Entity.PrivateChat;
import com.example.Backend.Entity.model.Message.MessageWithSenderInfo;
import com.example.Backend.Entity.model.User;
import com.example.Backend.Request.PrivateChat.RequestChatPrtivate;
import com.example.Backend.Request.PrivateChat.RequestCreatePrivateChat;
import com.example.Backend.Request.User.RequestCreateAccount;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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

    @Override
    public PrivateChatWithMessagesResponse getMessagesByPrivateChatId(String id) throws Exception {
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
        newMessage.setCreatedAt(new Date());
        newMessage.setPrivateChatId(privateChat.getId());
        mongoTemplate.save(newMessage);
        privateChat.setLastMessageSent(requestChatPrtivate.getLastMessageSent());
        mongoTemplate.save(privateChat);
        List<MessageWithSenderInfo> messages = getMessageList(privateChat);
        PrivateChatWithMessagesResponse response = new PrivateChatWithMessagesResponse();
        response.setMessages(messages);
        return response;
}
    private List<MessageWithSenderInfo> getMessageList(PrivateChat privateChat) {
            List<Message> messages = mongoTemplate.find(Query.query(Criteria.where("privateChatId").is(privateChat.getId())), Message.class);
            List<MessageWithSenderInfo> messageWithSenderInfos = new ArrayList<>();
        for (Message message : messages) {
            // Tạo đối tượng MessageWithSenderInfo và thiết lập thông tin tin nhắn cùng với thông tin người gửi
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



