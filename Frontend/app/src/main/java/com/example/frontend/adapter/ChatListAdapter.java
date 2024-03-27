package com.example.frontend.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.response.PrivateChat.PrivateChatWithMessagesResponse;
import com.example.frontend.response.Message.MessageWithSenderInfo;
import com.example.frontend.response.User.UserResponse;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private List<PrivateChatWithMessagesResponse> chatList;
    private Context context;

    public ChatListAdapter(List<PrivateChatWithMessagesResponse> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
    }

    public void setChatList(PrivateChatWithMessagesResponse chat) {
        if (chatList == null) {
            chatList = new ArrayList<>();
        }
        chatList.add(chat);
        notifyDataSetChanged(); // Notify adapter that data set has changed
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_chat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.lastMessage.setText(chatList.get(position).getLastMessage());
       holder.recipientName.setText(chatList.get(position).getRecipient().getUsername());
      // holder.img_user.setImageResource(chatList.get(position).getRecipient().getAvatarImg());
        if(chatList.get(position).getRecipient().getAvatarImg() != null)
            Glide.with(context)
                    .load(Uri.parse(chatList.get(position).getRecipient().getAvatarImg()))
                    .into(holder.img_user);
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

//        public void bind(PrivateChatWithMessagesResponse chat) {
//            recipientName.setText(chat.getRecipient().getId()); // Set recipient name
//            lastMessage.setText(chat.getLastMessage()); // Set last message
//
//            List<MessageWithSenderInfo> messages = chat.getMessages();
//            if (messages != null && !messages.isEmpty()) {
//                MessageWithSenderInfo lastMessage = messages.get(messages.size() - 1);
//                UserResponse sender = lastMessage.getSender();
//                if (sender != null) {
//                    // Assuming sender has a drawable resource ID for avatar
//                    img_user.setImageResource(sender.getAvatarImg());
//                }
//            }
//        }
    }
}
