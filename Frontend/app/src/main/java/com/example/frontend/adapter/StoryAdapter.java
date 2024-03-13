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
import com.example.frontend.request.Story.RequestStoryByUserId;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder>{
    public Context mContext;
    public List<RequestStoryByUserId> listStory;

    public StoryAdapter(Context mContext, List<RequestStoryByUserId> listStory) {
        this.mContext = mContext;
        this.listStory = listStory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.story_item,parent,false);
        return new StoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestStoryByUserId story = listStory.get(position);

        // set text for userName
        holder.txt_nameUserStory.setText(story.getUserName());

        // set image for avtUser
        Glide.with(mContext)
                .load(story.getAvtUser())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(holder.img_avtUser);

    }

    @Override
    public int getItemCount() {
        return listStory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_avtUser;
        private TextView txt_nameUserStory;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_avtUser = itemView.findViewById(R.id.img_avtUser);
            txt_nameUserStory = itemView.findViewById(R.id.txt_nameUserStory);

        }
    }
}
