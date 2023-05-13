package com.example.android_homeassignment2;

import com.example.android_homeassignment2.entities.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HighScoresHandler {
    private static final String HIGH_SCORES_KEY = "USERS_HIGH_SCORES";

    private List<User> topTen;

    public HighScoresHandler() {
        topTen = new ArrayList<>(10);
    }

    public void loadHighScores(HighScoresLoadedCallback highScoresLoadedCallback) {
        // Read the high scores from the device's storage, if the data doesn't exist use default to empty array.
        String data = MSPV3.getInstance().readString(HIGH_SCORES_KEY, "[]");
        User[] users = new Gson().fromJson(data, User[].class);
        topTen = new ArrayList<>(Arrays.asList(users));
        highScoresLoadedCallback.onHighScoresLoaded();
    }

    public void saveHighScores() {
        String data = new Gson().toJson(topTen.toArray());
        MSPV3.getInstance().saveString(HIGH_SCORES_KEY, data);
    }

    public int getUserPositionInTopTen(User user) {
        if (topTen.isEmpty()) return 0;
        double userScore = user.calculateScore();
        for (int i = 0; i < topTen.size(); i++) {
            User topTenUser = topTen.get(i);
            if (topTenUser.calculateScore() < userScore) return i;
        }
        return topTen.size() < 10 ? topTen.size() : -1;
    }

    public int addUserToTopTen(User user) {
        int insertionPosition = getUserPositionInTopTen(user);
        if (insertionPosition < 0) return -1;

        // If the top-ten list is full, remove the last entry.
        if (topTen.size() == 10) topTen.remove(9);
        // Insert the user to the list.
        topTen.add(insertionPosition, user);

        return insertionPosition;
    }

    public List<User> getTopTen() {
        return topTen;
    }

    public interface HighScoresLoadedCallback {
        void onHighScoresLoaded();
    }
}
