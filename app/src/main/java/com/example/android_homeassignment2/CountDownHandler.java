package com.example.android_homeassignment2;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.example.android_homeassignment2.utils.MyTimer;

public class CountDownHandler {

    private final int startValue;
    private int currentValue;
    private final MyTimer timer;
    private final TextView countDownLabel;
    private final CountDownEnd countDownEnd;
    private boolean running, paused;

    public CountDownHandler(int startValue, TextView countDownLabel, CountDownEnd countDownEnd) {
        this.startValue = startValue;
        this.countDownLabel = countDownLabel;
        this.countDownEnd = countDownEnd;
        timer = new MyTimer(1000, this::tick);
    }

    @SuppressLint("SetTextI18n")
    private void tick() {
        currentValue--;
        countDownLabel.setText(Integer.toString(currentValue));
        if (currentValue == 0) {
            timer.stop();
            running = false;
            countDownEnd.onFinish();
        }
    }

    @SuppressLint("SetTextI18n")
    public void start() {
        currentValue = startValue;
        countDownLabel.setText(Integer.toString(currentValue));
        timer.start();
        running = true;
        paused = false;
    }

    public void stop() {
        timer.stop();
        running = false;
    }

    public void resume() {
        timer.start();
        paused = false;
    }

    public void pause() {
        timer.stop();
        paused = true;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isPaused() {
        return paused;
    }

    public interface CountDownEnd {
        void onFinish();
    }
}
