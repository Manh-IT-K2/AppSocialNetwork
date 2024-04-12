package com.example.frontend.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.fragments.CommentFragment;
import com.example.frontend.fragments.LikeFragment;
import com.example.frontend.request.Notification.Notification;
import com.example.frontend.request.Post.RequestPostByUserId;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Post.PostResponse;
import com.example.frontend.response.Post.ResponsePostById;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.service.NotificationService;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Post.PostViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class PostIDActivity extends AppCompatActivity {
    private String idcomment;
    private TextView txt_liked;
    private PostViewModel postViewModel = new PostViewModel();
    private UserViewModel userViewModel = new UserViewModel();
    private ImageView img_user, img_userLiked, img_post, btn_like, btn_comment, btn_sentPostMessenger, btn_save;
    private TextView txt_userName, txt_address, txt_contentPost, txt_timeCreatePost;
    LinearLayout linear_layout_drag_Post;

    public PostIDActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_item);

        img_user = findViewById(R.id.img_user);
        //img_userLiked = itemView.findViewById(R.id.img_userLiked);
        img_post = findViewById(R.id.img_post);
        btn_like = findViewById(R.id.btn_like);
        btn_comment = findViewById(R.id.btn_comment);
        btn_save = findViewById(R.id.btn_save);
        btn_sentPostMessenger = findViewById(R.id.btn_sentPostMessenger);

        txt_userName = findViewById(R.id.txt_UserName);
        txt_contentPost = findViewById(R.id.txt_contentPost);
        txt_address = findViewById(R.id.txt_address);
        txt_timeCreatePost = findViewById(R.id.txt_timeCreatePost);
        linear_layout_drag_Post = findViewById(R.id.linear_layout_drag_Post);
        txt_liked = findViewById(R.id.txt_liked);

        ResponsePostById post = new ResponsePostById();

        // Nhận ID từ Intent
        String id = getIntent().getStringExtra("id");
        idcomment = getIntent().getStringExtra("idComment");

        // Xử lý ID
        if (!id.isEmpty()) {
            postViewModel.getPostById(id).observe(this, new Observer<ApiResponse<ResponsePostById>>() {
                @Override
                public void onChanged(ApiResponse<ResponsePostById> postResponseApiResponse) {
                    post.setPost(postResponseApiResponse.getData().getPost());
                    post.setUser(postResponseApiResponse.getData().getUser());
                    // Set information post in views
                    txt_userName.setText(post.getUser().getUsername());
                    txt_contentPost.setText(post.getPost().getDescription());

                    List<UserResponse> list = post.getPost().getLike();
                    // set text for txt_like and set icon for btn_like
                    if (list != null) {
                        txt_liked.setText(list.size() + " lượt thích");
                        String userId = SharedPreferenceLocal.read(getApplicationContext(), "userId");

                        for (UserResponse user : list) {
                            if (user.getId().contains(userId)) {
                                post.setLike(true);
                                break;
                            }
                        }

                        if ( post.isLike()) {
                            btn_like.setImageResource(R.drawable.icon_liked);
                        } else {
                            btn_like.setImageResource(R.drawable.icon_favorite); // Thay bằng icon khác nếu không được like
                        }
                    }

                    // check location to set text
                    if(post.getPost().getLocation().isEmpty()){
                        txt_address.setText("Unknown Location");
                    }else {
                        txt_address.setText(post.getPost().getLocation());
                    }

                    // Calculate the time elapsed from the time of posting to the present time
                    String timeAgo = getTimeAgo(post.getPost().getCreateAt().toString());
                    txt_timeCreatePost.setText(timeAgo);

                    // Load image from URL using Glide
                    Glide.with(getApplicationContext())
                            .load(post.getUser().getAvatarImg())
                            .placeholder(R.drawable.logo) // Ảnh thay thế khi đang load
                            .error(R.drawable.logo) // Ảnh thay thế khi có lỗi
                            .into(img_user);

                    // display more image in imageView
                    if(post.getPost().getImagePost().size() > 1){
                        // Thêm hình ảnh vào LinearLayout
                        LinearLayout linearLayout = findViewById(R.id.linear_layout_drag_Post);
                        linearLayout.removeAllViews(); // Xóa hết các ImageView cũ trước khi thêm mới

                        // Lấy kích thước màn hình
//           DisplayMetrics displayMetrics = new DisplayMetrics();
//           ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//           int screenWidth = displayMetrics.widthPixels;

                        for (String imageUrl : post.getPost().getImagePost()) { // Giả sử getImageUrls() trả về danh sách URL hình ảnh
                            ImageView imageView = new ImageView(getApplicationContext());

                            // Tạo LayoutParams với chiều rộng bằng chiều rộng của màn hình và chiều cao mong muốn
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    350 // Set chiều cao mong muốn ở đây (350px)
                            );
                            layoutParams.setMargins(0, 0, 16, 0); // Cài đặt khoảng cách giữa các ImageView
                            imageView.setLayoutParams(layoutParams);

                            Glide.with(getApplicationContext())
                                    .load(imageUrl)
                                    .placeholder(R.drawable.logo) // Ảnh thay thế khi đang load
                                    .error(R.drawable.logo) // Ảnh thay thế khi có lỗi
                                    .into(imageView);

                            linearLayout.addView(imageView);
                        }
                    }else{
                        Glide.with(getApplicationContext())
                                .load(post.getPost().getImagePost().get(0))
                                .into(img_post);
                    }
                }
            });
        }

        // action click btn_comment display screen comment
        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentFragment dialog = new CommentFragment(PostIDActivity.this, post.getPost().getId(), idcomment, post.getUser().getId(), post.getUser().getTokenFCM());
                dialog.show();
            }
        });

        // action click like post
        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Notification notification = new Notification();
                    notification.setPostId(post.getPost().getId());
                    notification.setLikePost(true);
                    notification.setUserId(SharedPreferenceLocal.read(PostIDActivity.this, "userId"));
                    notification.setIdRecipient(post.getUser().getId());
                    String userName = SharedPreferenceLocal.read(PostIDActivity.this, "userName");

                    if (!post.isLike()) {
                        btn_like.setImageResource(R.drawable.icon_liked);
                        post.setLike(true);
                        notification.setText(userName+" vừa like bài viết của bạn");
                        userViewModel.addNotification(notification);
                        NotificationService.sendNotification(PostIDActivity.this, notification.getText(), post.getUser().getTokenFCM());
                    } else {
                        btn_like.setImageResource(R.drawable.icon_favorite);
                        post.setLike(false);
                    }

                    String userId = SharedPreferenceLocal.read(getApplicationContext(), "userId");
                    postViewModel.addLike(post.getPost().getId(), userId).observe(PostIDActivity.this, new Observer<ApiResponse<PostResponse>>() {
                        @Override
                        public void onChanged(ApiResponse<PostResponse> response) {
                            txt_liked.setText(response.getData().getLike().size() + " lượt thích");
                            Log.e("loiii",String.valueOf(response.getData().getLike().size()));
                        }
                    });
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
                List<UserResponse> likes = post.getPost().getLike();
                bundle.putString("likes", new Gson().toJson(likes));
                likeFragment.setArguments(bundle);
                Log.e("prind", new Gson().toJson(likes));

                // Navigate to the LikeFragment by replacing the current fragment
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_layout_main, likeFragment); // Replace 'fragment_container' with your container ID
                transaction.addToBackStack(null); // Add transaction to the back stack
                transaction.commit();
            }
        });
    }

    private String getTimeAgo(String createdAt) {
        try {
            Log.e("getTimeAgo", createdAt);
            // Parse chuỗi thời gian thành đối tượng Date
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
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
            Log.e("getTimeAgo", new Gson().toJson(e));
            return "N/A";
        }
    }
}