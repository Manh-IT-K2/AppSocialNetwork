package com.example.frontend.adapter;

import android.content.Context;
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

public class searchUserAdapter extends RecyclerView.Adapter<searchUserAdapter.MyHolder> {

    Context context;
    ArrayList<UserResponse> arrayList;
    LayoutInflater layoutInflater;

    public searchUserAdapter(Context context, ArrayList<UserResponse> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.search_user_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull searchUserAdapter.MyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
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
