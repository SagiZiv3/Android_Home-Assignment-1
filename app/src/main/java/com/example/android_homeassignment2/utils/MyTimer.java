package com.example.android_homeassignment2.utils;

import android.os.Handler;

public class MyTimer {
    private final int interval;
    private final TimerTick timerTick;
    private final Handler handler = new Handler();
    private int currentInterval;

    private final Runnable runnable = new Runnable() {
        public void run() {
            handler.postDelayed(runnable, currentInterval);
            timerTick.tick();
        }
    };

    public MyTimer(int interval, TimerTick timerTick) {
        this.interval = interval;
        this.timerTick = timerTick;
        this.currentInterval = interval;
    }

    public void start() {
        handler.postDelayed(runnable, currentInterval);
    }

    public void stop() {
        handler.removeCallbacks(runnable);
    }

    public void setIntervalMultiplier(float multiplier) {
        currentInterval = Math.round(interval / multiplier);
    }

    public interface TimerTick {
        void tick();
    }
}
