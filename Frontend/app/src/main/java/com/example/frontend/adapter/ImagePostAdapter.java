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

import java.util.List;

public class ImagePostAdapter extends RecyclerView.Adapter<ImagePostAdapter.ViewHolder> {

    private Context mContext;
    private List<Uri> imageList;

    public ImagePostAdapter(Context mContext, List<Uri> imageList) {
        this.mContext = mContext;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item_store, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri imageUri = imageList.get(position);
        Glide.with(mContext)
                .load(imageUri)
                .into(holder.img_loadPostStore);
    }

    @Override
    public int getItemCount() {
        return imageList != null ? imageList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_loadPostStore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_loadPostStore = itemView.findViewById(R.id.img_loadPostStore);
        }
    }
}
