package com.example.frontend.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.activities.DetailStoryActivity;
import com.example.frontend.activities.MainActivity;
import com.example.frontend.request.Story.RequestStoryByUserId;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Story.StoryViewModel;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ViewerStoryAdapter extends RecyclerView.Adapter<ViewerStoryAdapter.ViewHolder> {
    private Context mContext;
    private List<UserResponse> mList;

    public ViewerStoryAdapter(Context context, List<UserResponse> mList) {
        this.mContext = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user_viewer_story, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //
        UserResponse user = mList.get(position);

        //
        holder.txt_idNameUser.setText(user.getUsername());
        holder.txt_nameUser.setText(user.getName());

        //
        Glide.with(mContext)
                .load(user.getAvatarImg())
                .into(holder.img_avtUsers);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ShapeableImageView img_avtUsers;
        private TextView txt_idNameUser, txt_nameUser;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_avtUsers = itemView.findViewById(R.id.img_avtUsers);
            txt_idNameUser = itemView.findViewById(R.id.txt_idNameUser);
            txt_nameUser = itemView.findViewById(R.id.txt_nameUser);
        }
    }
}
