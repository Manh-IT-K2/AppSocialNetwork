package com.example.Backend.Service.Follows;

import com.example.Backend.Entity.Follows;
import com.example.Backend.Request.Follows.RequestCreateFollows;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class FollowsImp implements FollowsService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public FollowsImp() {
    }
    @Override
    public ApiResponse<String> createFollows(RequestCreateFollows requestCreateFollows) {
        Follows user = new Follows();
        user.setIdFollower(requestCreateFollows.getIdFollower());
        user.setIdFollowing(requestCreateFollows.getIdFollowing());
        user.setCreated_at(requestCreateFollows.getCreated_at());
        user.setStatus(1);
        mongoTemplate.insert(user, "follows");
        return null;
    }
}
