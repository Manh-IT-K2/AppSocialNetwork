package com.example.frontend.utils;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDexApplication;

import io.stipop.Stipop;

public class GlobalApplication extends MultiDexApplication {
    private static GlobalApplication instance;

    public static GlobalApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Stipop.Companion.configure(this);
    }

    public Context getContext() {
        return getApplicationContext();
    }
}
