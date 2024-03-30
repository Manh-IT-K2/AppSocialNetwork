package com.example.frontend.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.activities.ChatActivity;
import com.example.frontend.response.GroupChat.GroupChatWithMessagesResponse;
import com.example.frontend.response.PrivateChat.PrivateChatWithMessagesResponse;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private List<PrivateChatWithMessagesResponse> privateChatList;
    private List<GroupChatWithMessagesResponse> groupChatList;
    private Context context;

    public ChatListAdapter(List<PrivateChatWithMessagesResponse> privateChatList, List<GroupChatWithMessagesResponse> groupChatList, Context context) {
        this.privateChatList = privateChatList;
        this.groupChatList = groupChatList;
        this.context = context;
    }
    public void setChatList(List<PrivateChatWithMessagesResponse> privateChatList) {
        this.privateChatList = privateChatList;
        notifyDataSetChanged();
    }
    public void setGroupChatList(List<GroupChatWithMessagesResponse> groupChatList) {
        this.groupChatList = groupChatList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position < privateChatList.size() ? 0 : 1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_chat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < privateChatList.size()) {
            PrivateChatWithMessagesResponse privateChat = privateChatList.get(position);
            holder.bindPrivateChat(privateChat);
        } else {
            int adjustedPosition = position - privateChatList.size();
            GroupChatWithMessagesResponse groupChat = groupChatList.get(adjustedPosition);
            holder.bindGroupChat(groupChat);
        }

        // Set click listener for the user item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("recipientUserId", chat.getRecipient().getUsername());
                intent.putExtra("recipientAvater", chat.getRecipient().getAvatarImg());
                intent.putExtra("recipientID", chat.getRecipient().getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return privateChatList.size() + groupChatList.size();
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

        public void bindPrivateChat(PrivateChatWithMessagesResponse privateChat) {
            recipientName.setText(privateChat.getRecipient().getUsername());
            lastMessage.setText(privateChat.getLastMessage());
            if (privateChat.getRecipient().getAvatarImg() != null) {
                Glide.with(context)
                        .load(Uri.parse(privateChat.getRecipient().getAvatarImg()))
                        .into(img_user);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("recipientUserId", privateChat.getRecipient().getUsername());
                    context.startActivity(intent);
                }
            });
        }

        public void bindGroupChat(GroupChatWithMessagesResponse groupChat) {
            recipientName.setText(groupChat.getGroupName());
            lastMessage.setText(groupChat.getLastMessage() != null ? groupChat.getLastMessage() : "Chưa có tin nhắn");
            if (!groupChat.getMembers().isEmpty() && groupChat.getMembers().get(0).getAvatarImg() != null) {
                Glide.with(context)
                        .load(Uri.parse(groupChat.getMembers().get(0).getAvatarImg()))
                        .into(img_user);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("groupChatId", groupChat.getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
