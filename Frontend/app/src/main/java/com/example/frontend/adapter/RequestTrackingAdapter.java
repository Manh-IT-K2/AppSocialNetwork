package com.example.frontend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.request.Post.RequestPostByUserId;
import com.example.frontend.request.User.RequestTrackingUser;

import java.util.List;

public class RequestTrackingAdapter extends RecyclerView.Adapter<RequestTrackingAdapter.ViewHolder>{
    public Context mContext;
    public List<RequestTrackingUser> listRequestTracking;

    public RequestTrackingAdapter(Context mContext, List<RequestTrackingUser> listRequestTracking) {
        this.mContext = mContext;
        this.listRequestTracking = listRequestTracking;
    }
    @NonNull
    @Override
    public RequestTrackingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_request_tracking, parent, false);
        return new RequestTrackingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestTrackingUser post = listRequestTracking.get(position);
        boolean checkStatus = Boolean.parseBoolean(post.getStatus());
        // Set thông tin bài đăng vào các view
        if (checkStatus){
            holder.status.setText("Đang theo dõi");
        } else {
            holder.status.setText("Chưa theo dõi");
        }
        holder.idNameUser.setText(post.getIdNameUser());
        holder.nameUser.setText("japan");


        // Load hình ảnh từ URL bằng Glide
        Glide.with(mContext)
                .load(post.getImageUser())
                .placeholder(R.drawable.logo) // Ảnh thay thế khi đang load
                .error(R.drawable.logo) // Ảnh thay thế khi có lỗi
                .into(holder.img_avtUser);
    }

    @Override
    public int getItemCount() {
        return listRequestTracking.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_avtUser;
        private TextView idNameUser, nameUser, status;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_avtUser = itemView.findViewById(R.id.img_avtUser);
            idNameUser = itemView.findViewById(R.id.idNameUser);
            nameUser = itemView.findViewById(R.id.nameUser);
            status = itemView.findViewById(R.id.status);
        }
    }
}
