package com.example.android_homeassignment1;

import android.os.Handler;

public class MyTimer {
    private final int interval;
    private final TimerTick timerTick;
    private final Handler handler = new Handler();

    private final Runnable runnable = new Runnable() {
        public void run() {
            handler.postDelayed(runnable, interval);
            timerTick.tick();
        }
    };

    public MyTimer(int interval, TimerTick timerTick) {
        this.interval = interval;
        this.timerTick = timerTick;
    }

    public void start() {
        handler.postDelayed(runnable, interval);
    }

    public void stop() {
        handler.removeCallbacks(runnable);
    }

    public interface TimerTick {
        void tick();
    }
}
