package com.example.android_homeassignment2;

import android.os.Bundle;

public class BundleWrapper {

    //<editor-fold desc="Key constants">
    private static final String INPUT_TYPE_KEY = "INPUT_TYPE_KEY";
    private static final String PLAY_SPEED_KEY = "PLAY_SPEED_KEY";
    private static final String USER_NAME_KEY = "USER_NAME_KEY";
    private static final String USER_NUM_COINS_KEY = "USER_NUM_COINS_KEY";
    private static final String USER_DISTANCE_KEY = "USER_DISTANCE_KEY";
    //</editor-fold>
    private final Bundle bundle;

    public BundleWrapper(Bundle bundle) {
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public boolean useSensorsAsInput() {
        return bundle.getBoolean(INPUT_TYPE_KEY);
    }

    public void setInputMethod(boolean useSensors) {
        bundle.putBoolean(INPUT_TYPE_KEY, useSensors);
    }

    public int getPlaySpeed() {
        return bundle.getInt(PLAY_SPEED_KEY);
    }

    public void setPlaySpeed(int playSpeed) {
        bundle.putInt(PLAY_SPEED_KEY, playSpeed);
    }

    public String getUserName() {
        return bundle.getString(USER_NAME_KEY);
    }

    public void setUserName(String name) {
        bundle.putString(USER_NAME_KEY, name);
    }

    public int getUserNumCoins() {
        return bundle.getInt(USER_NUM_COINS_KEY);
    }

    public void setUserNumCoins(int numCoins) {
        bundle.putInt(USER_NUM_COINS_KEY, numCoins);
    }

    public int getUserDistance() {
        return bundle.getInt(USER_DISTANCE_KEY);
    }

    public void setUserDistance(int distance) {
        bundle.putInt(USER_DISTANCE_KEY, distance);
    }
}
