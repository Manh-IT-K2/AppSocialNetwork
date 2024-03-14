package com.example.frontend.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.frontend.R;

public class StoryActivity extends AppCompatActivity {

    // innit variable
    private ImageView btn_closeStory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        // init view
        btn_closeStory = findViewById(R.id.btn_closeStory);

        // Set OnClickListener to the close button
        btn_closeStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the StoryActivity
                finish();
            }
        });
    }
}