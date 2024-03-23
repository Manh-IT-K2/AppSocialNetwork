package com.example.frontend.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

public class ImageGallery {

    public static ArrayList<String> listOfImage(Context context){
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;

        ArrayList<String> listOfAllImage = new ArrayList<>();
        String absolutePathOfImage;

        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns.DATA,MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        String oderBy = MediaStore.Video.Media.DATE_TAKEN;
        cursor = context.getContentResolver().query(uri,projection,null,null,oderBy + " DESC");
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        while (cursor.moveToNext()){
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImage.add(absolutePathOfImage);
        }
        return listOfAllImage;
    }
}
