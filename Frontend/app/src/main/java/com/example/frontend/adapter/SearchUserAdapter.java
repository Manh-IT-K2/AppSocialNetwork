package com.example.frontend.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.fragments.Fragment_searchHistory;
import com.example.frontend.response.User.UserResponse;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.MyHolder> {

    Context context;
    List<UserResponse> user_searchList;
    LayoutInflater layoutInflater;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public SearchUserAdapter(Context context, List<UserResponse> user_searchList) {

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

    public void onBindViewHolder(@NonNull SearchUserAdapter.MyHolder holder, int position) {
        holder.userName.setText(user_searchList.get(position).getUsername());
        if(user_searchList.get(position).getAvatarImg() != null)
            Glide.with(context)
                    .load(Uri.parse(user_searchList.get(position).getAvatarImg()))
                    .into(holder.avatar);

    }

    @Override
    public int getItemCount() {
        return user_searchList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView userName;
        CircleImageView avatar;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.txt_UserName);
            avatar = itemView.findViewById(R.id.imgAvatar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            itemClickListener.onItemClick(position);
                        }
                    }
                }
            });
        }


    }
}