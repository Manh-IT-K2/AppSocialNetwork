package com.example.frontend.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
//import com.example.frontend.activities.CreateStoryActivity;
import com.example.frontend.activities.DetailStoryActivity;
import com.example.frontend.activities.StoryActivity;
import com.example.frontend.request.Story.RequestStoryByUserId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class StoryPagerAdapter extends RecyclerView.Adapter<StoryPagerAdapter.ViewHolder> {
    private Context mContext;
    private List<RequestStoryByUserId> imageUrls;

    public StoryPagerAdapter(Context mContext, List<RequestStoryByUserId> imageUrls) {
        this.mContext = mContext;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_story_display, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.btn_addStoryMain.setVisibility(View.GONE);
        String imageUrl = imageUrls.get(position).getImage();
        Glide.with(holder.itemView.getContext()).load(imageUrl).into(holder.img_storyUri);
        holder.txt_timeCreateStory.setText(getTimeAgo(imageUrls.get(position).getCreatedAt()));
        if (imageUrls != null && imageUrls.get(position).getStickers() != null) {
            // Lặp qua danh sách sticker của câu chuyện
            for (int i = 0; i < imageUrls.get(position).getStickers().size(); i++) {
                // Lấy đường dẫn ảnh của sticker
                String imgSticker = imageUrls.get(position).getStickers().get(i).getUriSticker();
                // Tạo ImageView mới để hiển thị sticker
                ImageView stickerImageView = new ImageView(mContext);
                // Sử dụng Glide để tải ảnh và đặt vào ImageView
                Glide.with(mContext).load(imgSticker).into(stickerImageView);
                // Đặt kích thước cho sticker
                int width = 300; // Đặt chiều rộng
                int height = 300; // Đặt chiều cao
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
                stickerImageView.setLayoutParams(layoutParams);
                // Lấy vị trí X và Y của sticker từ dữ liệu của câu chuyện
                float marginTop = imageUrls.get(position).getStickers().get(i).getX();
                float marginLeft = imageUrls.get(position).getStickers().get(i).getY();
                // Tạo một ConstraintSet để đặt các ràng buộc cho stickerImageView
                ConstraintSet constraintSet = new ConstraintSet();
                // Thêm ImageView của sticker vào ConstraintLayout
                holder.layout_constraint_story.addView(stickerImageView);
                // Gán một ID duy nhất cho ImageView của sticker
                stickerImageView.setId(View.generateViewId());
                constraintSet.clone(holder.layout_constraint_story);
                constraintSet.connect(stickerImageView.getId(), ConstraintSet.TOP, holder.layout_constraint_story.getId(), ConstraintSet.TOP, Math.round(marginTop));
                constraintSet.connect(stickerImageView.getId(), ConstraintSet.START, holder.layout_constraint_story.getId(), ConstraintSet.START, Math.round(marginLeft));
                constraintSet.applyTo(holder.layout_constraint_story);
            }
        }

        //
        if (imageUrls != null && imageUrls.get(position).getContentMedia() != null) {
            for (int i = 0; i < imageUrls.get(position).getContentMedia().size(); i++) {
                String text = imageUrls.get(position).getContentMedia().get(i).getContent();

                // Tạo một TextView mới để hiển thị văn bản
                TextView textView = new TextView(mContext);
                textView.setText(text);
                // Đặt kích thước và kiểu chữ cho TextView
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                textView.setTextSize(20);
                textView.setTextColor(Color.WHITE);
                textView.setTypeface(null, Typeface.BOLD);
                textView.setBackgroundColor(Color.TRANSPARENT);
                textView.setLayoutParams(layoutParams);

                // Lấy vị trí X và Y của văn bản từ dữ liệu của câu chuyện
                float marginTop = imageUrls.get(position).getContentMedia().get(i).getX();
                float marginLeft = imageUrls.get(position).getContentMedia().get(i).getY();

                // Tạo một ConstraintSet để đặt các ràng buộc cho textView
                ConstraintSet constraintSet = new ConstraintSet();
                // Thêm TextView của văn bản vào ConstraintLayout
                holder.layout_constraint_story.addView(textView);
                // Gán một ID duy nhất cho TextView của văn bản
                textView.setId(View.generateViewId());
                constraintSet.clone(holder.layout_constraint_story);
                constraintSet.connect(textView.getId(), ConstraintSet.TOP, holder.layout_constraint_story.getId(), ConstraintSet.TOP, Math.round(marginTop));
                constraintSet.connect(textView.getId(), ConstraintSet.START, holder.layout_constraint_story.getId(), ConstraintSet.START, Math.round(marginLeft));
                constraintSet.applyTo(holder.layout_constraint_story);
            }
        }

        if (imageUrls.get(position).getImage().equals("")) {
            // Kiểm tra nếu không có hình ảnh
            // Hiển thị nút button thêm tin
            holder.btn_addStoryMain.setVisibility(View.VISIBLE);
            holder.btn_addStoryMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Chuyển sang trang CreateStoryActivity khi người dùng nhấn vào nút
                    Intent intent = new Intent(mContext, StoryActivity.class);
                    mContext.startActivity(intent);
                }
            });
            // Ẩn ImageView và TextView
            holder.img_storyUri.setVisibility(View.GONE);
            holder.txt_timeCreateStory.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_storyUri;
        Button btn_addStoryMain;
        TextView txt_timeCreateStory;
        ConstraintLayout layout_constraint_story;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btn_addStoryMain = itemView.findViewById(R.id.btn_addStoryMain);
            img_storyUri = itemView.findViewById(R.id.img_storyUri);
            txt_timeCreateStory = itemView.findViewById(R.id.txt_timeCreateStory);
            layout_constraint_story = itemView.findViewById(R.id.layout_constraint_story);
        }
    }

    //
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
                return days + " Ngày";
            } else if (hours > 0) {
                return hours + " Giờ";
            } else if (minutes > 0) {
                return minutes + " Phút";
            } else {
                return seconds + " Giây";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "N/A";
        }
    }
}