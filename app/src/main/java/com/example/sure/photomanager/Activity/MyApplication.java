package com.example.sure.photomanager.Activity;

import android.app.Application;

import org.litepal.LitePal;

public class MyApplication extends Application {
    private static MyApplication sinstance;
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }
    public static MyApplication getInstance() {
        return sinstance;
    }
}
