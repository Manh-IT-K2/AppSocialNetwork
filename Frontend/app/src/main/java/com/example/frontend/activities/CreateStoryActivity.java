package com.example.frontend.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.multidex.MultiDex;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.request.Story.RequestCreateStory;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.utils.SpotifyManager;
import com.example.frontend.viewModel.Story.StoryViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.stipop.Stipop;
import io.stipop.StipopDelegate;
import io.stipop.extend.StipopImageView;
import io.stipop.model.SPPackage;
import io.stipop.model.SPSticker;


public class CreateStoryActivity extends AppCompatActivity implements StipopDelegate, SpotifyManager.SpotifyConnectionListener {


    // init variable
    private List<RequestCreateStory.ContentMedia> listOfContents = new ArrayList<>();
    private List<RequestCreateStory.Stickers> listOfStickers = new ArrayList<>();;
    private FrameLayout deleteLayout;
    private ImageView imageViewDelete, stickerImageView;
    StoryViewModel storyViewModel;
    private SpotifyManager mSpotifyManager;
    private StipopImageView btn_addStickerStory;
    private FrameLayout frame_layout_create_story;
    private ShapeableImageView img_createStory, img_avtUserCreateStory;
    private ImageButton btn_backCreateStory, btn_addTextStory, btn_addMusicStory, btn_settingCreateStory, btn_addStory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(this);
        setContentView(R.layout.activity_create_story);

        // init view
        img_createStory = findViewById(R.id.img_createStory);
        img_avtUserCreateStory = findViewById(R.id.img_avtUserCreateStory);
        btn_backCreateStory = findViewById(R.id.btn_backCreateStory);
        btn_addTextStory = findViewById(R.id.btn_addTextStory);
        btn_addStickerStory = findViewById(R.id.btn_addStickerStory);
        btn_addMusicStory = findViewById(R.id.btn_addMusicStory);
        btn_settingCreateStory = findViewById(R.id.btn_settingCreateStory);
        btn_addStory = findViewById(R.id.btn_addStory);
        frame_layout_create_story = findViewById(R.id.frame_layout_create_story);
        stickerImageView = findViewById(R.id.stickerImageView);


        //
        storyViewModel = new ViewModelProvider(this).get(StoryViewModel.class);

        //
        initBtnDeleteView();

        // Get URI address from Intent
        String imageUriString = getIntent().getStringExtra("imageUri");
        Uri imageUri = Uri.parse(imageUriString);

        // Assign photo img_createStory
        Glide.with(this)
                .load(imageUri)
                .into(img_createStory);

