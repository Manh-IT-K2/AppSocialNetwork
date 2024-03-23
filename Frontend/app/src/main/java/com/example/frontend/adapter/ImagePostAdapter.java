package com.example.frontend.adapter;


import android.content.Context;
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
    private List<String> imageList;
    public int selectedPosition = 0; // Vị trí của hình ảnh được chọn
    private PhotoListener photoListener;

    public ImagePostAdapter(Context mContext, List<String> imageList, PhotoListener photoListener) {
        this.mContext = mContext;
        this.imageList = imageList;
        this.photoListener = photoListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item_store, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageUri = imageList.get(position);
        Glide.with(mContext).load(imageUri).into(holder.img_loadPostStore);

        // Kiểm tra nếu hình ảnh đầu tiên đã được chọn, và đang hiển thị ở vị trí đầu tiên, thì áp dụng hiệu ứng chọn
        if (position == selectedPosition) {
            holder.img_loadPostStore.setAlpha(0.5f);
        } else {
            holder.img_loadPostStore.setAlpha(1.0f);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cập nhật vị trí hình ảnh được chọn
                selectedPosition = holder.getAdapterPosition();
                // Thông báo cho Activity về vị trí hình ảnh được chọn
                photoListener.onPhotoClick(imageUri);
                // Cập nhật giao diện để hiển thị hiệu ứng chọn
                notifyDataSetChanged();
            }
        });
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

    public interface PhotoListener {
        void onPhotoClick(String path);
    }
}
