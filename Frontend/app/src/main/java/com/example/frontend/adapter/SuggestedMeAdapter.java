package com.example.frontend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.request.Post.RequestPostByUserId;
import com.example.frontend.response.User.UserResponse;

import java.util.List;

public class SuggestedMeAdapter extends RecyclerView.Adapter<SuggestedMeAdapter.ViewHolder> {

    public static Context mContext;
    public List<UserResponse> userResponseList;

    public SuggestedMeAdapter(Context mContext, List<UserResponse> userResponseList) {
        this.mContext = mContext;
        this.userResponseList = userResponseList;
    }


    @NonNull
    @Override
    public SuggestedMeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_suggest_me, parent, false);
        return new SuggestedMeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestedMeAdapter.ViewHolder holder, int position) {
        UserResponse userResponse = userResponseList.get(position);

        // Set thông tin bài đăng vào các view
        holder.idNameUser.setText(userResponse.getUsername());
        holder.nameUser.setText(userResponse.getName());

        // Load hình ảnh từ URL bằng Glide
        Glide.with(mContext)
                .load(userResponse.getAvatarImg())
                .placeholder(R.drawable.logo) // Ảnh thay thế khi đang load
                .error(R.drawable.logo) // Ảnh thay thế khi có lỗi
                .into(holder.img_avtUser);
    }

    @Override
    public int getItemCount() {
        return userResponseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_avtUser;
        private TextView idNameUser, nameUser;
        private Button btnFollow;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_avtUser = itemView.findViewById(R.id.img_avtUser);
            idNameUser = itemView.findViewById(R.id.idNameUser);
            nameUser = itemView.findViewById(R.id.nameUser);
            btnFollow = itemView.findViewById(R.id.btnFollow);
        }
    }
}
