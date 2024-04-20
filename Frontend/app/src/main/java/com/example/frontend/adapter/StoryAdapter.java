package com.example.frontend.adapter;

import static com.example.frontend.fragments.HomeFragment.recyclerViewStory;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.activities.CreateStoryActivity;
import com.example.frontend.activities.DetailStoryActivity;
import com.example.frontend.activities.MainActivity;
import com.example.frontend.activities.StoryActivity;
import com.example.frontend.request.Story.RequestCreateStory;
import com.example.frontend.request.Story.RequestStoryByUserId;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class StoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private Context mContext;
    private static List<RequestStoryByUserId> listStory;
    private List<List<RequestStoryByUserId>> groupedStoryList = new ArrayList<>();

    public StoryAdapter(Context mContext, List<RequestStoryByUserId> listStory) {
        this.mContext = mContext;
        this.listStory = listStory;
        groupStories();
    }

    // Phương thức để nhóm các story
    private void groupStories() {
        String userIdMain = SharedPreferenceLocal.read(mContext, "userId");
        List<RequestStoryByUserId> currentUserStories = new ArrayList<>();
        String currentUserId = null;

        // Lặp qua tất cả các story trong listStory
        for (RequestStoryByUserId story : listStory) {
            String userId = story.getUserId();
            Log.e("nhiiii", userId);
            // Nếu userId khác với userId của story trước đó, tạo một danh sách mới
            if (!userId.equals(currentUserId)) {
                // Lưu danh sách của người dùng trước đó vào groupedStoryList
                if (!currentUserStories.isEmpty()) {
                    groupedStoryList.add(currentUserStories);
                }
                // Tạo danh sách mới cho người dùng mới
                currentUserStories = new ArrayList<>();
                currentUserId = userId;
            }

            // Parse chuỗi thời gian thành đối tượng Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                Date createdDate = sdf.parse(story.getCreatedAt());

                // Lấy thời gian hiện tại
                Date currentDate = new Date();

                // Tính thời gian đã trôi qua
                long timeDifference = currentDate.getTime() - createdDate.getTime();
                long hoursSinceCreation = TimeUnit.MILLISECONDS.toHours(timeDifference);

                // Kiểm tra nếu story được tạo trong vòng 24 giờ
                if (hoursSinceCreation <= 24 && story.getStatus() == 0) {
                    // Thêm story vào danh sách của người dùng hiện tại
                    currentUserStories.add(story);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Đảm bảo rằng danh sách cuối cùng cũng được thêm vào groupedStoryList
        if (!currentUserStories.isEmpty()) {
            groupedStoryList.add(currentUserStories);
        }

        // Tìm index của userIdMain trong danh sách
        int userIdMainIndex = -1;
        for (int i = 0; i < groupedStoryList.size(); i++) {
            if (!groupedStoryList.get(i).isEmpty() && groupedStoryList.get(i).get(0).getUserId().equals(userIdMain)) {
                userIdMainIndex = i;
                break;
            }
        }

        // Đảm bảo rằng userIdMainIndex không âm và nhỏ hơn kích thước của groupedStoryList
        if (userIdMainIndex >= 0 && userIdMainIndex < groupedStoryList.size()) {
            // Di chuyển story của userIdMain vào vị trí đầu tiên
            List<RequestStoryByUserId> userIdMainStories = groupedStoryList.remove(userIdMainIndex);
            groupedStoryList.add(0, userIdMainStories);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View headerView = LayoutInflater.from(mContext).inflate(R.layout.item_header_story, parent, false);
            return new HeaderViewHolder(headerView, mContext);
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
            Glide.with(mContext).load(groupedStoryList.get(0).get(0).getAvtUser()).into(((HeaderViewHolder) holder).img_avtUserMain);
            if (!groupedStoryList.get(0).get(0).getImage().equals("")){
                headerViewHolder.img_avtUserMain.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.blue1)));
                headerViewHolder.img_add.setVisibility(View.GONE);
            }

            //
            headerViewHolder.img_avtUserMain.setOnTouchListener(new View.OnTouchListener() {
                private long startClickTime;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            startClickTime = System.currentTimeMillis();
                            break;
                        case MotionEvent.ACTION_UP:
                            long clickDuration = System.currentTimeMillis() - startClickTime;
                            if (clickDuration < ViewConfiguration.getLongPressTimeout()) {
                                // Người dùng thực hiện một cú chạm ngắn, xử lý sự kiện onClickListener
                                Gson gson = new Gson();
                                String jsonGroupedStoryList = gson.toJson(groupedStoryList.get(0));
                                Log.e("manhh",jsonGroupedStoryList);
                                Intent intent = new Intent(v.getContext(), DetailStoryActivity.class);
                                intent.putExtra("groupedStoryList", jsonGroupedStoryList);
                                v.getContext().startActivity(intent);
                            } else {
                                // Người dùng thực hiện một cú chạm dài, xử lý sự kiện onLongClickListener
                                // init dialog
                                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
                                bottomSheetDialog.setContentView(R.layout.dialog_bottom_create_story);

                                // init view
                                TextView txt_createNewStory = bottomSheetDialog.findViewById(R.id.txt_createNewStory);

                                // action
                                txt_createNewStory.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(mContext, StoryActivity.class);
                                        mContext.startActivity(intent);
                                    }
                                });

                                // show dialog
                                bottomSheetDialog.show();
                            }
                            break;
                    }
                    return true;
                }
            });

            // Bind header data or perform any necessary operations
        } else {
             //Handle regular item binding
            ViewHolder viewHolder = (ViewHolder) holder;
            int positons = viewHolder.getAdapterPosition();
            if (positons >= 0 && positons < groupedStoryList.size()) {
                viewHolder.txt_nameUserStory.setText(groupedStoryList.get(positons).get(0).getUserName());
                // Đặt lại màu của strokeColor nếu viewHolder.img_avtUser là ShapeableImageView
                String userId = SharedPreferenceLocal.read(mContext, "userId");
                if(groupedStoryList.get(positons).get(0).getSeen() != null){
                    for (int i = 0; i < groupedStoryList.get(positons).get(0).getSeen().size(); i++){
                        if (groupedStoryList.get(positons).get(0).getSeen().get(i).getId().equals(userId)){
                            viewHolder.img_avtUser.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.blackLite)));
                        }
                    }
                }
                //
                Glide.with(mContext).load(groupedStoryList.get(positons).get(0).getAvtUser()).placeholder(R.drawable.logo).error(R.drawable.logo).into(viewHolder.img_avtUser);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("manhh","jsonGroupedStoryList");
                        Gson gson = new Gson();
                        String jsonGroupedStoryList = gson.toJson(groupedStoryList.get(positons));
                        Log.e("manhh",jsonGroupedStoryList);
                        Intent intent = new Intent(v.getContext(), DetailStoryActivity.class);
                        intent.putExtra("groupedStoryList", jsonGroupedStoryList);
                        v.getContext().startActivity(intent);
                    }
                });
            } else {
                Log.e("StoryAdapter", "Invalid position: " + position);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        // Add 1 to the item count to account for the header
        Log.e("sizeManh",String.valueOf(groupedStoryList.size()));
        return groupedStoryList.size();
    }

    // Create a separate ViewHolder for the header if needed
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        // Add any additional views or functionality for the header here
        private ShapeableImageView img_avtUserMain;
        private ImageButton img_add;
        private Context mContext; // Add mContext variable to hold the context

        public HeaderViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            // Initialize header views here if needed
            this.mContext = context; // Initialize mContext
            img_avtUserMain = itemView.findViewById(R.id.img_avtUserMain);
            img_add = itemView.findViewById(R.id.img_add);

            //
            img_add.setOnClickListener(new View.OnClickListener() {
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
        private ShapeableImageView img_avtUser;
        private TextView txt_nameUserStory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_avtUser = itemView.findViewById(R.id.img_avtUser);
            txt_nameUserStory = itemView.findViewById(R.id.txt_nameUserStory);
        }
    }
}
