package com.example.frontend.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.fragments.CommentFragment;
import com.example.frontend.fragments.LikeFragment;
import com.example.frontend.request.Notification.Notification;
import com.example.frontend.request.Post.RequestPostByUserId;
import com.example.frontend.request.Story.RequestStoryByUserId;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Post.PostResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.service.NotificationService;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Post.PostViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    public static Context mContext;
    public static List<RequestPostByUserId> listPost;
    private static LifecycleOwner lifecycleOwner;

    private static int checkLike;

    public PostAdapter(Context mContext, List<RequestPostByUserId> listPost, LifecycleOwner lifecycleOwner) {
        this.mContext = mContext;
        this.listPost = listPost;
        this.lifecycleOwner = lifecycleOwner;
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
        List<UserResponse> list = post.getLike();
        Gson gson = new Gson();
        String t = gson.toJson(post);
        Log.e("loi",t);

        // Set information post in views
        holder.txt_userName.setText(post.getUserName());
        holder.txt_contentPost.setText(post.getDescription());

        // set text for txt_like and set icon for btn_like
        if (list != null) {
            holder.txt_liked.setText(list.size() + " lượt thích");

            for (UserResponse user : list) {
                if (user.getId().contains("65e8a525714ccc3a3caa7f77")) {
                    post.setLiked(true);
                    break;
                }
            }

            if ( post.isLiked()) {
                holder.btn_like.setImageResource(R.drawable.icon_liked);
            } else {
                holder.btn_like.setImageResource(R.drawable.icon_favorite); // Thay bằng icon khác nếu không được like
            }
        }

        // check location to set text
        if(post.getLocation().isEmpty()){
            holder.txt_address.setText("Unknown Location");
        }else {
            holder.txt_address.setText(post.getLocation());
        }

        // Calculate the time elapsed from the time of posting to the present time
        String timeAgo = getTimeAgo(post.getCreateAt());
        holder.txt_timeCreatePost.setText(timeAgo);

        // Load image from URL using Glide
        Glide.with(mContext)
                .load(post.getAvtImage())
                .placeholder(R.drawable.logo) // Ảnh thay thế khi đang load
                .error(R.drawable.logo) // Ảnh thay thế khi có lỗi
                .into(holder.img_user);

        // display more image in imageView
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
                       350 // Set chiều cao mong muốn ở đây (350px)
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

        private PostAdapter adapter;
        // Khai báo biến tạm để lưu trữ txt_liked
        private TextView txt_liked;
        private PostViewModel postViewModel = new PostViewModel();
        private UserViewModel userViewModel = new UserViewModel();
        private ImageView img_user, img_userLiked, img_post, btn_like, btn_comment, btn_sentPostMessenger, btn_save;
        private TextView txt_userName, txt_address, txt_contentPost, txt_timeCreatePost;
        LinearLayout linear_layout_drag_Post;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_user = itemView.findViewById(R.id.img_user);
            //img_userLiked = itemView.findViewById(R.id.img_userLiked);
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
            txt_liked = itemView.findViewById(R.id.txt_liked);

            // action click btn_comment display screen comment
            btn_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    RequestPostByUserId post = listPost.get(position);
                    String idPost = post.getIdPost();
                    CommentFragment dialog = new CommentFragment(mContext, idPost, "",listPost.get(position).getUserId(), listPost.get(position).getTokenFCM());
                    dialog.show();
                }
            });

            // action click like post
            btn_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        RequestPostByUserId post = listPost.get(position);
                        String postId = post.getIdPost();

                        Notification notification = new Notification();
                        notification.setPostId(post.getIdPost());
                        notification.setUserId(SharedPreferenceLocal.read(itemView.getContext(), "userId"));
                        notification.setIdRecipient(post.getUserId());
                        String userName = SharedPreferenceLocal.read(itemView.getContext(), "userName");

                        if (!post.isLiked()) {
                            btn_like.setImageResource(R.drawable.icon_liked);
                            post.setLiked(true);
                            notification.setText(userName+" vừa like bài viết của bạn");
                            userViewModel.addNotification(notification);
                            NotificationService.sendNotification(mContext, notification.getText(), post.getTokenFCM());
                        } else {
                            btn_like.setImageResource(R.drawable.icon_favorite);
                            post.setLiked(false);
//                            notification.setText("Vừa bỏ like bài viết của bạn");
//                            NotificationService.sendNotification(mContext, notification.getText(), post.getTokenFCM());
                        }
                        postViewModel.addLike(postId,"65e8a525714ccc3a3caa7f77").observe(lifecycleOwner, new Observer<ApiResponse<PostResponse>>() {
                            @Override
                            public void onChanged(ApiResponse<PostResponse> response) {
//                                List<UserResponse> list = post.getLike();
//                                if(list != null){
                                    txt_liked.setText(response.getData().getLike().size() + " lượt thích");
                                    Log.e("loiii",String.valueOf(response.getData().getLike().size()));
                                    //adapter.updateData();
                                //}
                            }
                        });
                    }
                }
            });

            // set action click txt_liked
            txt_liked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create an instance of the LikeFragment
                    LikeFragment likeFragment = new LikeFragment();

                    // Pass data (list of likes) to the LikeFragment
                    Bundle bundle = new Bundle();
                    List<UserResponse> likes = listPost.get(getAdapterPosition()).getLike();
                    bundle.putString("likes", new Gson().toJson(likes));
                    likeFragment.setArguments(bundle);
                    Log.e("prind", new Gson().toJson(likes));

                    // Navigate to the LikeFragment by replacing the current fragment
                    FragmentTransaction transaction = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_layout_main, likeFragment); // Replace 'fragment_container' with your container ID
                    transaction.addToBackStack(null); // Add transaction to the back stack
                    transaction.commit();
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

    //
    public void updateData() {
        notifyDataSetChanged();
    }

}
