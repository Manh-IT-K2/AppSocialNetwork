package com.example.Backend.Service.Follows;

import com.example.Backend.Entity.model.Message.GetQuantityFollows;
import com.example.Backend.Request.Follows.RequestCreateFollows;
import com.example.Backend.Request.Follows.RequestUpdateFollows;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public interface FollowsService {
    ApiResponse<String> createFollows (RequestCreateFollows requestCreateFollows) throws Exception;
    ApiResponse<GetQuantityFollows> getQuantityFollows (String id);
    ApiResponse<String> updateFollows(RequestUpdateFollows follows);
}
