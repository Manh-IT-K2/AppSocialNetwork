package com.example.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.adapter.ChatListAdapter;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Message.MessageViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.squareup.picasso.Picasso;

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
    private TextView username;
    UserViewModel userViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        String userId = SharedPreferenceLocal.read(getApplicationContext(), "userId");
        //back
        img_back = findViewById(R.id.back_btn);
        username = findViewById(R.id.userchat);

        // Khởi tạo RecyclerView và Adapter
        recyclerView = findViewById(R.id.user_recycler_view);
        adapter = new ChatListAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//set user name name
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getDetailUserById(userId).observe(this, new Observer<ApiResponse<UserResponse>>() {
            @Override
            public void onChanged(ApiResponse<UserResponse> response) {
                if (response.getMessage().equals("Success") && response.getStatus()){
                    UserResponse userResponse = response.getData();
                    username.setText(userResponse.getUsername());

                }
            }
        });
        // Khởi tạo và lắng nghe dữ liệu từ MessageViewModel
        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);
        messageViewModel.getListChat(userId).observe(this, chatList -> {
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