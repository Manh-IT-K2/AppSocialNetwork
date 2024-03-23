package com.example.Backend.Service.Follows;

import com.example.Backend.Request.Follows.RequestCreateFollows;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public interface FollowsService {
    ApiResponse<String> createFollows (RequestCreateFollows requestCreateFollows) throws Exception;
}
