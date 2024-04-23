package com.example.frontend.adapter;

import static com.example.frontend.fragments.CommentFragment.getIconList;
import static com.example.frontend.fragments.CommentFragment.positionReplyComment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.request.Comment.RequestDeleteComment;
import com.example.frontend.request.Comment.RequestLikeComment;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Comment.CommentResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Comment.CommentViewModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class ReplyCommentAdapter extends RecyclerView.Adapter<ReplyCommentAdapter.ViewHolder> {

    public interface OnReplyClickListener {
        void onReplyClick(int position);
    }
    private static OnReplyClickListener replyClickListener;
    public void setOnReplyClickListener(OnReplyClickListener listener) {
        replyClickListener = listener;
    }

    private static Context mContext;
    private static List<CommentResponse> listComment;

    private static LifecycleOwner lifecycleOwner;
    private static String idCommentParent;
    public ReplyCommentAdapter(Context mContext, List<CommentResponse> listComment, LifecycleOwner lifecycleOwner, String idCommentParent) {
        this.mContext = mContext;
        this.listComment = listComment;
        this.lifecycleOwner = lifecycleOwner;
        this.idCommentParent = idCommentParent;
    }

    @NonNull
    @Override
    public ReplyCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_reply_comments,parent,false);
        return new ReplyCommentAdapter.ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyCommentAdapter.ViewHolder holder, int position) {
        CommentResponse comment = listComment.get(position);
        holder.txt_idUserComment.setText(comment.getUser().getUsername());
        holder.btn_deleteReplyComment.setVisibility(View.INVISIBLE);

        // set text for txt_countLike and set icon for btn_like
        if (comment.getLike() != null) {
            holder.txt_countLikeComment.setText(comment.getLike().size() + "");

            for (UserResponse user : comment.getLike()) {
                if (user.getId().contains(SharedPreferenceLocal.read(mContext, "userId"))) {
                    comment.setLike(true);
                    break;
                }
            }

            if ( comment.isLike()) {
                holder.btn_likeComment.setImageResource(R.drawable.icon_liked);
            } else {
                holder.btn_likeComment.setImageResource(R.drawable.icon_favorite); // Thay bằng icon khác nếu không được like
            }
            holder.txt_countLikeComment.setText(comment.getLike().size()+"");
        }else holder.txt_countLikeComment.setText("0");

        holder.setContentWithIcon(comment.getContent());

        String timeAgo = getTimeAgo(comment.getCreateAt());
        holder.txt_timeComment.setText(timeAgo);

        Glide.with(mContext)
                .load(comment.getUser().getAvatarImg() != null ? comment.getUser().getAvatarImg() : R.drawable.echobond)
                .centerCrop().into(holder.img_userComment);

        //
        holder.btn_replyComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (replyClickListener != null){
                    int position = holder.getAdapterPosition();
                    replyClickListener.onReplyClick(position);
                    Log.e("manhdeptrai", String.valueOf(position));
                }

            }
        });

        // Set long click listener for lnl_comment
        holder.lnl_replyComment.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Show btn_deleteComment
                holder.btn_deleteReplyComment.setVisibility(View.VISIBLE);

                // Hide btn_deleteComment after a delay (for example, 2 seconds)
                holder.itemView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.btn_deleteReplyComment.setVisibility(View.GONE);
                    }
                }, 10000); // 2 seconds delay

                return true; // Consume the long click event
            }
        });

    }


    @Override
    public int getItemCount() {
        return listComment.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private ShapeableImageView img_userComment;
        private TextView txt_idUserComment, txt_timeComment, txt_contentComment, btn_replyComment, txt_countLikeComment;
        private ImageButton btn_likeComment, btn_deleteReplyComment;
        private LinearLayout lnl_replyComment;
        private CommentViewModel commentViewModel = new CommentViewModel();
        private ReplyCommentAdapter adapter;

        public ViewHolder(@NonNull View itemView, ReplyCommentAdapter adapter) {
            super(itemView);
            this.adapter = adapter;

            // init view
            img_userComment = itemView.findViewById(R.id.img_userComment);
            txt_idUserComment = itemView.findViewById(R.id.txt_idUserComment);
            txt_timeComment = itemView.findViewById(R.id.txt_timeComment);
            txt_contentComment = itemView.findViewById(R.id.txt_contentComment);
            btn_replyComment = itemView.findViewById(R.id.btn_replyComment);
            txt_countLikeComment = itemView.findViewById(R.id.txt_countLikeComment);
            btn_likeComment = itemView.findViewById(R.id.btn_likeComment);
            lnl_replyComment = itemView.findViewById(R.id.lnl_replyComment);
            btn_deleteReplyComment = itemView.findViewById(R.id.btn_deleteReplyComment);

            //
            btn_replyComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            //
            btn_deleteReplyComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Log.e("position" ,String.valueOf(position));
                    CommentResponse commentResponse = listComment.get(position);
                    Gson gson = new Gson();
                    String h = gson.toJson(commentResponse);
                    Log.e("position" ,h);
                    String idPost = commentResponse.getIdPost();
                    String idComment = idCommentParent;
                    Boolean isReplyComment = true;
                    String idReplyComment = commentResponse.getId();;

                    RequestDeleteComment deleteComment = new RequestDeleteComment(idPost,idComment,isReplyComment,idReplyComment);
                    commentViewModel.deleteComment(deleteComment).observe(lifecycleOwner, new Observer<ApiResponse<String>>() {
                        @Override
                        public void onChanged(ApiResponse<String> response) {
                            listComment.remove(position);
                            adapter.updateData();
                        }
                    });
                }
            });

            btn_likeComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        CommentResponse comment = listComment.get(position);

                        int countLike =  Integer.parseInt(txt_countLikeComment.getText().toString());

                        if (!comment.isLike()) {
                            btn_likeComment.setImageResource(R.drawable.icon_liked);
                            comment.setLike(true);
                            countLike++;
                        } else {
                            btn_likeComment.setImageResource(R.drawable.icon_favorite);
                            comment.setLike(false);
                            countLike--;
                        }

                        txt_countLikeComment.setText(countLike + "");

                        RequestLikeComment likeComment = new RequestLikeComment();
                        likeComment.setIdComment(idCommentParent);
                        likeComment.setIdUser(SharedPreferenceLocal.read(adapter.mContext, "userId"));
                        likeComment.setReplyComment(true);
                        likeComment.setIdReplyComment(comment.getId());

                        commentViewModel.likeComment(likeComment);
                    }
                }
            });
        }

        public void setContentWithIcon(String content) {
            SpannableStringBuilder builder = new SpannableStringBuilder(content);
            List<Integer> iconList = getIconList();

            // Check if the username begins with "@".
            if (content.startsWith("@")) {
                // Find the location of the first space after "@" in the username
                int spaceIndex =content.indexOf(" ");
                if (spaceIndex != -1) {
                    //If there is a space, apply color to the part from "@" to the space
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.blue1));
                    builder.setSpan(colorSpan, 0, spaceIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

            // Duyệt qua danh sách icon
            for (Integer iconResId : iconList) {
                String iconName = itemView.getContext().getResources().getResourceEntryName(iconResId);
                int index = content.indexOf(iconName);

                // Nếu tìm thấy biểu tượng trong chuỗi
                while (index != -1) {
                    // Loại bỏ tên biểu tượng khỏi chuỗi
                    //content = content.substring(0, index) + content.substring(index + iconName.length());

                    // Thêm biểu tượng vào chuỗi với ImageSpan
                    Drawable iconDrawable = ContextCompat.getDrawable(itemView.getContext(), iconResId);
                    if (iconDrawable != null) {
                        iconDrawable.setBounds(0, 0, iconDrawable.getIntrinsicWidth(), iconDrawable.getIntrinsicHeight());
                        ImageSpan imageSpan = new ImageSpan(iconDrawable, ImageSpan.ALIGN_BOTTOM);
                        builder.setSpan(imageSpan, index, index+iconName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    // Tìm vị trí của biểu tượng tiếp theo
                    index = content.indexOf(iconName, index + 1);
                }
            }

            // Hiển thị chuỗi đã được định dạng (với tên biểu tượng đã được loại bỏ)
            txt_contentComment.setText(builder);
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
                return hours + " giờ";
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
    public void updateData() {
        notifyDataSetChanged();
    }
}
