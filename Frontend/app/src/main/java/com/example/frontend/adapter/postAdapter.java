package com.example.frontend.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;

public class postAdapter extends RecyclerView.Adapter<postAdapter.ViewHolder>{
    public Context mContext;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
        }
    }
}
