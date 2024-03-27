package com.example.frontend.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.frontend.R;
import com.example.frontend.adapter.ImagePostAdapter;
import com.example.frontend.fragments.HomeFragment;
import com.example.frontend.utils.CameraX;
import com.example.frontend.utils.GradientTextView;
import com.example.frontend.utils.ImageGallery;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PostActivity extends AppCompatActivity {

    private ImageView btn_closePost,  btn_cameraPost, img_mainPost;
    private GradientTextView btn_netPost;
    private RecyclerView list_mainPost;
    private ImagePostAdapter adapter;
    public static TextView btn_choseMore;

    private List<String> imageGallery;
    private static final int MY_READ_PERMISSION_CODE = 101;
    public static int checkChoseMore = 0;
    public static int checkScreen = 0;

    public static int checkDataIntent = 0;
    public static int chooseMoreClickCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Init views
        btn_closePost = findViewById(R.id.btn_closePost);
        list_mainPost = findViewById(R.id.list_mainPost);
        btn_cameraPost = findViewById(R.id.btn_cameraPost);
        img_mainPost = findViewById(R.id.img_mainPost);
        btn_choseMore = findViewById(R.id.btn_choseMore);
        btn_netPost = findViewById(R.id.btn_netPost);

        // Nhận dữ liệu màu nền từ Intent
        int buttonColorResId = getIntent().getIntExtra("buttonColor", 0);
        if (buttonColorResId != 0) {
            setButtonBackground();
        }

        // Close page post
        btn_closePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        //
        btn_cameraPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDataIntent = 0;
                checkScreen = 0;
                Intent intent = new Intent(PostActivity.this, CameraX.class);
                startActivity(intent);
            }
        });

        // check permission
        checkPermissionAndLoadImages();
        //
        String imageMain = imageGallery.get(0);
        Glide.with(getApplicationContext()).load(imageMain).into(img_mainPost);

        // action click chose more image
        btn_choseMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseMoreClickCount == 0) {
                    checkChoseMore = 1;
                    ArrayList<Integer> selectedPositions = adapter.getSelectedPositions();
                    StringBuilder message = new StringBuilder();
                    for (Integer position : selectedPositions) {
                        message.append(position + 1).append(", ");
                    }
                    btn_choseMore.setBackgroundResource(R.drawable.background_btn_radius_selected);
                    chooseMoreClickCount++;
                    adapter.notifyDataSetChanged();
                } else {
                    checkChoseMore = 0;
                    chooseMoreClickCount--;
                    btn_choseMore.setBackgroundResource(R.drawable.background_btn_radius);
                    adapter.notifyDataSetChanged();
                }

                //Toast.makeText(PostActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // action click next post
        btn_netPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkChoseMore == 1) {
                    checkScreen = 1;
                    List<Integer> selectedPositions = adapter.getSelectedPositions();
                    List<String> selectedImages = new ArrayList<>();
                    for (Integer position : selectedPositions) {
                        selectedImages.add(imageGallery.get(position));
                    }
                    if (!selectedImages.isEmpty()) {
                        checkDataIntent = 1;
                        Intent intent = new Intent(PostActivity.this, CreatePostActivity.class);
                        intent.putStringArrayListExtra("selectedImages", (ArrayList<String>) selectedImages);
                        startActivity(intent);
                    } else {
                        Toast.makeText(PostActivity.this, "No images selected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    choseOneImage();
                }
            }
        });

    }

    // function chose one image
    private void choseOneImage(){
        checkDataIntent = 2;
        checkScreen = 1;
        Drawable drawable = img_mainPost.getDrawable();
        if (drawable != null) {
            Bitmap bitmap = null;
            if (drawable instanceof BitmapDrawable) {
                bitmap = ((BitmapDrawable) drawable).getBitmap();
            } else if (drawable instanceof VectorDrawable) {
                // Handle case where drawable is a VectorDrawable
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
            } else if (drawable instanceof GifDrawable) {
                // Handle case where drawable is a GIF
                GifDrawable gifDrawable = (GifDrawable) drawable;
                ByteBuffer buffer = gifDrawable.getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            } else {
                // Handle other drawable types if necessary
            }

            if (bitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                String imagePath = saveImageToFile(byteArray);

                Intent intent = new Intent(PostActivity.this, CreatePostActivity.class);
                intent.putExtra("imagePaths", imagePath);
                startActivity(intent);
            } else {
                // Handle case where bitmap is null
                Toast.makeText(PostActivity.this, "Cannot convert drawable to bitmap", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(PostActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to save byte array to a file and return the file path
    private String saveImageToFile(byte[] byteArray) {
        // Create a directory for images if not exists
        File directory = new File(getFilesDir(), "images");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Create a unique file name
        String fileName = "image_" + System.currentTimeMillis() + ".png";

        // Save byte array to file
        File file = new File(directory, fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }

    // check permission and load image to img_mainPost
    private void checkPermissionAndLoadImages() {
        if (ContextCompat.checkSelfPermission(PostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it from the user
            ActivityCompat.requestPermissions(PostActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_READ_PERMISSION_CODE);
        } else {
            // Permission granted, load images
            loadImages();
        }
    }

    // Request Permissions Result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_READ_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, load images
                loadImages();
            } else {
                Toast.makeText(PostActivity.this, "Read external storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // function load image ti img_mainPost
    private void loadImages() {
        list_mainPost.setHasFixedSize(true);
        list_mainPost.setLayoutManager(new GridLayoutManager(this, 3));
        imageGallery = ImageGallery.listOfImage(this);
        adapter = new ImagePostAdapter(this, imageGallery, new ImagePostAdapter.PhotoListener() {
            @Override
            public void onPhotoClick(String path) {
                // Load selected image into img_mainPost
                Glide.with(PostActivity.this).load(path).into(img_mainPost);
            }
        });
        list_mainPost.setAdapter(adapter);
    }

    //
    public void setButtonBackground() {
        btn_choseMore.setBackgroundResource(R.drawable.background_btn_radius_selected);
    }


}
