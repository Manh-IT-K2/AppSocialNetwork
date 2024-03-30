package com.example.frontend.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.request.Follows.RequestCreateFollows;
import com.example.frontend.request.Follows.RequestUpdateFollows;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Follows.GetQuantityResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Follows.FollowsViewModel;
import com.example.frontend.viewModel.User.UserViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {

    public static Context mContext;
    public List<UserResponse> userResponseList;
    public static FollowsViewModel followsViewModel;
    private LifecycleOwner lifecycleOwner;
    public static UserViewModel userViewModel;

    public FollowingAdapter(Context mContext, List<UserResponse> userResponseList,FollowsViewModel followsViewModel,LifecycleOwner lifecycleOwner,
                              UserViewModel userViewModel) {
        this.mContext = mContext;
        this.userResponseList = userResponseList;
        this.followsViewModel = followsViewModel;
        this.lifecycleOwner = lifecycleOwner;
        this.userViewModel = userViewModel;
    }
    @NonNull
    @Override
    public FollowingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_following, parent, false);
        return new FollowingAdapter.ViewHolder(view,lifecycleOwner);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingAdapter.ViewHolder holder, int position) {
        UserResponse userResponse = userResponseList.get(position);
        // Set thông tin bài đăng vào các view
        holder.idNameUser.setText(userResponse.getUsername());
        holder.nameUser.setText(userResponse.getName());
        holder.userResponse = userResponse;
        // Load hình ảnh từ URL bằng Glide
        Glide.with(mContext)
                .load(userResponse.getAvatarImg())
                .placeholder(R.drawable.logo) // Ảnh thay thế khi đang load
                .error(R.drawable.logo) // Ảnh thay thế khi có lỗi
                .into(holder.img_avtUser);
    }

    @Override
    public int getItemCount() {
        return userResponseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_avtUser;
        private TextView idNameUser, nameUser;
        private Button btnFollow;
        private UserResponse userResponse;
        private LifecycleOwner lifecycleOwner;
        final Dialog dialog = new Dialog(mContext);
        public ViewHolder(@NonNull View itemView, LifecycleOwner lifecycleOwner) {
            super(itemView);
            img_avtUser = itemView.findViewById(R.id.img_avtUser);
            idNameUser = itemView.findViewById(R.id.idNameUser);
            nameUser = itemView.findViewById(R.id.nameUser);
            btnFollow = itemView.findViewById(R.id.btnFollow);
            this.lifecycleOwner = lifecycleOwner;

            String userId = SharedPreferenceLocal.read(mContext.getApplicationContext(), "userId");
            btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String temp = btnFollow.getText().toString();
                    if(temp.equals("Following")){
                        openDialogFollowing(Gravity.BOTTOM,userId,userResponse.getId());
                    } else if(temp.equals("Follow")) {
                        RequestCreateFollows requestCreateFollows = new RequestCreateFollows(userId,userResponse.getId(),formatDate());
                        createFollow(requestCreateFollows,userId,userResponse.getId());
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
            dialog.setContentView(R.layout.activity_dialog_custom);
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
            Button btnUnFollow = dialog.findViewById(R.id.btnUnFollow);
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

            btnUnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteFollow(userId,userIdOther,dialog);
                }
            });
            dialog.show();
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

        private void handleUpdateQuantityFollows(String idFollows, String type){
            handleQuantityUpdate(idFollows, type, -1 );
        }

        private void handleGetQuantityFollows(String idFollows, String type){
            handleQuantityUpdate(idFollows, type,1 );
        }
        private void handleUpdateFollow(RequestUpdateFollows requestUpdateFollows){
            followsViewModel.updateFollows(requestUpdateFollows);
        }
        private void deleteFollow(String idFollower,String idFollowing,Dialog dialog){
            followsViewModel.deleteFollows(idFollower,idFollowing).observe(lifecycleOwner, new Observer<ApiResponse<String>>() {
                @Override
                public void onChanged(ApiResponse<String> response) {
                    if(response.getMessage().equals("Delete Success!") && response.getStatus()){
                        handleUpdateQuantityFollows(idFollower,"following");
                        handleUpdateQuantityFollows(idFollowing,"follower");
                        btnFollow.setText("Follow");
                        dialog.dismiss();
                    }
                }
            });;
        }

        private void createFollow (RequestCreateFollows requestCreateFollows,String userId,String userIdOther){
            followsViewModel.createFollows(requestCreateFollows).observe(lifecycleOwner, new Observer<ApiResponse<String>>() {
                @Override
                public void onChanged(ApiResponse<String> response) {
                    if(response.getStatus() && response.getMessage().equals("Success")){
                        btnFollow.setText("Followed");
                        handleGetQuantityFollows(userId,"following");
                        handleGetQuantityFollows(userIdOther,"follower");
                    }
                }
            });
        }
    }
}
