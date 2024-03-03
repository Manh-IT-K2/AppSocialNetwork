package com.example.frontend.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;

public class storyAdapter extends RecyclerView.Adapter<storyAdapter.ViewHolder>{
    public Context mContext;
    @NonNull
    @Override
    public storyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull storyAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_story;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_story = itemView.findViewById(R.id.img_story);

        }
    }
}
