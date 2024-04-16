package com.example.frontend.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.adapter.CommentAdapter;
import com.example.frontend.adapter.IconAdapter;
import com.example.frontend.request.Comment.RequestCreateComment;
import com.example.frontend.request.Notification.Notification;
import com.example.frontend.request.Post.RequestPostByUserId;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Comment.CommentResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.service.NotificationService;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Comment.CommentViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CommentFragment extends Dialog implements IconAdapter.IconClickListener {

    private TextView btn_drag;
    private LinearLayout linearLayout;
    private RecyclerView list_comment, list_iconComment;
    private ShapeableImageView img_user;
    public static EditText edt_contentComment;
    private CommentAdapter commentAdapter;
    private IconAdapter iconAdapter;
    private CommentViewModel commentViewModel;
    private UserViewModel userViewModel;
    private List<CommentResponse> listComment;
    private List<Integer> listIcon;
    private Context context;
    private String idPost;
    private String idComment;
    private int positionComment = -1;
    public static int positionReplyComment = -1;
    public static int positionReplyCommentParent = -1;
    private ImageView btn_createComment, btn_sendGifComment;
    private static List<String> listIconsChoosed = new ArrayList<>();
    private String userId;
    private String tokenFCM;

    public CommentFragment(Context context, String idPost, String idComment, String userId, String tokenFCM) {
        super(context);
        this.context = context;
        this.idPost = idPost;
        this.idComment = idComment;
        this.userId = userId;
        this.tokenFCM = tokenFCM;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_comment);

        // set the properties of Dialog
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);

        // init view
        btn_drag = findViewById(R.id.btn_drag);
        linearLayout = findViewById(R.id.linearLayout);
        list_comment = findViewById(R.id.list_comment);
        img_user = findViewById(R.id.img_user);
        list_iconComment = findViewById(R.id.list_iconComment);
        edt_contentComment = findViewById(R.id.edt_contentComment);
        btn_createComment = findViewById(R.id.btn_createComment);
        btn_sendGifComment = findViewById(R.id.btn_sendGifComment);

        // init recycler view
        list_comment.setLayoutManager(new LinearLayoutManager(getContext()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        list_iconComment.setLayoutManager(layoutManager);

        //
        listIcon = getIconList();
        iconAdapter = new IconAdapter(context, listIcon);
        list_iconComment.setAdapter(iconAdapter);
        iconAdapter.setIconClickListener(this);

        // action click btn_drag
        btn_drag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        // Create ViewModel instance
        commentViewModel = new ViewModelProvider((FragmentActivity) context).get(CommentViewModel.class);
        userViewModel = new ViewModelProvider((FragmentActivity) context).get(UserViewModel.class);

        // Observe LiveData directly
        commentViewModel.getListCommentByIdPost(idPost, idComment).observe((FragmentActivity) context, new Observer<ApiResponse<List<CommentResponse>>>() {
            @Override
            public void onChanged(ApiResponse<List<CommentResponse>> response) {
                Gson gson = new Gson();
                String json = gson.toJson(response);
                Log.e("listComment", json);
                if (response.getData() != null) {
                    listComment = response.getData();
                    commentAdapter = new CommentAdapter(context, listComment, (LifecycleOwner) context);
                    list_comment.setAdapter(commentAdapter);
                    commentAdapter.setOnReplyClickListener(new CommentAdapter.OnReplyClickListener() {
                        @Override
                        public void onReplyClick(int position) {
                            String userName = listComment.get(position).getUser().getUsername();
                            edt_contentComment.setText("@"+userName + " ");
                            edt_contentComment.requestFocus();
                            positionComment = position;
                            edt_contentComment.setSelection(edt_contentComment.getText().length());
                            Log.e("dcmm",String.valueOf(positionComment));
                        }
                    });
                } else {
                    // Handle when there is no data or there's an error
                }
            }
        });

        // create comment
        btn_createComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contentComment = edt_contentComment.getText().toString();;
                if(listIconsChoosed.size() > 0){
                    for(int i = 0; i < listIconsChoosed.size(); i++){
                        String placeholder = "-" + i + "-";
                        contentComment = contentComment.replace(placeholder, listIconsChoosed.get(i));
                    }
                }

                String idComment = "";
                String idUserReply = "";
                boolean isReplyComment = false;

                Notification notification = new Notification();
                notification.setPostId(idPost);
                notification.setUserId(SharedPreferenceLocal.read(getContext(), "userId"));
                String userName = SharedPreferenceLocal.read(getContext(), "userName");

                if (positionComment != -1 ){
                    idComment = listComment.get(positionComment).getId();
                    isReplyComment = true;
                    idUserReply = listComment.get(positionComment).getUser().getId();
                    Log.e("relly",idComment);
                }else if (positionReplyComment != -1 && positionReplyCommentParent != -1){
                    idComment = listComment.get(positionReplyCommentParent).getId();
                    isReplyComment = true;
                    idUserReply = listComment.get(positionReplyCommentParent).getReplyComment().get(positionReplyComment).getUser().getId();
                }

                if(isReplyComment){
                    notification.setIdComment(idComment);
                    notification.setText(userName+" vừa phản hồi bình luận của bạn");
                    notification.setIdRecipient(idUserReply);
                    if(positionComment != -1){
                        tokenFCM = listComment.get(positionComment).getUser().getTokenFCM();
                    }else if (positionReplyComment != -1 && positionReplyCommentParent != -1){
                        tokenFCM = listComment.get(positionReplyCommentParent).getReplyComment().get(positionReplyComment).getUser().getTokenFCM();
                    }

                }else{
                    notification.setText(userName+" vừa bình luận bài viết của bạn");
                    notification.setIdRecipient(userId);
                }

                NotificationService.sendNotification(getContext(), notification.getText(), tokenFCM);

                userViewModel.addNotification(notification);

                Log.e("checkNotificaiton","idPost "+ idPost);
                Log.e("checkNotificaiton","idComment "+ idComment);
                RequestCreateComment createComment = new RequestCreateComment(
                        idPost,
                        "65e8a525714ccc3a3caa7f77",
                        contentComment,
                        isReplyComment,
                        idComment,
                        idUserReply);

                Gson gson = new Gson();
                String j = gson.toJson(createComment);
                Log.e("relly",j);
                // Call the ViewModel to create a comment
                String finalIdComment = idComment;
                commentViewModel.createComment(createComment).observe((FragmentActivity) context, new Observer<ApiResponse<CommentResponse>>() {
                    @Override
                    public void onChanged(ApiResponse<CommentResponse> response) {
                        if (response.getData() != null) {
                            // Comment created successfully, refresh the comment list
                            commentViewModel.getListCommentByIdPost(idPost, finalIdComment).observe((FragmentActivity) context, new Observer<ApiResponse<List<CommentResponse>>>() {
                                @Override
                                public void onChanged(ApiResponse<List<CommentResponse>> response) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response);
                                    Log.e("listComment", json);
                                    if (response.getData().size() > 0) {
                                        listComment = response.getData();
                                        commentAdapter = new CommentAdapter(context, listComment, (LifecycleOwner) context);
                                        list_comment.setAdapter(commentAdapter);
                                    } else {
                                        // Handle when there is no data or there's an error
                                    }
                                }
                            });
                        } else {
                            // Handle if there's an error creating the comment
                        }
                    }
                });

                // Clear the EditText after submitting the comment
                edt_contentComment.setText("");
                listIconsChoosed.clear();
            }
        });

        //
        userViewModel.getDetailUserById("65e8a525714ccc3a3caa7f77").observe((FragmentActivity) context, new Observer<ApiResponse<UserResponse>>() {
            @Override
            public void onChanged(ApiResponse<UserResponse> response) {
                if (response.getMessage().equals("Success") && response.getStatus()) {
                    UserResponse userResponse = response.getData();
                    Picasso.get().load(userResponse.getAvatarImg()).into(img_user);
                }
            }
        });

        // action click btn_sendGifComment
        btn_sendGifComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("size",String.valueOf(listComment.size()));
            }
        });
    }

    public static List<Integer> getIconList() {
        List<Integer> icons = new ArrayList<>();

        icons.add(R.drawable.icons_favorite);
        icons.add(R.drawable.icons_pouting_face);
        icons.add(R.drawable.icons_clown_face);
        icons.add(R.drawable.icons_dragon_face);
        icons.add(R.drawable.icons_angry_face);
        icons.add(R.drawable.icons_crying_face);
        icons.add(R.drawable.icons_dizzy_face);
        icons.add(R.drawable.icons_angry_face);
        icons.add(R.drawable.icons_astonished_face);
        icons.add(R.drawable.icons_downcast_face);
        icons.add(R.drawable.icons_fearful_face);
        icons.add(R.drawable.icons_flushed_face);
        icons.add(R.drawable.icons_grinning_face);
        icons.add(R.drawable.icons_hugging_face);
        icons.add(R.drawable.icons_kissing_face);
        icons.add(R.drawable.icons_melting_face);
        icons.add(R.drawable.icons_smiling_face_hearts);
        icons.add(R.drawable.icons_nerd_face);
        icons.add(R.drawable.icons_zany_face);
        icons.add(R.drawable.icons_worried_face);
        icons.add(R.drawable.icons_smirking_face);
        icons.add(R.drawable.icons_saluting_face);
        return icons;
    }

    @Override
    public void onIconClick(int iconResId) {
        // Lấy biểu tượng hiện tại
        Drawable iconDrawable = ContextCompat.getDrawable(context, iconResId);

        // Chuyển đổi biểu tượng thành chuỗi
        String iconString = getIconString(iconResId);

        // Tạo một SpannableStringBuilder để xử lý văn bản có thể định dạng
        SpannableStringBuilder builder = new SpannableStringBuilder(edt_contentComment.getText());

        // Thêm biểu tượng vào cuối văn bản hiện tại
        if (iconDrawable != null) {
            iconDrawable.setBounds(0, 0, iconDrawable.getIntrinsicWidth(), iconDrawable.getIntrinsicHeight());
            ImageSpan imageSpan = new ImageSpan(iconDrawable, ImageSpan.ALIGN_BOTTOM);
            builder.append(" -"+listIconsChoosed.size()+"-");
            builder.setSpan(imageSpan, builder.length() - 3, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // Thêm chuỗi biểu tượng vào nội dung của comment
        //builder.append(iconString);
        listIconsChoosed.add(iconString);
        Log.e("icon",builder+"");
        // Hiển thị văn bản mới trong edt_contentComment
        edt_contentComment.setText(builder);
        edt_contentComment.setSelection(builder.length());
    }

    public String getIconString(int iconResId) {
        // Lấy tên tệp của biểu tượng từ tài nguyên
        String iconName = context.getResources().getResourceEntryName(iconResId);
        // Trả về tên tệp của biểu tượng
        return iconName;
    }


}
