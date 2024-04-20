package com.example.frontend.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.fragments.LikeFragment;
import com.example.frontend.fragments.ProfileFragment;
import com.example.frontend.response.User.UserResponse;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import java.util.List;

public class LikedAdapter extends RecyclerView.Adapter<LikedAdapter.ViewHolder> {

    private static Context mContext;
    private static List<UserResponse> listUserLiked;

    public LikedAdapter(Context mContext, List<UserResponse> listUserLiked) {
        this.mContext = mContext;
        this.listUserLiked = listUserLiked;
    }

    @NonNull
    @Override
    public LikedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_suggest_me,parent,false);
        return new LikedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LikedAdapter.ViewHolder holder, int position) {
        UserResponse user = listUserLiked.get(position);
        holder.btnFollow.setVisibility(View.GONE);
        // set text in view
        holder.nameUser.setText(user.getName());
        holder.idNameUser.setText(user.getUsername());

        // load image user
        Glide.with(mContext)
                .load(user.getAvatarImg())
                .into(holder.img_avtUser);

    }

    @Override
    public int getItemCount() {
        return listUserLiked.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ShapeableImageView img_avtUser;
        private TextView idNameUser, nameUser;
        private Button btnFollow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // init view
            img_avtUser = itemView.findViewById(R.id.img_avtUser);
            idNameUser = itemView.findViewById(R.id.idNameUser);
            nameUser = itemView.findViewById(R.id.nameUser);
            btnFollow = itemView.findViewById(R.id.btnFollow);

            //
            idNameUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create an instance of the LikeFragment
                    ProfileFragment profileFragment = new ProfileFragment();

                    // Pass data (list of likes) to the LikeFragment
                    Bundle bundle = new Bundle();
                    String userId = listUserLiked.get(getAdapterPosition()).getId();
                    bundle.putString("userIdLiked", userId);
                    profileFragment.setArguments(bundle);
                    Log.e("prind", userId);

                    // Navigate to the LikeFragment by replacing the current fragment
                    FragmentTransaction transaction = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_layout_main, profileFragment); // Replace 'fragment_container' with your container ID
                    transaction.addToBackStack(null); // Add transaction to the back stack
                    transaction.commit();
                }
            });
        }
    }

}
