package com.example.frontend.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.activities.StoryActivity;
import com.example.frontend.request.Story.RequestStoryByUserId;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private Context mContext;
    private List<RequestStoryByUserId> listStory;

    public StoryAdapter(Context mContext, List<RequestStoryByUserId> listStory) {
        this.mContext = mContext;
        this.listStory = listStory;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View headerView = LayoutInflater.from(mContext).inflate(R.layout.item_header_story, parent, false);
            return new HeaderViewHolder(headerView,mContext);
        } else {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.story_item, parent, false);
            return new ViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_HEADER) {
            // Handle header binding here if needed
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            // Bind header data or perform any necessary operations
        } else {
            // Handle regular item binding
            RequestStoryByUserId story = listStory.get(position - 1); // Adjust position for header
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.txt_nameUserStory.setText(story.getUserName());
            Glide.with(mContext)
                    .load(story.getAvtUser())
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(viewHolder.img_avtUser);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        // Add 1 to the item count to account for the header
        return listStory.size() + 1;
    }

    // Create a separate ViewHolder for the header if needed
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        // Add any additional views or functionality for the header here
        private ShapeableImageView btn_createStory;
        private Context mContext; // Add mContext variable to hold the context
        public HeaderViewHolder(@NonNull View itemView, Context context) {
                super(itemView);
                // Initialize header views here if needed
                this.mContext = context; // Initialize mContext
                btn_createStory = itemView.findViewById(R.id.btn_createStory);

                // Set OnClickListener to the button
                btn_createStory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch StoryActivity when the button is clicked
                        Intent intent = new Intent(mContext, StoryActivity.class);
                        mContext.startActivity(intent);
                    }
                });
            }
        }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_avtUser;
        private TextView txt_nameUserStory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_avtUser = itemView.findViewById(R.id.img_avtUser);
            txt_nameUserStory = itemView.findViewById(R.id.txt_nameUserStory);
        }
    }
}
