package com.example.frontend.utils;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public class FirebaseStorageUploader {

    private static final String TAG = "FirebaseStorageHelper";

    // Hàm tải lên tệp tin lên Firebase Storage và nhận về đường dẫn URL
    public static void uploadFileToFirebaseStorage(Uri fileUri, String fileName, final OnUploadCompleteListener listener) {
        // Tạo tham chiếu đến Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://noteapp-f20f4.appspot.com");
        StorageReference storageRef = storage.getReference();

        // Tạo tham chiếu đến vị trí lưu trữ tệp tin trong thư mục "uploadFiles"
        StorageReference fileRef = storageRef.child("uploadFiles/" + fileName);

        // Tải lên tệp tin lên Firebase Storage
        UploadTask uploadTask = fileRef.putFile(fileUri);

        // Lắng nghe sự kiện hoàn thành tải lên
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Lấy đường dẫn URL của tệp tin đã tải lên
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUri) {
                        String fileUrl = downloadUri.toString();
                        Log.d(TAG, "File uploaded successfully. URL: " + fileUrl);
                        // Gọi phương thức callback để truyền đường dẫn URL về cho người dùng
                        listener.onUploadComplete(fileUrl);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "File upload failed: " + exception.getMessage());
                // Gọi phương thức callback để thông báo lỗi cho người dùng
                listener.onUploadFailed(exception.getMessage());
            }
        });
    }

    // Interface để định nghĩa phương thức callback
    public interface OnUploadCompleteListener {
        void onUploadComplete(List<String> fileUrls);

        void onUploadComplete(String fileUrl);
        void onUploadFailed(String errorMessage);
    }
}