package com.example.android_homeassignment1;

import java.util.Random;

public class RandomUtils {

    private static RandomUtils instance;
    private final Random random;

    private RandomUtils() {
        random = new Random();
    }

    public static RandomUtils getInstance() {
        return instance;
    }

    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

    public int nextInt(int lowerBound, int upperBound) {
        return nextInt(upperBound - lowerBound + 1) + lowerBound;
    }
}
