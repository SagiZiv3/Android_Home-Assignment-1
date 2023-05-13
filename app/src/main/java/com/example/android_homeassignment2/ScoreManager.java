package com.example.android_homeassignment2;

public class ScoreManager {

    private int distance;
    private int numCoins;

    public ScoreManager() {
        distance = numCoins = 0;
    }

    public void increaseDistance() {
        distance++;
    }

    public void coinCollected() {
        numCoins++;
    }

    public int getDistance() {
        return distance;
    }

    public int getNumCoins() {
        return numCoins;
    }
}
