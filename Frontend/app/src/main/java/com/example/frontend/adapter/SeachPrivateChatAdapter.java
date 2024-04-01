package com.example.frontend.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.activities.ChatActivity;
import com.example.frontend.response.User.UserResponse;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SeachPrivateChatAdapter extends RecyclerView.Adapter<SeachPrivateChatAdapter.ViewHolder> {

    private List<UserResponse> listUser;
    private Context context;

    public SeachPrivateChatAdapter(List<UserResponse> listUser, Context context) {
        this.listUser = listUser;
        this.context = context;
    }

    public void setListUser(List<UserResponse> listUser) {
        this.listUser = listUser;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_privatechat, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserResponse user = listUser.get(position);
        holder.txtName.setText(user.getName());
        holder.txtUsername.setText(user.getUsername());
        if (user.getAvatarImg() != null) {
            Glide.with(context)
                    .load(Uri.parse(user.getAvatarImg()))
                    .into(holder.img_avatar);
        } else {
            Glide.with(context)
                    .load(R.drawable.baseline_account_circle_24)
                    .into(holder.img_avatar);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("recipientUserId", user.getUsername());
                intent.putExtra("recipientAvater", user.getAvatarImg());
                intent.putExtra("recipientID", user.getId());
                context.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return listUser.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName,txtUsername;
        CircleImageView img_avatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.name_text);
            txtUsername = itemView.findViewById(R.id.user_name_text);
            img_avatar = itemView.findViewById(R.id.imgAvatar);

        }
    }

}
