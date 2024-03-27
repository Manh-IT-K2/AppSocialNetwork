package com.example.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.adapter.ChatListAdapter;
import com.example.frontend.viewModel.Message.MessageViewModel;

import java.util.ArrayList;

public class MainChatActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_chat);
//    }
private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private MessageViewModel messageViewModel;
    private ImageButton img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        //back

        img_back = findViewById(R.id.back_btn);

        // Khởi tạo RecyclerView và Adapter
        recyclerView = findViewById(R.id.user_recycler_view);
        adapter = new ChatListAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo và lắng nghe dữ liệu từ MessageViewModel
        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);
        messageViewModel.getListChat("65f3c106296b661ff3c54b42").observe(this, chatList -> {
            // Cập nhật dữ liệu mới nhận được vào Adapter
            adapter.setChatList(chatList);
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}