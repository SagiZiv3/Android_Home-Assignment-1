package com.example.android_homeassignment2;

import android.app.Application;

import com.example.android_homeassignment2.utils.RandomUtils;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FeedbackHandler.init(this);
        MSPV3.init(this);
        RandomUtils.init();
    }
}
