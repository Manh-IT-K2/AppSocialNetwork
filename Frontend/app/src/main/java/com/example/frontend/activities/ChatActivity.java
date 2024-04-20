package com.example.frontend.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.adapter.PrivateChatAdapter;
import com.example.frontend.request.GroupChat.RequestChatGroup;
import com.example.frontend.request.PrivateChat.RequestPrivateChat;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Message.MessageWithSenderInfo;
import com.example.frontend.response.PrivateChat.PrivateChatWithMessagesResponse;
import com.example.frontend.utils.FirebaseStorageUploader;
import com.example.frontend.utils.PusherClient;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Message.MessageViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pusher.client.Pusher;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.stipop.Stipop;
import io.stipop.StipopDelegate;
import io.stipop.extend.StipopImageView;
import io.stipop.model.SPPackage;
import io.stipop.model.SPSticker;

public class ChatActivity extends AppCompatActivity implements StipopDelegate {
    private TextView username_recipient;
    private ImageButton btn_back_main_chat;
    private CircleImageView imgAvatar;
    private ImageButton btnSend, btn_File, btn_openSticker;
    private Pusher pusher;
    private RecyclerView recyclerView;
    private PrivateChatAdapter adapter;
    private MessageViewModel messageViewModel;
    private EditText inputMessage;
    private String recipientUserId;
    private String recipientAvatar;
    private String recipientId;
    private String userId;

    private Context context;
    StipopImageView btn_Sticker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        context = this;
        recipientUserId = getIntent().getStringExtra("recipientUserId");
        recipientAvatar = getIntent().getStringExtra("recipientAvater");
        recipientId = getIntent().getStringExtra("recipientID");
        userId = SharedPreferenceLocal.read(getApplicationContext(), "userId");


        username_recipient = findViewById(R.id.other_username);
        btn_back_main_chat = findViewById(R.id.back_btn);
        imgAvatar = findViewById(R.id.imgAvatar);
        recyclerView = findViewById(R.id.chat_recycler_view);
        btnSend = findViewById(R.id.message_send_btn);
        inputMessage = findViewById(R.id.chat_message_input);
        btn_Sticker = findViewById(R.id.btn_Sticker);
        btn_File = findViewById(R.id.btn_File);
        btn_openSticker = findViewById(R.id.btn_openSticker);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PrivateChatAdapter(new ArrayList<>(), this, userId);
        recyclerView.setAdapter(adapter);


        username_recipient.setText(recipientUserId);
        if (recipientAvatar != null) {
            Glide.with(getApplicationContext())
                    .load(Uri.parse(recipientAvatar))
                    .into(imgAvatar);
        }else {
            Glide.with(getApplicationContext())
                    .load(R.drawable.baseline_account_circle_24) // Ảnh mặc định trong thư mục drawable
                    .into(imgAvatar);
        }
        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);
        btn_back_main_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChatActivity.this,MainChatActivity.class);
                startActivity(i);
                //finish();
            }
        });
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Bundle để chứa ID của người nhận
                Intent intent = new Intent(getApplicationContext(), FragmentReplacerActivity.class);
                // Thêm dữ liệu cho Intent để FragmentReplacerActivity biết cần thay thế fragment nào
                intent.putExtra("fragment_to_load", "Profile_recipetn");
                intent.putExtra("userId", recipientId);
                // Bắt đầu activity với Intent đã tạo
                startActivity(intent);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMessage();
            }
        });

        loadInitialMessages();

        Stipop.Companion.connect(this, btn_Sticker, "1234", "en", "US", this);
        //btn_Sticker.setOnClickListener(v -> Stipop.Companion.showSearch());

        btn_openSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stipop.Companion.showSearch();
            }
        });

        btn_File.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });

        pusher = PusherClient.init();
        pusher.connect();
        pusher.subscribe(userId)
                .bind("send", (channelName, eventName, data) -> {
                    try {
                        Gson gson = new GsonBuilder()
                                .setDateFormat("MMM dd, yyyy, hh:mm:ss a")
                                .create();
                        String jsonData = data.toString();
                        MessageWithSenderInfo messages = gson.fromJson(jsonData, MessageWithSenderInfo.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.addNewMessage(messages);
                                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                                // Gửi Broadcast Intent với dữ liệu tin nhắn mới
                                Intent intent = new Intent("NEW_MESSAGE_ACTION");
                                Gson gson = new Gson();
                                String messagesJson = gson.toJson(messages); // Chuyển đổi danh sách tin nhắn thành chuỗi JSON
                                intent.putExtra("new_message", messagesJson); // Đặt chuỗi JSON vào Intent
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                            }
                        });
                    } catch (Exception e) {
                        Log.d("trycatch", new Gson().toJson(e));
                    }
                });
    }

    private void loadInitialMessages() {
        messageViewModel.getMessagesByPrivate(userId, recipientId).observe(this, new Observer<ApiResponse<PrivateChatWithMessagesResponse>>() {
            @Override
            public void onChanged(ApiResponse<PrivateChatWithMessagesResponse> response) {
                if (response != null && response.isSuccess()) {
                    PrivateChatWithMessagesResponse privateChatResponse = response.getData();
                    List<MessageWithSenderInfo> messages = privateChatResponse.getMessages();
                    if (messages != null && !messages.isEmpty()) {
                        adapter.setListMessage(messages);
                        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    }
                } else {
                    String errorMessage = "Failed to load initial messages";
                    if (response != null && response.getMessage() != null) {
                        errorMessage += ": " + response.getMessage();
                    }
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void sendMessage() {
        String message = inputMessage.getText().toString();
        RequestPrivateChat requestPrivateChat = new RequestPrivateChat(userId, recipientId, message, "", "");
        messageViewModel.sendMessage(requestPrivateChat);
        inputMessage.setText(null);
    }

    @Override
    public boolean canDownload(@NonNull SPPackage spPackage) {
        return true;
    }

    @Override
    public boolean onStickerSelected(@NonNull SPSticker spSticker) {
        RequestPrivateChat requestPrivateChat = new RequestPrivateChat(userId, recipientId, "", "", spSticker.getStickerImg());
        messageViewModel.sendMessage(requestPrivateChat);
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri fileUri = data.getData();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timestamp = sdf.format(new Date());
            String fileName = "file_" + timestamp+ "_"+ new File(fileUri.getPath()).getName() + ".jpg";
            FirebaseStorageUploader.uploadFileToFirebaseStorage(fileUri, fileName, new FirebaseStorageUploader.OnUploadCompleteListener() {
                @Override
                public void onUploadComplete(List<String> fileUrls) {

                }

                @Override
                public void onUploadComplete(String fileUrl) {
                    RequestPrivateChat requestPrivateChat = new RequestPrivateChat(userId, recipientId, "", fileUrl, "");
                    messageViewModel.sendMessage(requestPrivateChat);
                }

                @Override
                public void onUploadFailed(String errorMessage) {
                    Toast.makeText(getApplicationContext(), "Upload failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}