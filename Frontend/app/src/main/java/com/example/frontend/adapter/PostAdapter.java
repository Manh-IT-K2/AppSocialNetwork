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

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    public Context mContext;
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
        holder.txt_address.setText("japan");
        holder.txt_contentPost.setText(post.getDescription());

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

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_user, img_userLiked, img_post, btn_like, btn_comment, btn_sentPostMessenger, btn_save;
        private TextView txt_userName, txt_address, txt_contentPost;


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
        }
    }


}
