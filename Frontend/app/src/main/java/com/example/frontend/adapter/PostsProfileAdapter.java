package com.example.frontend.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.request.Post.RequestPostByUserId;
import com.example.frontend.viewModel.Post.PostViewModel;
import com.example.frontend.viewModel.User.UserViewModel;

import java.util.List;

public class PostsProfileAdapter extends RecyclerView.Adapter<PostsProfileAdapter.ViewHolder> {
    public static Context mContext;
    public List<RequestPostByUserId> postResponseList;
    private LifecycleOwner lifecycleOwner;
    public static UserViewModel userViewModel;
    public static PostViewModel postViewModel;

    public PostsProfileAdapter(Context mContext, List<RequestPostByUserId> postResponseList,LifecycleOwner lifecycleOwner,
                           UserViewModel userViewModel,PostViewModel postViewModel) {
        this.mContext = mContext;
        this.postResponseList = postResponseList;
        this.lifecycleOwner = lifecycleOwner;
        this.userViewModel = userViewModel;
        this.postViewModel = postViewModel;
    }
    @NonNull
    @Override
    public PostsProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_post_profile, parent, false);
        return new PostsProfileAdapter.ViewHolder(view,lifecycleOwner);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsProfileAdapter.ViewHolder holder, int position) {
        RequestPostByUserId post = postResponseList.get(position);
        Glide.with(mContext)
                .load(post.getImagePost().get(0))
                .placeholder(R.drawable.logo) // Ảnh thay thế khi đang load
                .error(R.drawable.logo) // Ảnh thay thế khi có lỗi
                .into(holder.imagePost);
    }

    @Override
    public int getItemCount() {
        return postResponseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imagePost;
        private LifecycleOwner lifecycleOwner;
        public ViewHolder(@NonNull View itemView,LifecycleOwner lifecycleOwner) {
            super(itemView);
            imagePost = itemView.findViewById(R.id.imagePost);
            this.lifecycleOwner = lifecycleOwner;
            imagePost.setMaxWidth(200);
            imagePost.setMaxHeight(200);
            imagePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Width", String.valueOf(imagePost.getWidth()));
                    Log.d("Height", String.valueOf(imagePost.getHeight()));
                }
            });
        }

    }
}
