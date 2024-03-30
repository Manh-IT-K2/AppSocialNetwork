package com.example.frontend.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.frontend.request.Follows.RequestUpdateFollows;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Follows.GetQuantityResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Follows.FollowsViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.gson.Gson;

import java.util.List;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.ViewHolder> {

    public static Context mContext;
    public List<UserResponse> userResponseList;
    public static FollowsViewModel followsViewModel;
    private LifecycleOwner lifecycleOwner;
    public static UserViewModel userViewModel;

    public FollowerAdapter(Context mContext, List<UserResponse> userResponseList,FollowsViewModel followsViewModel,LifecycleOwner lifecycleOwner,
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
        UserResponse userResponse = userResponseList.get(position);
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
        private UserResponse userResponse;
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
                    openDialogFollowing(Gravity.BOTTOM,userId,userResponse.getId());
                }
            });
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

    }
}
