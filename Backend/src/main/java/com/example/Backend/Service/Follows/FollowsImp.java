package com.example.Backend.Service.Follows;

import com.example.Backend.Entity.Follows;
import com.example.Backend.Entity.model.Message.GetQuantityFollows;
import com.example.Backend.Entity.model.User;
import com.example.Backend.Request.Follows.RequestCreateFollows;
import com.example.Backend.Request.Follows.RequestUpdateFollows;
import com.example.Backend.Request.User.RequestGetAllUserByFollows;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public ApiResponse<GetQuantityFollows> getQuantityFollows(String id) {
        Query query = new Query(Criteria.where("_id").in(id));
        User user = mongoTemplate.findOne(query, User.class, "users");
        if ( user == null) {
            return new ApiResponse<GetQuantityFollows>(false, "No data",  new GetQuantityFollows());
        }
        GetQuantityFollows follows = new GetQuantityFollows(user.getId(),user.getFollowers(),user.getFollowing());
        return new ApiResponse<GetQuantityFollows>(true, "Success", follows);
    }

    @Override
    public ApiResponse<String> updateFollows(RequestUpdateFollows follows) {
        Query query = new Query(Criteria.where("id").is(follows.getId()));
        User resultUser = mongoTemplate.findOne(query, User.class, "users");
        if (resultUser == null) {
            return new ApiResponse<>(false, "User not found", null);
        }
        resultUser.setFollowers(follows.getFollower());
        resultUser.setFollowing(follows.getFollowing());
        mongoTemplate.save(resultUser,"users");
        return new ApiResponse<>(true, "Update Success!","");
    }
}
