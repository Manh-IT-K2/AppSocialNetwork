package com.example.frontend.adapter;


import static com.example.frontend.activities.PostActivity.checkChoseMore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;

import java.util.ArrayList;
import java.util.List;

public class ImagePostAdapter extends RecyclerView.Adapter<ImagePostAdapter.ViewHolder> {

    private Context mContext;
    private List<String> imageList;
    public int selectedPosition = 0; // Vị trí của hình ảnh được chọn

    private ArrayList<Integer> selectedPositions = new ArrayList<>();
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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String imageUri = imageList.get(position);
        Glide.with(mContext).load(imageUri).into(holder.img_loadPostStore);

        // Kiểm tra nếu hình ảnh đầu tiên đã được chọn, và đang hiển thị ở vị trí đầu tiên, thì áp dụng hiệu ứng chọn
        if (position == selectedPosition) {
            holder.img_loadPostStore.setAlpha(0.5f);
        } else {
            holder.img_loadPostStore.setAlpha(1.0f);
        }
        if(checkChoseMore == 1){
            // Chọn vị trí 0 mặc định khi người dùng chọn nhiều mục
            if (selectedPositions.size() < 1 && !selectedPositions.contains(0)) {
                selectedPositions.remove(Integer.valueOf(position));
                selectedPositions.add(0);
                holder.selection_indicator.setChecked(true);
            }
            // Hiển thị hoặc ẩn RadioButton dựa trên việc mục được chọn hay không
            if (selectedPositions.contains(position)) {
                holder.selection_indicator.setVisibility(View.VISIBLE);
                holder.selection_indicator.setChecked(true);
                holder.selection_indicator.setText(String.valueOf(selectedPositions.indexOf(position) + 1)); // Hiển thị số thứ tự trong RadioButton
            } else {
                holder.selection_indicator.setVisibility(View.VISIBLE);
                holder.selection_indicator.setChecked(false);
                holder.selection_indicator.setText(""); // Xóa số thứ tự trong RadioButton
            }
        }else {
            holder.selection_indicator.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cập nhật vị trí hình ảnh được chọn
                selectedPosition = holder.getAdapterPosition();
                // Thông báo cho Activity về vị trí hình ảnh được chọn
                photoListener.onPhotoClick(imageUri);

                // Nếu mục đã được chọn, hủy chọn; nếu không, thêm vào danh sách các mục được chọn
                if(checkChoseMore == 1){
                    if (selectedPositions.contains(position)) {
                        selectedPositions.remove(Integer.valueOf(position));
                        holder.selection_indicator.setChecked(false); // Bỏ chọn RadioButton khi mục bị bỏ chọn
                    } else {
                        selectedPositions.add(position);
                        holder.selection_indicator.setChecked(true); // Chọn RadioButton khi mục được chọn
                    }
                }
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
        RadioButton selection_indicator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_loadPostStore = itemView.findViewById(R.id.img_loadPostStore);
            selection_indicator = itemView.findViewById(R.id.selection_indicator);
        }
    }

    public interface PhotoListener {
        void onPhotoClick(String path);
    }

    public ArrayList<Integer> getSelectedPositions() {
        return selectedPositions;
    }
}
