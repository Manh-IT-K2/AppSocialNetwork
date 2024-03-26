package com.example.frontend.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontend.R;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.CaptureAct;
import com.example.frontend.utils.QRCode;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class QRCodeFragment extends Fragment {
    TextView nameTv;
    ImageView imageIv, cameraBtn, downloadBtn, shareBtn, backBtn, galleryBtn;

    static final int CAMERA_REQUEST_CODE = 100, STORAGE_REQUEST_CODE = 101;

    String[] cameraPermission, storagePermission;

    String TAG = "MAIN_TAG";

    Uri imageUri = null;

    BarcodeScannerOptions barcodeScannerOptions;
    BarcodeScanner barcodeScanner;
    CircleImageView avatar;
    UserViewModel userViewModel;

    public QRCodeFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_r_code, container, false);

        init(view);

        loadQRCode();
        clickListener();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getDetailUserById(SharedPreferenceLocal.read(getContext(),"userId")).observe(getViewLifecycleOwner(), new Observer<ApiResponse<UserResponse>>() {
            @Override
            public void onChanged(ApiResponse<UserResponse> userResponseApiResponse) {
                nameTv.setText(userResponseApiResponse.getData().getUsername());
                Picasso.get().load(userResponseApiResponse.getData().getAvatarImg()).into(avatar);
            }
        });
        return view;
    }

    private void loadQRCode() {
        String userId = SharedPreferenceLocal.read(getContext(),"userId");
        Bitmap qrcodeBitmap = QRCode.generateQRCode(userId);
        imageIv.setImageBitmap(qrcodeBitmap);
    }

    private void clickListener() {
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(checkCameraPermission()) pickImageCamera();
//                else ActivityCompat.requestPermissions(getActivity(), cameraPermission, CAMERA_REQUEST_CODE);
                ScanOptions scanOptions = new ScanOptions();
                scanOptions.setPrompt("Camera").setBeepEnabled(true).setOrientationLocked(true).setCaptureActivity(CaptureAct.class);
                barLauncher.launch(scanOptions);
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkStoragePermission()) pickImageGallery();
                else ActivityCompat.requestPermissions(getActivity(), storagePermission, STORAGE_REQUEST_CODE);
            }
        });

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = QRCode.readQRCodeFromImageView(imageIv);
                QRCode.saveQRCodeFromImageView(getContext(), imageIv, userId);
                Toast.makeText(getContext(), "Download is success", Toast.LENGTH_SHORT).show();
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = QRCode.readQRCodeFromImageView(imageIv);
                if(userId != null){
                    // Lấy drawable từ ImageView
                    Drawable drawable = imageIv.getDrawable();
                    Bitmap bitmap = null;

                    if (drawable instanceof BitmapDrawable) {
                        bitmap = ((BitmapDrawable) drawable).getBitmap();
                    } else {
                        // Chuyển đổi drawable thành bitmap nếu drawable không phải là BitmapDrawable
                        bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        drawable.draw(canvas);
                    }

                    // Lưu bitmap vào bộ nhớ tạm
                    String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "Image Description", null);

                    // Tạo Uri từ đường dẫn lưu
                    Uri imageUri = Uri.parse(path);

                    // Tạo Intent để chia sẻ qua Zalo
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/*");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//                    shareIntent.setPackage("com.zing.zalo"); // Gói ứng dụng Zalo

                    startActivity(Intent.createChooser(shareIntent, "Chia sẻ qua:"));
                }else{
                    Toast.makeText(getContext(), "Cannot share this QR Code", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null){
//            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//            builder.setTitle("Result: ");
//            builder.setMessage(result.getContents());
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            }).show();

            Log.d("https", result.getContents());
            if(result.getContents().contains("https")){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
                startActivity(intent);
            }else{
                // Tạo một Bundle để đóng gói dữ liệu email
                Bundle bundle = new Bundle();
                bundle.putString("userId", result.getContents());

                // Tạo Fragment mới và gắn Bundle vào đó
                ProfileFragment profileFragment = new ProfileFragment();
                profileFragment.setArguments(bundle);

                // Chuyển sang profileFragment
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout_main,profileFragment).commit();
            }
        }
    });

    private void pickImageGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        imageUri = data.getData();
                        Log.d(TAG,"onActivityResult: imageUri "+imageUri);

                        //imageIv.setImageURI(imageUri);
                        String userId = QRCode.readQRCodeFromUri(getActivity(),imageUri);

                        if(userId != null){
                            // Tạo một Bundle để đóng gói dữ liệu email
                            Bundle bundle = new Bundle();
                            bundle.putString("userId", userId);

                            // Tạo Fragment mới và gắn Bundle vào đó
                            ProfileFragment profileFragment = new ProfileFragment();
                            profileFragment.setArguments(bundle);

                            // Chuyển sang profileFragment
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout_main,profileFragment).commit();
                        }else{
                            Toast.makeText(getActivity(), "Can not read this QR code...", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private void pickImageCamera(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Sample Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Sample Image Description");

        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityResultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Log.d(TAG,"onActivityResult Camera: imageUri "+imageUri);

                        //imageIv.setImageURI(imageUri);
                        String userId = QRCode.readQRCodeFromImageView(imageIv);
                        //String userId = QRCode.readQRCodeFromUri(getActivity(),imageUri);

                        if(userId != null){
                            // Tạo một Bundle để đóng gói dữ liệu email
                            Bundle bundle = new Bundle();
                            bundle.putString("userId", userId);

                            // Tạo Fragment mới và gắn Bundle vào đó
                            ProfileFragment profileFragment = new ProfileFragment();
                            profileFragment.setArguments(bundle);

                            // Chuyển sang profileFragment
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout_main,profileFragment).commit();
                        }else{
                            Toast.makeText(getActivity(), "Can not read this QR code...", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getActivity(), "Cancelled...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        return result;
    }

    private boolean checkCameraPermission(){
        boolean resultCamera = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;

        boolean resultStorage = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;

        return resultCamera && resultStorage;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && storageAccepted) pickImageCamera();
                    else Toast.makeText(getContext(), "Camera and Storage permission is required ....", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if(grantResults.length > 0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(storageAccepted) pickImageGallery();
                    else Toast.makeText(getContext(), "Storage permission is required ....", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    private void init(View view) {
        cameraPermission = new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        barcodeScannerOptions =  new BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS).build();
        barcodeScanner = BarcodeScanning.getClient(barcodeScannerOptions);

        cameraBtn = view.findViewById(R.id.cameraBtn);
        galleryBtn = view.findViewById(R.id.galleryBtn);
        downloadBtn = view.findViewById(R.id.downloadBtn);
        shareBtn = view.findViewById(R.id.shareBtn);
        backBtn = view.findViewById(R.id.backBtn);

        imageIv = view.findViewById(R.id.imageIv);
        avatar = view.findViewById(R.id.avatar);
        nameTv = view.findViewById(R.id.nameTv);
    }
}