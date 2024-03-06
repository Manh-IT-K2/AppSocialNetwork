package com.example.frontend.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceLocal {
    public static void save(Context context, String key, String value){
        // Lưu dữ liệu vào SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String read(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }
}
