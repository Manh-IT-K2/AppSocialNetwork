package com.example.frontend.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.response.User.UserResponse;

import java.util.ArrayList;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.MyHolder> {

    Context context;
    ArrayList<UserResponse> user_searchList;
    LayoutInflater layoutInflater;

<<<<<<< Updated upstream
    public searchUserAdapter(Context context, ArrayList<UserResponse> user_searchList) {
=======
    public SearchUserAdapter(Context context, ArrayList<UserResponse> arrayList) {
>>>>>>> Stashed changes
        this.context = context;
        this.user_searchList = user_searchList;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.search_user_item, parent, false);
        return new MyHolder(view);
    }

    @Override
<<<<<<< Updated upstream
    public void onBindViewHolder(@NonNull searchUserAdapter.MyHolder holder, int position) {
        holder.userName.setText(user_searchList.get(position).getUsername());
        holder.avatar.setImageURI(Uri.parse(user_searchList.get(position).getAvatarImg()));
=======
    public void onBindViewHolder(@NonNull SearchUserAdapter.MyHolder holder, int position) {

>>>>>>> Stashed changes
    }

    @Override
    public int getItemCount() {
        return user_searchList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView userName;
        ImageView avatar;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.txt_UserName);
            avatar = itemView.findViewById(R.id.imgAvatar);
        }
    }
}
