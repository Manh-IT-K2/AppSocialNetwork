package com.example.frontend.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.frontend.response.User.GetAllUserByFollowsResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Follows.FollowsViewModel;
import com.example.frontend.viewModel.User.UserViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SuggestedMeAdapter extends RecyclerView.Adapter<SuggestedMeAdapter.ViewHolder> {

    public static Context mContext;
    public List<GetAllUserByFollowsResponse> userResponseList;
    public static FollowsViewModel followsViewModel;
    private LifecycleOwner lifecycleOwner;
    public static UserViewModel userViewModel;

    public SuggestedMeAdapter(Context mContext, List<GetAllUserByFollowsResponse> userResponseList,FollowsViewModel followsViewModel,LifecycleOwner lifecycleOwner,
                              UserViewModel userViewModel) {
        this.mContext = mContext;
        this.userResponseList = userResponseList;
        this.followsViewModel = followsViewModel;
        this.lifecycleOwner = lifecycleOwner;
        this.userViewModel = userViewModel;
    }

    @NonNull
    @Override
    public SuggestedMeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_suggest_me, parent, false);
        return new SuggestedMeAdapter.ViewHolder(view,lifecycleOwner);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestedMeAdapter.ViewHolder holder, int position) {
        GetAllUserByFollowsResponse userResponse = userResponseList.get(position);
        // Set thông tin bài đăng vào các view
        holder.idNameUser.setText(userResponse.getUsername());
        holder.nameUser.setText(userResponse.getName());
        holder.idFollows = userResponse.getId();
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
        private String idFollows;
        private LifecycleOwner lifecycleOwner;
        public ViewHolder(@NonNull View itemView, LifecycleOwner lifecycleOwner) {
            super(itemView);
            img_avtUser = itemView.findViewById(R.id.img_avtUser);
            idNameUser = itemView.findViewById(R.id.idNameUser);
            nameUser = itemView.findViewById(R.id.nameUser);
            btnFollow = itemView.findViewById(R.id.btnFollow);
            this.lifecycleOwner = lifecycleOwner;

            String userId = SharedPreferenceLocal.read(mContext.getApplicationContext(), "userId");

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date currentDate = new Date();
            String formattedDate = dateFormat.format(currentDate);
            btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestCreateFollows requestCreateFollows = new RequestCreateFollows(userId,idFollows,formattedDate);
                    followsViewModel.createFollows(requestCreateFollows).observe(lifecycleOwner, new Observer<ApiResponse<String>>() {
                        @Override
                        public void onChanged(ApiResponse<String> response) {
                            if(response.getStatus() && response.getMessage().equals("Success")){
                                setTextBtn(btnFollow,"Đã theo dõi");
                                handleGetQuantityFollows(userId,"following");
                                handleGetQuantityFollows(idFollows,"follower");
                            }
                        }
                    });
                }
            });
        }
        public void setTextBtn(Button btnFollow,String text){
            btnFollow.setText(text);
            // Get the reference to the Drawable you want to assign
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.custom_buttom_selected_follows);

            // Assign Drawable to Button
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                // For Android versions below API 16
                btnFollow.setBackgroundDrawable(drawable);
            } else {
                // For Android versions from API 16 and above
                btnFollow.setBackground(drawable);
            }
        }

        private void handleGetQuantityFollows(String idFollows,String type){
            followsViewModel.getQuantityFollows(idFollows).observe(lifecycleOwner, new Observer<ApiResponse<GetQuantityResponse>>() {
                @Override
                public void onChanged(ApiResponse<GetQuantityResponse> response) {
                    if(response.getMessage().equals("Success") && response.getStatus()){
                        GetQuantityResponse getQuantityResponse = response.getData();
                        // update follower của người được theo dõi
                        if(type.equals("follower")){
                            int countFollower = getQuantityResponse.getQuantityFollower() + 1;
                            RequestUpdateFollows requestUpdateFollowsByFollower = new RequestUpdateFollows(
                                    getQuantityResponse.getId(), countFollower,getQuantityResponse.getQuantityFollowing()
                            );
                            handleUpdateFollow(requestUpdateFollowsByFollower);
                        } // update thông tin của người dùng hiện tại ( tăng số người đang theo dõi)
                        else if (type.equals("following")) {
                            int countFollowing = getQuantityResponse.getQuantityFollowing() + 1;
                            RequestUpdateFollows requestUpdateFollowsByFollower = new RequestUpdateFollows(
                                    getQuantityResponse.getId(), getQuantityResponse.getQuantityFollower(),countFollowing
                            );
                            handleUpdateFollow(requestUpdateFollowsByFollower);
                        }
                    }
                }
            });
        }
        private void handleUpdateFollow(RequestUpdateFollows requestUpdateFollows){
            followsViewModel.updateFollows(requestUpdateFollows);
        }
    }
}
