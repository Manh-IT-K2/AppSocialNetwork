package com.example.frontend.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.frontend.R;

public class CommentFragment extends Dialog {

    float startY;
    float lastY;
    boolean isMoving = false;
    private TextView btn_drag;
    private LinearLayout linearLayout;
    public CommentFragment(Context context) {
        super(context);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.fragment_comment);
    }

    // Các phương thức khác để cấu hình Dialog nếu cần
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_comment);

        // Thiết lập các thuộc tính của Dialog
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);

        // Xử lý sự kiện kéo di chuyển
        btn_drag  = findViewById(R.id.btn_drag);
        linearLayout = findViewById(R.id.linearLayout);
        // Thêm sự kiện lắng nghe cho LinearLayout chứa bình luận

        btn_drag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss(); // Đóng dialog khi nhấn vào btn_drag
            }
        });

//        linearLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        startY = event.getRawY();
//                        lastY = event.getRawY();
//                        isMoving = false;
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        float deltaY = event.getRawY() - lastY;
//                        float newY = v.getY() + deltaY;
//
//                        // Kiểm tra xem có đang di chuyển không
//                        if (!isMoving) {
//                            isMoving = true;
//                            deltaY = 0; // Đặt lại deltaX và deltaY nếu đang bắt đầu di chuyển
//                        }
//
//                        // Cập nhật vị trí của giao diện
//                        v.setY(newY);
//                        lastY = event.getRawY();
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        // Xử lý sự kiện khi người dùng thả tay
//                        break;
//                }
//                return true; // Trả về true để bắt lấy các sự kiện tiếp theo
//            }
//        });


    }

}
