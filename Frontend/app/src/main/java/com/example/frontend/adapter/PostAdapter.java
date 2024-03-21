package com.example.frontend.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.fragments.CommentFragment;
import com.example.frontend.request.Post.RequestPostByUserId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    public static Context mContext;
    public List<RequestPostByUserId> listPost;

    public PostAdapter(Context mContext, List<RequestPostByUserId> listPost) {
        this.mContext = mContext;
        this.listPost = listPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestPostByUserId post = listPost.get(position);

        // Set thông tin bài đăng vào các view
        holder.txt_userName.setText(post.getUserName());
        holder.txt_contentPost.setText(post.getDescription());
        if(post.getLocation().isEmpty()){
            holder.txt_address.setText("Unknown Location");
        }else {
            holder.txt_address.setText(post.getLocation());
        }
        // Tính thời gian đã trôi qua từ thời điểm đăng bài đến thời điểm hiện tại
        String timeAgo = getTimeAgo(post.getCreateAt());
        holder.txt_timeCreatePost.setText(timeAgo);

        // Load hình ảnh từ URL bằng Glide
        Glide.with(mContext)
                .load(post.getAvtImage())
                .placeholder(R.drawable.logo) // Ảnh thay thế khi đang load
                .error(R.drawable.logo) // Ảnh thay thế khi có lỗi
                .into(holder.img_user);

        Glide.with(mContext)
                .load(post.getImagePost())
                .placeholder(R.drawable.logo) // Ảnh thay thế khi đang load
                .error(R.drawable.logo) // Ảnh thay thế khi có lỗi
                .into(holder.img_post);
    }

    @Override
    public int getItemCount() {
        return listPost.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_user, img_userLiked, img_post, btn_like, btn_comment, btn_sentPostMessenger, btn_save;
        private TextView txt_userName, txt_address, txt_contentPost, txt_timeCreatePost;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_user = itemView.findViewById(R.id.img_user);
            img_userLiked = itemView.findViewById(R.id.img_userLiked);
            img_post = itemView.findViewById(R.id.img_post);
            btn_like = itemView.findViewById(R.id.btn_like);
            btn_comment = itemView.findViewById(R.id.btn_comment);
            btn_save = itemView.findViewById(R.id.btn_save);
            btn_sentPostMessenger = itemView.findViewById(R.id.btn_sentPostMessenger);

            txt_userName = itemView.findViewById(R.id.txt_UserName);
            txt_contentPost = itemView.findViewById(R.id.txt_contentPost);
            txt_address = itemView.findViewById(R.id.txt_address);
            txt_timeCreatePost = itemView.findViewById(R.id.txt_timeCreatePost);

            //
            btn_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Hiển thị màn hình comment bằng cách sử dụng một Dialog
                    CommentFragment dialog = new CommentFragment(mContext);
                    dialog.show();
                }
            });

        }
    }

    // Hàm tính thời gian đã trôi qua từ một thời điểm cho đến hiện tại
    private String getTimeAgo(String createdAt) {
        try {
            // Parse chuỗi thời gian thành đối tượng Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date createdDate = sdf.parse(createdAt);

            // Lấy thời gian hiện tại
            Date currentDate = new Date();

            // Tính thời gian đã trôi qua
            long timeDifference = currentDate.getTime() - createdDate.getTime();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDifference);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifference);
            long hours = TimeUnit.MILLISECONDS.toHours(timeDifference);
            long days = TimeUnit.MILLISECONDS.toDays(timeDifference);

            // Trả về chuỗi biểu thị thời gian đã trôi qua
            if (days > 0) {
                return days + " days ago";
            } else if (hours > 0) {
                return hours + " hours ago";
            } else if (minutes > 0) {
                return minutes + " minutes ago";
            } else {
                return seconds + " seconds ago";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "N/A";
        }
    }

}