        // handle event click btn_backCreateStory
        btn_backCreateStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // handle click btn_addStory
        btn_addStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();
            }
        });

        // Xử lý sự kiện khi nhấn nút "Thêm văn bản"
        btn_addTextStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFloatingTextView();
            }
        });

        //
        showStickerPickerDialog();

        // Khởi tạo SpotifyManager
        mSpotifyManager = new SpotifyManager();

        // Gắn lắng nghe sự kiện cho nút btn_addMusicStory
        btn_addMusicStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kết nối với Spotify
                mSpotifyManager.connect(CreateStoryActivity.this, CreateStoryActivity.this);
            }
        });

    }

    private void showBottomDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.dialog_create_story);

        Button btnShareStory = bottomSheetDialog.findViewById(R.id.btn_shareStory);

        List<RequestCreateStory.ContentMedia> contentMediaList = new ArrayList<>();
        List<RequestCreateStory.Stickers> stickersList = new ArrayList<>();
        String imageStory = getIntent().getStringExtra("imageUri");
        Date createAt = new Date();
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        String isoDateString = isoFormat.format(createAt);
        String userId = SharedPreferenceLocal.read(getApplicationContext(), "userId");

        // Lặp qua danh sách các nội dung và thêm vào contentMediaList
        for (int i = 0; i < listOfContents.size(); i++) {
            String content = listOfContents.get(i).getContent();
            float X = listOfContents.get(i).getX();
            float Y = listOfContents.get(i).getY();
            contentMediaList.add(new RequestCreateStory.ContentMedia(content, X, Y));
        }

        // Lặp qua danh sách các nhãn dán và thêm vào stickersList
        for (int i = 0; i < listOfStickers.size(); i++) {
            String uriSticker = listOfStickers.get(i).getUriSticker();
            float x = listOfStickers.get(i).getX();
            float y = listOfStickers.get(i).getY();
            stickersList.add(new RequestCreateStory.Stickers(uriSticker, x, y));
        }

        RequestCreateStory story = new RequestCreateStory(userId, isoDateString, imageStory, contentMediaList, stickersList);

        btnShareStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                storyViewModel.createStory(story, userId);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        bottomSheetDialog.show();
    }


    private void addFloatingTextView() {
        final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        final EditText floatingEditText = new EditText(this);
        // Thiết lập thuộc tính cho EditText nổi
        floatingEditText.setHint("Nhập văn bản"); // Đặt văn bản mặc định
        floatingEditText.setTextSize(20); // Đặt kích thước chữ
        floatingEditText.setTextColor(Color.WHITE); // Đặt màu chữ
        floatingEditText.setTypeface(null, Typeface.BOLD); // Đặt kiểu chữ là đậm
        floatingEditText.setBackgroundColor(Color.TRANSPARENT); // Đặt màu nền transparent
        // Thiết lập các thuộc tính layout cho EditText nổi
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER; // Đặt vị trí EditText nổi ở giữa ảnh
        floatingEditText.setLayoutParams(params);
        // Loại bỏ EditText nếu đã có cha
        //Thêm EditText nổi vào FrameLayout (hoặc ViewGroup phù hợp)
        frame_layout_create_story.addView(floatingEditText);
        Log.e("hay qua ban", String.valueOf(floatingEditText.getText()));
        //listOfContents.add(String.valueOf(floatingEditText.getText()));
        // Yêu cầu EditText nổi nhận tập trung
        floatingEditText.requestFocus();
        // Hiển thị bàn phím
        imm.showSoftInput(floatingEditText, InputMethodManager.SHOW_IMPLICIT);
        // Xử lý sự kiện khi EditText nổi đã nhập văn bản và bị nhấn lại

        final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // Xử lý sự kiện khi người dùng chạm và giữ trên EditText nổi
        floatingEditText.setOnTouchListener(new View.OnTouchListener() {

            private float startX, startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                floatingEditText.clearFocus();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Lưu vị trí khi chạm vào EditText nổi
                        startX = event.getX();
                        startY = event.getY();
                        Log.e("positon", String.valueOf(startX) + " and " + String.valueOf(startY));
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // Di chuyển EditText nổi theo vị trí của ngón tay
                        floatingEditText.setX(floatingEditText.getX() + (event.getX() - startX));
                        floatingEditText.setY(floatingEditText.getY() + (event.getY() - startY));
                        imageViewDelete.setVisibility(View.VISIBLE);
                        deleteLayout.setVisibility(View.VISIBLE);
                        // Kiểm tra nếu EditText nổi được kéo vào khu vực xoá
                        float x = event.getRawX();
                        float y = event.getRawY();

                        if (isInsideView(x, y, imageViewDelete)) {
                            // Rung điện thoại
                            if (vibrator != null) {
                                vibrator.vibrate(50); // Thời gian rung 50ms
                            }
                            // Rung icon_delete
                            Animation shakeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                            imageViewDelete.startAnimation(shakeAnimation);
                            Animation scaleAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_down);
                            floatingEditText.startAnimation(scaleAnimation);
                            floatingEditText.setVisibility(View.GONE);

                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        // Kết thúc di chuyển
                        imageViewDelete.setVisibility(View.GONE);
                        deleteLayout.setVisibility(View.GONE);

                        // Kiểm tra nếu EditText nổi được kéo vào khu vực xoá khi thả ra
                        float releaseX = event.getRawX();
                        float releaseY = event.getRawY();
                        if (isInsideView(releaseX, releaseY, imageViewDelete)) {
                            frame_layout_create_story.removeView(floatingEditText);
                            imageViewDelete.setVisibility(View.GONE);
                            deleteLayout.setVisibility(View.GONE);

                        }
                        floatingEditText.setVisibility(View.VISIBLE);
                        floatingEditText.setTextSize(20);
                        String text = floatingEditText.getText().toString().trim();
                        if (!text.isEmpty()) {
                            floatingEditText.post(new Runnable() {
                                @Override
                                public void run() {
                                    int[] location = new int[2];
                                    floatingEditText.getLocationOnScreen(location);
                                    float marginTop = location[1];
                                    float marginLeft = location[0];
                                    listOfContents.add(new RequestCreateStory.ContentMedia(text,marginTop,marginLeft));
                                }
                            });
                        }
                        break;
                }
                return true;
            }
        });


        // Xử lý sự kiện khi EditText nổi đã nhập văn bản và bị nhấn lại
        frame_layout_create_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cho phép chỉnh sửa văn bản đã nhập
                floatingEditText.setFocusableInTouchMode(true);
                floatingEditText.requestFocus();
                imm.showSoftInput(floatingEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        });

    }

    private void initBtnDeleteView(){
        // Tạo một FrameLayout mới để chứa ImageView
        deleteLayout = new FrameLayout(this);
        FrameLayout.LayoutParams deleteLayoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        deleteLayoutParams.gravity = Gravity.CENTER | Gravity.BOTTOM;
        deleteLayout.setBackgroundResource(R.drawable.background_action_create_story);
        deleteLayout.setLayoutParams(deleteLayoutParams);

        imageViewDelete = new ImageView(this);
        imageViewDelete.setImageResource(R.drawable.icon_delete_red);

        FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        imageParams.gravity = Gravity.CENTER;
        imageViewDelete.setLayoutParams(imageParams);

        deleteLayout.addView(imageViewDelete);
        frame_layout_create_story.addView(deleteLayout);

        // hide view delete
        imageViewDelete.setVisibility(View.GONE);
        deleteLayout.setVisibility(View.GONE);
    }

    // Phương thức để kiểm tra xem một điểm x, y có nằm trong một View hay không
    private boolean isInsideView(float x, float y, View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];
        // Kiểm tra nếu x, y nằm trong khu vực của view
        return (x > viewX && x < (viewX + view.getWidth()) && y > viewY && y < (viewY + view.getHeight()));
    }

    //
    private void showStickerPickerDialog() {
        Stipop.Companion.connect(this, btn_addStickerStory, "1234", "en", "US", this);
        btn_addStickerStory.setOnClickListener(v -> Stipop.Companion.showSearch());

    }

    @Override
    public boolean onStickerSelected(SPSticker sticker) {
        System.out.print(sticker);
        Log.e("vcl",String.valueOf(sticker));
        addStickerToFrameLayout(sticker.getStickerImg());
        //listOfStickers.add(String.valueOf(sticker.getStickerImg()));
        return false;
    }

    @Override
    public boolean canDownload(SPPackage spPackage) {
        System.out.print(spPackage);
        return true;
    }

    //
    private void addStickerToFrameLayout(String imageUrl) {
        stickerImageView = new ImageView(this);
        Glide.with(this).load(imageUrl).into(stickerImageView);
        // Thiết lập các thuộc tính layout cho ImageView sticker
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                300,300
        );
        params.gravity = Gravity.CENTER; // Đặt vị trí sticker ở giữa frame_layout_create_story
        stickerImageView.setLayoutParams(params);
        frame_layout_create_story.addView(stickerImageView);

        final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        // Xử lý sự kiện chạm để di chuyển và phóng to thu nhỏ sticker
        stickerImageView.setOnTouchListener(new View.OnTouchListener() {
            private float startX, startY;
            private float initialScaleX, initialScaleY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Lưu vị trí khi chạm vào sticker
                        startX = event.getX();
                        startY = event.getY();
                        initialScaleX = stickerImageView.getScaleX();
                        initialScaleY = stickerImageView.getScaleY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // Di chuyển sticker theo vị trí của ngón tay
                        float offsetX = event.getX() - startX;
                        float offsetY = event.getY() - startY;
                        stickerImageView.setX(stickerImageView.getX() + offsetX);
                        stickerImageView.setY(stickerImageView.getY() + offsetY);

                        // Tính toán tỷ lệ phóng to thu nhỏ
                        float scaleChange = Math.max(Math.abs(offsetX), Math.abs(offsetY)) / 100f;

                        // Áp dụng tỷ lệ phóng to thu nhỏ
                        float newScaleX = initialScaleX + scaleChange;
                        float newScaleY = initialScaleY + scaleChange;
                        stickerImageView.setScaleX(newScaleX);
                        stickerImageView.setScaleY(newScaleY);

                        imageViewDelete.setVisibility(View.VISIBLE);
                        deleteLayout.setVisibility(View.VISIBLE);
                        // Kiểm tra nếu EditText nổi được kéo vào khu vực xoá
                        float x = event.getRawX();
                        float y = event.getRawY();

                        if (isInsideView(x, y, imageViewDelete)) {
                            // Rung điện thoại
                            if (vibrator != null) {
                                vibrator.vibrate(50); // Thời gian rung 50ms
                            }
                            // Rung icon_delete
                            Animation shakeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                            imageViewDelete.startAnimation(shakeAnimation);
                            Animation scaleAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_down);
                            stickerImageView.startAnimation(scaleAnimation);
                            stickerImageView.setVisibility(View.GONE);
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        // Kết thúc di chuyển
                        imageViewDelete.setVisibility(View.GONE);
                        deleteLayout.setVisibility(View.GONE);

                        // Kiểm tra nếu EditText nổi được kéo vào khu vực xoá khi thả ra
                        float releaseX = event.getRawX();
                        float releaseY = event.getRawY();
                        if (isInsideView(releaseX, releaseY, imageViewDelete)) {
                            frame_layout_create_story.removeView(stickerImageView);
                            imageViewDelete.setVisibility(View.GONE);
                            deleteLayout.setVisibility(View.GONE);

                        }
                        stickerImageView.setVisibility(View.VISIBLE);
                        if (!imageUrl.isEmpty()){
                            stickerImageView.post(new Runnable() {
                                @Override
                                public void run() {
                                    int[] location = new int[2];
                                    stickerImageView.getLocationOnScreen(location);
                                    float marginTop = location[1];
                                    float marginLeft = location[0];
                                    // Thêm sticker vào danh sách với vị trí marginTop và marginLeft
                                    listOfStickers.add(new RequestCreateStory.Stickers(imageUrl, marginTop, marginLeft));
                                }
                            });
                        }
                        break;
                }
                return true;
            }
        });
    }


    @Override
    public void onConnected() {
        // Replace "spotify:playlist:37i9dQZF1DX2sUQwD7tbmL" with your actual playlist URI
        mSpotifyManager.playPlaylist("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");
    }

    @Override
    public void onFailure(Throwable throwable) {
        Log.e("CreateStoryActivity", "Failed to connect to Spotify: " + throwable.getMessage(), throwable);
        // Handle failure, e.g., show a toast message or log the error
    }

}