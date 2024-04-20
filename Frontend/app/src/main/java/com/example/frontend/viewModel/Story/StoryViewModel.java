package com.example.frontend.viewModel.Story;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.frontend.repository.StoryRepository;
import com.example.frontend.request.Story.RequestCreateStory;
import com.example.frontend.request.Story.RequestStoryByUserId;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Story.StoryResponse;

import java.util.List;

public class StoryViewModel extends ViewModel {
    private StoryRepository storyRepository;

    public StoryViewModel(){
        storyRepository = new StoryRepository();
    }

    // create story
    public void createStory(RequestCreateStory createStory, String userId){
        storyRepository.createStory(createStory,userId);
    }

    // get list story by user id
    public MutableLiveData<ApiResponse<List<RequestStoryByUserId>>> getListStoryByUserId(String userId){
        return storyRepository.getListStoryByUserId(userId);
    }

    // add viewer story
    public void addViewerStory(String storyId, String userId){
        storyRepository.addViewerStory(storyId,userId);
    }

    // delete story
    public void deleteStoryById(String idStory){
        storyRepository.deleteStoryById(idStory);
    }
}
