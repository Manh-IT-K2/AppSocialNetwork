package com.example.frontend.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.frontend.R;

public class PostActivity extends AppCompatActivity {

    // init variable
    private ImageView btn_closePost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // init view
        btn_closePost = findViewById(R.id.btn_closePost);

        // close page post
        btn_closePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}