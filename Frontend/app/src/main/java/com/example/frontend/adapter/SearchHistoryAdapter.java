package com.example.frontend.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.response.Search.SearchHistoryResponse;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.MyHolder> {

    Context context;
    ArrayList<SearchHistoryResponse> searchHistoryResponseList;
    LayoutInflater layoutInflater;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClickToRemove(int position);
        void onItemClickToSearch(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        listener = clickListener;
    }

    public SearchHistoryAdapter(Context context, ArrayList<SearchHistoryResponse> searchHistoryResponseList) {
        this.context = context;
        this.searchHistoryResponseList = searchHistoryResponseList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = layoutInflater.inflate(R.layout.search_history_item_text, parent, false);
        }
        else {
            view = layoutInflater.inflate(R.layout.search_history_item_account, parent, false);
        }
        return new MyHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistoryAdapter.MyHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            holder.text.setText(searchHistoryResponseList.get(position).getText());
        }
        else {
            holder.userName.setText(searchHistoryResponseList.get(position).getText());
            holder.name.setText(searchHistoryResponseList.get(position).getName());
            if(searchHistoryResponseList.get(position).getAvatar() != null)
                Glide.with(context)
                        .load(Uri.parse(searchHistoryResponseList.get(position).getAvatar()))
                        .into(holder.avatar);
        }
    }

    @Override
    public int getItemCount() {
        return searchHistoryResponseList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return !searchHistoryResponseList.get(position).getAccount() ? 0 : 1;
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView name;
        CircleImageView avatar;
        TextView text;
        ImageButton imgButtonDelete;

        public MyHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            userName = itemView.findViewById(R.id.txt_UserName);
            name = itemView.findViewById(R.id.txt_Name);
            avatar = itemView.findViewById(R.id.imgAvatar);
            text = itemView.findViewById(R.id.txt_Text);
            imgButtonDelete = itemView.findViewById(R.id.delete_item);

            imgButtonDelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    listener.onItemClickToRemove(getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    listener.onItemClickToSearch(getAdapterPosition());
                }
            });


        }
    }
}
