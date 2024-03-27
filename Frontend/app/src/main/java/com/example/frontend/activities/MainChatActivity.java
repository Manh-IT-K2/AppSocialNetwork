package com.example.frontend.activities;

import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.user_recycler_view);
        adapter = new ChatListAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo và lắng nghe dữ liệu từ MessageViewModel
        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);
        messageViewModel.getListChat("your_id").observe(this, chatMessages -> {
            adapter.setChatList(chatMessages);
        });
    }
}