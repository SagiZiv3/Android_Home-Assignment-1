package com.example.android_homeassignment1;

import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FeedbackHandler.init(this);
    }
}
