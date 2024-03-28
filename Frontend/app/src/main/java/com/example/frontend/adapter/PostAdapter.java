package com.example.frontend.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

       if(post.getImagePost().size() > 1){
           // Thêm hình ảnh vào LinearLayout
           LinearLayout linearLayout = holder.itemView.findViewById(R.id.linear_layout_drag_Post);
           linearLayout.removeAllViews(); // Xóa hết các ImageView cũ trước khi thêm mới

           // Lấy kích thước màn hình
//           DisplayMetrics displayMetrics = new DisplayMetrics();
//           ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//           int screenWidth = displayMetrics.widthPixels;

           for (String imageUrl : post.getImagePost()) { // Giả sử getImageUrls() trả về danh sách URL hình ảnh
               ImageView imageView = new ImageView(mContext);

               // Tạo LayoutParams với chiều rộng bằng chiều rộng của màn hình và chiều cao mong muốn
               LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                       ViewGroup.LayoutParams.MATCH_PARENT,
                       1200 // Set chiều cao mong muốn ở đây (350px)
               );
               layoutParams.setMargins(0, 0, 16, 0); // Cài đặt khoảng cách giữa các ImageView
               imageView.setLayoutParams(layoutParams);

               Glide.with(mContext)
                       .load(imageUrl)
                       .placeholder(R.drawable.logo) // Ảnh thay thế khi đang load
                       .error(R.drawable.logo) // Ảnh thay thế khi có lỗi
                       .into(imageView);

               linearLayout.addView(imageView);
           }
       }else{
           Glide.with(mContext)
                   .load(post.getImagePost().get(0))
                   .into(holder.img_post);
       }
    }

    @Override
    public int getItemCount() {
        return listPost.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_user, img_userLiked, img_post, btn_like, btn_comment, btn_sentPostMessenger, btn_save;
        private TextView txt_userName, txt_address, txt_contentPost, txt_timeCreatePost;
        LinearLayout linear_layout_drag_Post;


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
            linear_layout_drag_Post = itemView.findViewById(R.id.linear_layout_drag_Post);

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
