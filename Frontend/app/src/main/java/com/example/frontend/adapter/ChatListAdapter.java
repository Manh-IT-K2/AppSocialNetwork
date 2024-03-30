package com.example.frontend.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.activities.ChatActivity;
import com.example.frontend.response.GroupChat.GroupChatWithMessagesResponse;
import com.example.frontend.response.PrivateChat.PrivateChatWithMessagesResponse;
import com.example.frontend.response.Message.MessageWithSenderInfo;
import com.example.frontend.response.User.UserResponse;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private List<PrivateChatWithMessagesResponse> chatList;
    private List<GroupChatWithMessagesResponse> groupChatList;
    private Context context;

    public ChatListAdapter(List<PrivateChatWithMessagesResponse> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
    }
    public void setChatList(List<PrivateChatWithMessagesResponse> chatList) {
        this.chatList = chatList;
        notifyDataSetChanged(); // Notify adapter that dataset has changed
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_chat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PrivateChatWithMessagesResponse chat = chatList.get(position);
        holder.lastMessage.setText(chat.getLastMessage());
        holder.recipientName.setText(chat.getRecipient().getUsername());
        if (chat.getRecipient().getAvatarImg() != null) {
            Glide.with(context)
                    .load(Uri.parse(chat.getRecipient().getAvatarImg()))
                    .into(holder.img_user);
        }

        // Set click listener for the user item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ChatActivity and pass necessary data if needed
                Intent intent = new Intent(context, ChatActivity.class);
                // You can put extra data if needed, for example, recipient user id
                intent.putExtra("recipientUserId", chat.getRecipient().getUsername());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView recipientName;
        private TextView lastMessage;
        private CircleImageView img_user;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipientName = itemView.findViewById(R.id.user_name_text);
            lastMessage = itemView.findViewById(R.id.last_message_text);
            img_user = itemView.findViewById(R.id.imgAvatar);
        }
    }
}
