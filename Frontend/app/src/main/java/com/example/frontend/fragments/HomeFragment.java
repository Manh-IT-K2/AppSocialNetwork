package com.example.frontend.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.frontend.R;
import com.example.frontend.activities.MainChatActivity;
import com.example.frontend.adapter.PostAdapter;
import com.example.frontend.adapter.StoryAdapter;
import com.example.frontend.request.Post.RequestPostByUserId;
import com.example.frontend.request.Story.RequestStoryByUserId;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Post.PostViewModel;
import com.example.frontend.viewModel.Story.StoryViewModel;
import com.google.gson.Gson;

import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewPost, recyclerViewStory;
    private PostAdapter postAdapter;
    private StoryAdapter storyAdapter;
    private List<RequestPostByUserId> postList;
    private List<RequestStoryByUserId> storyList;
    private PostViewModel postViewModel;
    private StoryViewModel storyViewModel;
    private ImageView imgMessage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // init list post
        recyclerViewPost = view.findViewById(R.id.list_post);
        recyclerViewPost.setLayoutManager(new LinearLayoutManager(getContext()));

        // init list story
        recyclerViewStory = view.findViewById(R.id.list_story);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewStory.setLayoutManager(layoutManager);

        // init call api post
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        // init call api story
        storyViewModel = new ViewModelProvider(this).get(StoryViewModel.class);

        //View Chat
        imgMessage = view.findViewById(R.id.img_message);
        // init userId
        String userId = SharedPreferenceLocal.read(getContext(),"userId");

        // call api display list story in home
        storyViewModel.getListStoryByUserId("65e8a525714ccc3a3caa7f77").observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<RequestStoryByUserId>>>() {
            @Override
            public void onChanged(ApiResponse<List<RequestStoryByUserId>> response) {
                Gson gson = new Gson();
                String json = gson.toJson(response);
                Log.d("check1", json);
                if (response.getData().size() > 0) {
                    storyList = response.getData();
                    storyAdapter = new StoryAdapter(getContext(), storyList);
                    recyclerViewStory.setAdapter(storyAdapter);
                } else {
                    // Xử lý khi không có dữ liệu hoặc có lỗi
                }
            }
        });

        // call api display list post in home
        postViewModel.getListPostByUserId("65e8a525714ccc3a3caa7f77").observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<RequestPostByUserId>>>() {
            @Override
            public void onChanged(ApiResponse<List<RequestPostByUserId>> response) {
                Gson gson = new Gson();
                String json = gson.toJson(response);
                Log.d("check", json);
                if (response.getData().size() > 0) {
                    postList = response.getData();
                    postAdapter = new PostAdapter(getContext(), postList);
                    recyclerViewPost.setAdapter(postAdapter);
                } else {
                    // Xử lý khi không có dữ liệu hoặc có lỗi
                }
            }
        });
       imgMessage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              //Toast.makeText(getContext(),"hello",Toast.LENGTH_LONG).show();
               Intent intent = new Intent(getActivity(),MainChatActivity.class);
               startActivity(intent);


           }
       });

        return view;
    }
}