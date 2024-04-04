package com.example.frontend.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.response.Post.PostResponse;
import com.example.frontend.response.User.UserResponse;

import java.util.List;

public class SearchPostAdapter extends RecyclerView.Adapter<SearchPostAdapter.MyViewHolder> {

    Context context;
    List<PostResponse> post_searchList;
    LayoutInflater layoutInflater;

    public SearchPostAdapter(Context context, List<PostResponse> post_searchList) {
        this.context = context;
        this.post_searchList = post_searchList;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.search_post_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(post_searchList.get(position).getImagePost() != null)
            Glide.with(context)
                    .load(Uri.parse(post_searchList.get(position).getImagePost().get(0)))
                    .into(holder.imgSearchPost);
    }

    @Override
    public int getItemCount() {
        return post_searchList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgSearchPost;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgSearchPost = itemView.findViewById(R.id.imgSearchPost);
        }
    }
}
