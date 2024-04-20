package com.example.Backend.Controller;

import com.example.Backend.Entity.Post;
import com.example.Backend.Entity.Story;
import com.example.Backend.Request.Story.RequestCreateStory;
import com.example.Backend.Request.Story.RequestStoryByUserId;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import com.example.Backend.Service.Story.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/story")
public class StoryController {

    @Autowired
    StoryService storyService;

    // createPost
    @PostMapping("/createStory")
    public ResponseEntity<ApiResponse<String>> createStory(@RequestBody RequestCreateStory requestStory, @RequestParam String userId) {
        storyService.createStory(requestStory,userId);
        ApiResponse<String> apiResponse = new ApiResponse<String>(true, "Tải lên tin thành công !","");
        return new ResponseEntity<ApiResponse<String>>(apiResponse, HttpStatus.OK);
    }

    // select post
    @GetMapping("/getListStoryByUserId")
    public ResponseEntity<ApiResponse<List<RequestStoryByUserId>>> getListStoryByUserId(@RequestParam String userId) {
        if (userId.isEmpty()) userId = "";
        System.out.println(userId);
        ApiResponse<List<RequestStoryByUserId>> result = storyService.getListStoryByUserId(userId);
        return new ResponseEntity<ApiResponse<List<RequestStoryByUserId>>>(result, HttpStatus.OK);
    }

    // add viewer
    @PostMapping("/addViewer")
    public ResponseEntity<ApiResponse<String>> addViewerStory(@RequestParam String storyId, @RequestParam String userId){
        if (!storyId.isEmpty() && !userId.isEmpty()) {
            storyService.addViewedStory(storyId, userId);
            ApiResponse<String> apiResponse = new ApiResponse<String>(true, "success !","");
            return new ResponseEntity<ApiResponse<String>>(apiResponse, HttpStatus.OK);
        }
        return null;
    }
}

