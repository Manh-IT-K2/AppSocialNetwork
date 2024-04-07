package com.example.frontend.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.activities.FollowsActivity;
import com.example.frontend.activities.MainActivity;
import com.example.frontend.fragments.FollowersFragment;
import com.example.frontend.request.Follows.RequestCreateFollows;
import com.example.frontend.request.Follows.RequestUpdateFollows;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Follows.GetQuantityResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.response.User.UserTrackingStatus;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Follows.FollowsViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.ViewHolder> {

    public static Context mContext;
    public List<UserTrackingStatus> userResponseList;
    public static FollowsViewModel followsViewModel;
    private LifecycleOwner lifecycleOwner;
    public static UserViewModel userViewModel;

    public FollowerAdapter(Context mContext, List<UserTrackingStatus> userResponseList,FollowsViewModel followsViewModel,LifecycleOwner lifecycleOwner,
                            UserViewModel userViewModel) {
        this.mContext = mContext;
        this.userResponseList = userResponseList;
        this.followsViewModel = followsViewModel;
        this.lifecycleOwner = lifecycleOwner;
        this.userViewModel = userViewModel;
    }
    @NonNull
    @Override
    public FollowerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_follower, parent, false);
        return new FollowerAdapter.ViewHolder(view,lifecycleOwner);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowerAdapter.ViewHolder holder, int position) {
        UserTrackingStatus userResponse = userResponseList.get(position);
        // Set thông tin bài đăng vào các view
        holder.idNameUser.setText(userResponse.getUsername());
        holder.nameUser.setText(userResponse.getName());
        holder.idFollows = userResponse.getId();
        holder.userResponse = userResponse;
        // Load hình ảnh từ URL bằng Glide
        Glide.with(mContext)
                .load(userResponse.getAvatarImg())
                .placeholder(R.drawable.logo) // Ảnh thay thế khi đang load
                .error(R.drawable.logo) // Ảnh thay thế khi có lỗi
                .into(holder.img_avtUser);
        switch (userResponse.getStatus()) {
            case "0":
                holder.btnRemove.setVisibility(View.INVISIBLE);
                break;
            case "1":
                setTextBtn(holder.btnRemove, "Following");
                break;
            case "2":
                setTextBtn(holder.btnRemove, "Follow");
                break;
        }
    }

    public void setTextBtn(Button btnFollow, String text){
        btnFollow.setText(text);
        // Get the reference to the Drawable you want to assign
        Drawable drawable;
        if (text.equals("Following"))
            drawable = mContext.getResources().getDrawable(R.drawable.custom_buttom_selected_follows);
        else
            drawable = mContext.getResources().getDrawable(R.drawable.custom_button_background);
        // Assign Drawable to Button
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            // For Android versions below API 16
            btnFollow.setBackgroundDrawable(drawable);
        } else {
            // For Android versions from API 16 and above
            btnFollow.setBackground(drawable);
        }
    }

    @Override
    public int getItemCount() {
        return userResponseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_avtUser;
        private TextView idNameUser, nameUser;
        private Button btnRemove;
        private String idFollows;
        private LifecycleOwner lifecycleOwner;
        private UserTrackingStatus userResponse;
        final Dialog dialog = new Dialog(mContext);
        private OnUpdateListener listener;
        public interface OnUpdateListener {
             void onUpdate(List<UserResponse> userResponseList);
        }

        public ViewHolder(@NonNull View itemView, LifecycleOwner lifecycleOwner) {
            super(itemView);
            img_avtUser = itemView.findViewById(R.id.img_avtUser);
            idNameUser = itemView.findViewById(R.id.idNameUser);
            nameUser = itemView.findViewById(R.id.nameUser);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            this.lifecycleOwner = lifecycleOwner;

            String userId = SharedPreferenceLocal.read(mContext.getApplicationContext(), "userId");
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String temp = btnRemove.getText().toString();
                    if(temp.equals("Following")){
                        openDialogFollowing(Gravity.BOTTOM,userId,userResponse.getId());
                    } else if(temp.equals("Follow")) {
                        RequestCreateFollows requestCreateFollows = new RequestCreateFollows(userId,userResponse.getId(),formatDate());
                        createFollow(requestCreateFollows,userId,userResponse.getId());
                    } else if (temp.equals("Remove")) {
                        openDialogFollowing(Gravity.BOTTOM, userId, userResponse.getId());
                    }
                }
            });
        }

        private String formatDate () {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date currentDate = new Date();
            return dateFormat.format(currentDate);
        }

        private void openDialogFollowing(int gravity,String userId,String userIdOther){

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.activity_dialog_custom_remove);
            Window window = dialog.getWindow();
            if (window == null) return;
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = gravity;
            window.setAttributes(windowAttributes);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            if (Gravity.BOTTOM == gravity){
                dialog.setCancelable(true);
            } else {
                dialog.setCancelable(false);
            }

            ImageView imageView = dialog.findViewById(R.id.img_avtUser);
            TextView name = dialog.findViewById(R.id.name);
            Button btnRemoveFollower = dialog.findViewById(R.id.btnRemoveFollower);
            Button btnCancel = dialog.findViewById(R.id.btnCancel);

            name.setText(userResponse.getName());
            // Load hình ảnh từ URL bằng Glide
            Glide.with(mContext)
                    .load(userResponse.getAvatarImg())
                    .placeholder(R.drawable.logo) // Ảnh thay thế khi đang load
                    .error(R.drawable.logo) // Ảnh thay thế khi có lỗi
                    .into(imageView);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnRemoveFollower.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteFollow(userResponse.getId(),userId,dialog);
                }
            });
            dialog.show();
        }
        private void deleteFollow(String idFollower,String idFollowing,Dialog dialog){
            followsViewModel.deleteFollows(idFollower,idFollowing).observe(lifecycleOwner, new Observer<ApiResponse<String>>() {
                @Override
                public void onChanged(ApiResponse<String> response) {
                    if(response.getMessage().equals("Delete Success!") && response.getStatus()){
                        handleQuantityUpdate(idFollowing,"follower",-1);
                        handleQuantityUpdate(idFollower,"following",-1);
                        dialog.dismiss();
                        FragmentTransaction fragmentTransaction = ((FollowsActivity) mContext).getSupportFragmentManager().beginTransaction();
                        FollowersFragment followersFragment = new FollowersFragment();
                        fragmentTransaction.attach(followersFragment).commit();
                    }
                }
            });;
        }

        private void handleQuantityUpdate(String idFollows, String type, int quantityChange) {
            followsViewModel.getQuantityFollows(idFollows).observe(lifecycleOwner, new Observer<ApiResponse<GetQuantityResponse>>() {
                @Override
                public void onChanged(ApiResponse<GetQuantityResponse> response) {
                    if(response.getMessage().equals("Success") && response.getStatus()){
                        GetQuantityResponse getQuantityResponse = response.getData();
                        int countFollower = getQuantityResponse.getQuantityFollower();
                        int countFollowing = getQuantityResponse.getQuantityFollowing();

                        if(type.equals("follower")) {
                            countFollower += quantityChange;
                        } else if (type.equals("following")) {
                            countFollowing += quantityChange;
                        }

                        RequestUpdateFollows requestUpdateFollowsByFollower = new RequestUpdateFollows(
                                getQuantityResponse.getId(), countFollower, countFollowing
                        );
                        handleUpdateFollow(requestUpdateFollowsByFollower);
                    }
                }
            });
        }

        private void handleUpdateFollow(RequestUpdateFollows requestUpdateFollows){
            followsViewModel.updateFollows(requestUpdateFollows);
        }

        public void setTextBtn(Button btnFollow, String text){
            btnFollow.setText(text);
            // Get the reference to the Drawable you want to assign
            Drawable drawable;
            if (text.equals("Following"))
                drawable = mContext.getResources().getDrawable(R.drawable.custom_buttom_selected_follows);
            else
                drawable = mContext.getResources().getDrawable(R.drawable.custom_button_background);
            // Assign Drawable to Button
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                // For Android versions below API 16
                btnFollow.setBackgroundDrawable(drawable);
            } else {
                // For Android versions from API 16 and above
                btnFollow.setBackground(drawable);
            }
        }

        private void createFollow (RequestCreateFollows requestCreateFollows,String userId,String userIdOther){
            followsViewModel.createFollows(requestCreateFollows).observe(lifecycleOwner, new Observer<ApiResponse<String>>() {
                @Override
                public void onChanged(ApiResponse<String> response) {
                    if(response.getStatus() && response.getMessage().equals("Success")){
                        //btnFollow.setText("Followed");
                        setTextBtn(btnRemove, "Following");
                        handleGetQuantityFollows(userId,"following");
                        handleGetQuantityFollows(userIdOther,"follower");
                    }
                }
            });
        }

        private void handleGetQuantityFollows(String idFollows, String type){
            handleQuantityUpdate(idFollows, type,1 );
        }
    }
}
