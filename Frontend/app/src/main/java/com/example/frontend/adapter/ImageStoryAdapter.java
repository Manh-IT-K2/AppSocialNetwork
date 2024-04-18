package com.example.frontend.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.activities.CreateStoryActivity;

import java.util.List;

public class ImageStoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Uri> listImage;

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    public ImageStoryAdapter(Context context, List<Uri> listImage) {
        this.context = context;
        this.listImage = listImage;
    }

    @NonNull
    @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View headerView = LayoutInflater.from(context).inflate(R.layout.item_header_create_story, parent, false);
            return new HeaderViewHolderStory(headerView, context);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.story_item_store, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == VIEW_TYPE_HEADER) {
            // Handle header binding here if needed
            ImageStoryAdapter.HeaderViewHolderStory headerViewHolder = (ImageStoryAdapter.HeaderViewHolderStory) holder;
            // Bind header data or perform any necessary operations
        } else {
            // Handle regular item binding
            // Adjust position for header
            ImageStoryAdapter.ViewHolder viewHolder = (ImageStoryAdapter.ViewHolder) holder;
            Uri imageUri = listImage.get(position - 1);
            Glide.with(context)
                    .load(imageUri)
                    .into(viewHolder.img_loadStoryStore);

            //
            ((ViewHolder) holder).img_loadStoryStore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri imageUri = listImage.get(holder.getAdapterPosition() - 1);
                    Intent intent = new Intent(context, CreateStoryActivity.class);
                    intent.putExtra("imageUri", imageUri.toString());
                    context.startActivity(intent);
                }
            });

        }
    }


    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return listImage.size() + 1;
    }

    // Create a separate ViewHolder for the header if needed
    public static class HeaderViewHolderStory extends RecyclerView.ViewHolder {
        private ImageView btn_cameraStory;
        private Context mContext; // Add mContext variable to hold the context

        public HeaderViewHolderStory(@NonNull View itemView, Context context) {
            super(itemView);
            this.mContext = context; // Initialize mContext
            btn_cameraStory = itemView.findViewById(R.id.btn_cameraStory);

            // Set OnClickListener to the button

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_loadStoryStore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_loadStoryStore = itemView.findViewById(R.id.img_loadStoryStore);
        }
    }
}
