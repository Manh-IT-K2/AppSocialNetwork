package com.example.frontend.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.adapter.SeekBarAdapter;
import com.example.frontend.adapter.StoryPagerAdapter;
import com.example.frontend.request.Story.RequestStoryByUserId;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.List;

public class DetailStoryActivity extends AppCompatActivity{

    // init variable
    private ViewPager2 viewPager_story;
    private ShapeableImageView img_avtUserStory;
    private UserViewModel userViewModel;
    private TextView txt_nameUserStory;
    private StoryPagerAdapter pagerAdapter;
    private List<RequestStoryByUserId> imageUrls;
    private Handler handler = new Handler();
    private int currentPage = 0;

    private RecyclerView recycler_view_seekbar;
    private SeekBarAdapter seekBarAdapter;
    private List<Object> seekBarList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_story);

        // init view
        viewPager_story = findViewById(R.id.viewPager_story);
        img_avtUserStory = findViewById(R.id.img_avtUserStory);
        txt_nameUserStory = findViewById(R.id.txt_nameUserStory);
        recycler_view_seekbar = findViewById(R.id.recycler_view_seekbar);
        //
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        viewPager_story.setUserInputEnabled(false);
        //
        handler.postDelayed(runnable, 5000);

        // Get data from Intent
        Intent intent = getIntent();
        if (intent != null) {
            String jsonGroupedStoryList = intent.getStringExtra("groupedStoryList");

            if (jsonGroupedStoryList != null) {
                // decode JSON encoding into list
                Gson gson = new Gson();
                Type listType = new TypeToken<List<RequestStoryByUserId>>() {
                }.getType();
                List<RequestStoryByUserId> groupedStoryList = gson.fromJson(jsonGroupedStoryList, listType);
                // load img user
                if (!groupedStoryList.isEmpty()) {
                    userViewModel.getDetailUserById(groupedStoryList.get(0).getUserId()).observe(this, new Observer<ApiResponse<UserResponse>>() {
                        @Override
                        public void onChanged(ApiResponse<UserResponse> response) {
                            if (response.getMessage().equals("Success") && response.getStatus()){
                                UserResponse userResponse = response.getData();
                                txt_nameUserStory.setText(userResponse.getUsername());
                            }
                        }
                    });
                    Glide.with(this)
                            .load(groupedStoryList.get(0).getAvtUser())
                            .into(img_avtUserStory);
                }

                // init list story
                imageUrls = groupedStoryList;

                // Initialize RecyclerView for ProgressBar
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                recycler_view_seekbar.setLayoutManager(layoutManager);
                seekBarList = new ArrayList<>();
                // Add ProgressBar objects to the list based on the number of stories
                for (int i = 0; i < imageUrls.size(); i++) {
                    seekBarList.add(new Object()); // Placeholder object for ProgressBar
                }
                seekBarAdapter = new SeekBarAdapter(this, seekBarList);
                recycler_view_seekbar.setAdapter(seekBarAdapter);

                //
                // Initialize ViewPager
                pagerAdapter = new StoryPagerAdapter(this,imageUrls);
                viewPager_story.setAdapter(pagerAdapter);
            }
        }
    }

    //
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (currentPage >= imageUrls.size() - 1) {
                currentPage = 0;
                viewPager_story.setCurrentItem(currentPage, true);
            } else {
                Log.e("loggg", String.valueOf(imageUrls.size()) + " and " + String.valueOf(currentPage));
                currentPage++;
                viewPager_story.setCurrentItem(currentPage, true);
            }

            // Nếu currentPage đạt đến giá trị cuối cùng của imageUrls, không gọi lại run() nữa
            if (currentPage < imageUrls.size() - 1) {
                handler.postDelayed(this, 5000);
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}