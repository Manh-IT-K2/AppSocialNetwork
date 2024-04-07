package com.example.frontend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.response.Comment.CommentResponse;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class IconAdapter extends RecyclerView.Adapter<IconAdapter.ViewHolder> {

    private static IconClickListener iconClickListener;

    public interface IconClickListener {
        void onIconClick(int iconResId);
    }

    public void setIconClickListener(IconClickListener iconClickListener) {
        this.iconClickListener = iconClickListener;
    }

    private Context mContext;
    private List<Integer> listIcon;

    public IconAdapter(Context mContext, List<Integer> listIcon) {
        this.mContext = mContext;
        this.listIcon = listIcon;
    }

    @NonNull
    @Override
    public IconAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_icon_comment,parent,false);
        return new IconAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IconAdapter.ViewHolder holder, int position) {
        Integer img = listIcon.get(position);
        holder.img_iconComment.setImageResource(img);
        //
        holder.img_iconComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iconClickListener != null) {
                    iconClickListener.onIconClick(img);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listIcon.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

       private ImageView img_iconComment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // init view
            img_iconComment = itemView.findViewById(R.id.img_iconComment);

        }
    }

}
