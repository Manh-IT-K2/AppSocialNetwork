package com.example.frontend.adapter;

import static com.example.frontend.fragments.CommentFragment.edt_contentComment;
import static com.example.frontend.fragments.CommentFragment.getIconList;
import static com.example.frontend.fragments.CommentFragment.positionReplyComment;
import static com.example.frontend.fragments.CommentFragment.positionReplyCommentParent;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.request.Comment.RequestDeleteComment;
import com.example.frontend.request.Comment.RequestLikeComment;
import com.example.frontend.request.Post.RequestPostByUserId;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Comment.CommentResponse;
import com.example.frontend.response.Post.PostResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Comment.CommentViewModel;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    public interface OnReplyClickListener {
        void onReplyClick(int position);
    }
    private OnReplyClickListener replyClickListener;
    public void setOnReplyClickListener(OnReplyClickListener listener) {
        replyClickListener = listener;
    }

    private static LifecycleOwner lifecycleOwner;
    private Context mContext;
    private static List<CommentResponse> listComment;
    private boolean[] isReplyShown;

    public CommentAdapter(Context mContext, List<CommentResponse> listComment, LifecycleOwner lifecycleOwner) {
        this.mContext = mContext;
        this.listComment = listComment;
        this.lifecycleOwner = lifecycleOwner;
        this.isReplyShown = new boolean[listComment.size()];
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_comments, parent, false);
        return new CommentAdapter.ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        int positionParent = position;
        CommentResponse comment = listComment.get(position);
        holder.txt_idUserComment.setText(comment.getUser().getUsername());
        holder.setContentWithIcon(comment.getContent());
        holder.btn_deleteComment.setVisibility(View.INVISIBLE);

        String timeAgo = getTimeAgo(comment.getCreateAt());
        holder.txt_timeComment.setText(timeAgo);

        Glide.with(mContext)
                .load(comment.getUser().getAvatarImg())
                .into(holder.img_userComment);

        // set text for txt_countLike and set icon for btn_like
        if (comment.getLike() != null) {
            holder.txt_countLikeComment.setText(comment.getLike().size() + "");

            for (UserResponse user : comment.getLike()) {
                if (user.getId().contains("65e8a525714ccc3a3caa7f77")) {
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

        int positionReply = holder.getAdapterPosition();

        int replyCount = 0;
        if (comment.getReplyComment() != null) {
            replyCount = comment.getReplyComment().size();
            holder.txt_replyCount.setText("Xem " + replyCount + " câu trả lời khác");
        }else holder.txt_replyCount.setVisibility(View.INVISIBLE);

        // Check the display status of reply comments
        if (isReplyShown[positionReply]) {
            holder.list_replyComment.setVisibility(View.VISIBLE);
            ReplyCommentAdapter replyAdapter = new ReplyCommentAdapter(mContext, comment.getReplyComment(), (LifecycleOwner) mContext, comment.getId());
            holder.list_replyComment.setLayoutManager(new LinearLayoutManager(mContext));
            holder.list_replyComment.setAdapter(replyAdapter);
            holder.txt_hideComments.setVisibility(View.VISIBLE);

            //
            replyAdapter.setOnReplyClickListener(new ReplyCommentAdapter.OnReplyClickListener() {
                @Override
                public void onReplyClick(int position) {
                    String userName = comment.getReplyComment().get(position).getUser().getUsername();
                    edt_contentComment.setText("@"+userName + " ");
                    positionReplyCommentParent = positionParent;
                    positionReplyComment = position;
                    edt_contentComment.setSelection(edt_contentComment.getText().length());
                    edt_contentComment.requestFocus();
                    Log.e("dcmm",userName);
                }
            });
            replyAdapter.notifyDataSetChanged();
        } else {
            holder.list_replyComment.setVisibility(View.GONE);
            holder.txt_hideComments.setVisibility(View.GONE);
        }

        // Handle click events to show reply comments
        holder.txt_replyCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Nếu chưa hiển thị reply comments, hiển thị lên
                holder.list_replyComment.setVisibility(View.VISIBLE);
                holder.txt_hideComments.setVisibility(View.VISIBLE);
                isReplyShown[positionReply] = true;
                holder.txt_replyCount.setVisibility(View.INVISIBLE);
                notifyDataSetChanged();
            }
        });

        // Handle click events to hide reply comments
        holder.txt_hideComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Nếu đã hiển thị reply comments, ẩn đi
                holder.list_replyComment.setVisibility(View.GONE);
                holder.txt_hideComments.setVisibility(View.GONE);
                isReplyShown[positionReply] = false;
                holder.txt_hideComments.setVisibility(View.INVISIBLE);
                holder.txt_replyCount.setVisibility(View.VISIBLE);
                notifyDataSetChanged();
            }
        });

        //
        holder.btn_replyComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (replyClickListener != null) {
                    int position = holder.getAdapterPosition();
                    replyClickListener.onReplyClick(position);
                    Log.e("manhdeptrai", String.valueOf(position));
                }

            }
        });

        // Set long click listener for lnl_comment
        holder.lnl_comment.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Show btn_deleteComment
                Log.e("iddd", comment.getUser().getId());
                Log.e("iddd", "me: "+ SharedPreferenceLocal.read(mContext, "userId"));
                if(comment.getUser().getId().equals(SharedPreferenceLocal.read(mContext, "userId")) ){
                    holder.btn_deleteComment.setVisibility(View.VISIBLE);
                }

                // Hide btn_deleteComment after a delay (for example, 2 seconds)
                holder.itemView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.btn_deleteComment.setVisibility(View.GONE);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CommentViewModel commentViewModel = new CommentViewModel();
        private ShapeableImageView img_userComment;
        private RecyclerView list_replyComment;
        private TextView txt_idUserComment, txt_timeComment, txt_contentComment, btn_replyComment, txt_countLikeComment, txt_replyCount, txt_hideComments;
        private ImageButton btn_likeComment, btn_deleteComment;
        private LinearLayout lnl_comment;
        private CommentAdapter adapter;

        public ViewHolder(@NonNull View itemView, CommentAdapter adapter) {
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
            list_replyComment = itemView.findViewById(R.id.list_replyComment);
            txt_replyCount = itemView.findViewById(R.id.txt_replyCount);
            txt_hideComments = itemView.findViewById(R.id.txt_hideComments);
            lnl_comment = itemView.findViewById(R.id.lnl_comment);
            btn_deleteComment = itemView.findViewById(R.id.btn_deleteComment);

            btn_deleteComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    CommentResponse commentResponse = listComment.get(position);
                    String idPost = commentResponse.getIdPost();
                    String idComment = commentResponse.getId();
                    Boolean isReplyComment = false;

                    RequestDeleteComment deleteComment = new RequestDeleteComment(idPost,idComment,isReplyComment,"");
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
                        likeComment.setIdComment(comment.getId());
                        likeComment.setIdUser("65e8a525714ccc3a3caa7f77");
                        likeComment.setReplyComment(false);

                        commentViewModel.likeComment(likeComment);
                    }
                }
            });

        }

        //
        public void setContentWithIcon(String content) {
            List<Integer> iconList = getIconList();

            SpannableStringBuilder builder = new SpannableStringBuilder(content);
            Log.e("buider",String.valueOf(builder));
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
