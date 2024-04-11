package com.example.frontend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.request.Notification.Notification;
import com.example.frontend.request.Notification.NotificationResponse;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    Context mContext;
    List<NotificationResponse> mNotification;

    public NotificationAdapter(Context mContext, List<NotificationResponse> mNotification) {
        this.mContext = mContext;
        this.mNotification = mNotification;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationResponse notification = mNotification.get(position);

        holder.text.setText(notification.getText());
        holder.time.setText(getTimeAgo(notification.getCreateAt()));
        Glide.with(mContext)
                .load(notification.getUser().getAvatarImg())
                .into(holder.image_profile);

        holder.username.setText(notification.getUser().getUsername());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notification.isLikePost()){
                    Toast.makeText(mContext, "Like", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mNotification.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image_profile;
        TextView username, text, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_profile = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.username);
            text = itemView.findViewById(R.id.text);
            time = itemView.findViewById(R.id.txt_time);
        }
    }

    //
    private String getTimeAgo(String createdAt) {
        try {
            // Parse chuỗi thời gian thành đối tượng Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date createdDate = sdf.parse(createdAt);

            // Lấy thời gian hiện tại
            Date currentDate = new Date();

            // Tính thời gian đã trôi qua
            long timeDifference = currentDate.getTime() - createdDate.getTime();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDifference);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifference);
            long hours = TimeUnit.MILLISECONDS.toHours(timeDifference);
            long days = TimeUnit.MILLISECONDS.toDays(timeDifference);

            // Trả về chuỗi biểu thị thời gian đã trôi qua
            if (days > 0) {
                return days + " ngày";
            } else if (hours > 0) {
                return hours + " Giờ";
            } else if (minutes > 0) {
                return minutes + " phút";
            } else {
                return seconds + " giây";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "N/A";
        }
    }

    //
    public void updateData() {
        notifyDataSetChanged();
    }
}
