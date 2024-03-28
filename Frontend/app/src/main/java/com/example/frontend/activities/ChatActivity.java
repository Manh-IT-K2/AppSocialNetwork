package com.example.frontend.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.frontend.R;

public class ChatActivity extends AppCompatActivity {
    private TextView username_recipient;
    private ImageButton btn_back_main_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        username_recipient = findViewById(R.id.other_username);
        btn_back_main_chat = findViewById(R.id.back_btn);
        String userId = getIntent().getStringExtra("recipientUserId");
        username_recipient.setText(userId);
        btn_back_main_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}