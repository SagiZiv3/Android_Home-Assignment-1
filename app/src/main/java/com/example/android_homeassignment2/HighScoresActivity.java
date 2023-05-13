package com.example.android_homeassignment2;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android_homeassignment2.entities.Location;
import com.example.android_homeassignment2.entities.User;
import com.example.android_homeassignment2.fragments.HighScoresFragment;
import com.example.android_homeassignment2.fragments.MapsFragment;
import com.example.android_homeassignment2.utils.ScreenUtils;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class HighScoresActivity extends AppCompatActivity {
    private static final int NUM_PROCESSES_TO_LOAD = 3;
    private MapsFragment mapsFragment;
    private HighScoresFragment highScoresFragment;
    private HighScoresHandler highScoresHandler;
    private AtomicInteger numInitialized;
    private Location userLocation;
    private ImageView loadingCircle;
    private ObjectAnimator rotationAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        ScreenUtils.hideSystemUI(this);

        numInitialized = new AtomicInteger(0);
        mapsFragment = new MapsFragment(this::onMapsInitialized);
        highScoresFragment = new HighScoresFragment();
        highScoresHandler = new HighScoresHandler();
        highScoresHandler.loadHighScores(this::onHighScoresLoaded);

        loadingCircle = findViewById(R.id.highscores_IMG_loadingCircle);
        rotationAnimator = ObjectAnimator.ofFloat(loadingCircle, "rotation", 0f, 360f);
        rotationAnimator.setDuration(2000);
        rotationAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        rotationAnimator.setInterpolator(new LinearInterpolator());
        rotationAnimator.start();


        getSupportFragmentManager().beginTransaction()
                .add(R.id.highscores_LAY_list, highScoresFragment)
                .add(R.id.highscores_LAY_map, mapsFragment)
                .commit();

        highScoresFragment.setSelectedCallback((itemIndex, selectedUser) -> {
            Location location = selectedUser.getLocation();
            if (location == null) {
                Log.d("pfff", String.format(Locale.getDefault(),
                        "User \"%s\" doesn't have location!", selectedUser.getName()));
                return;
            }
            mapsFragment.zoomToLocation(location.getLatitude(), location.getLongitude());
        });
    }

    private void onHighScoresLoaded() {
        onProcessFinishedInitialization();
    }

    private void onMapsInitialized() {
        mapsFragment.getCurrentUserLocation(new MapsFragment.LocationRetrievedCallback() {
            @Override
            public void onLocationRetrieved(double latitude, double longitude) {
                userLocation = new Location(latitude, longitude);
                onProcessFinishedInitialization();
            }

            @Override
            public void onLocationNotAvailable() {
                userLocation = null;
                onProcessFinishedInitialization();
            }
        });
        onProcessFinishedInitialization();
    }

    private void onProcessFinishedInitialization() {
        int totalInitialized = numInitialized.incrementAndGet();
        if (totalInitialized != NUM_PROCESSES_TO_LOAD) return;

        User user = loadUserDataFromBundle();
        int userPosition = -1;
        if (user != null) {
            userPosition = updateHighScoresList(user);
        }
        updateUI(user, userPosition);
    }

    private void updateUI(@Nullable User currentUser, int userPosition) {
        loadingCircle.setVisibility(View.GONE);
        rotationAnimator.end();
        showHighScoresTable(currentUser, userPosition);
        showUsersMarkersOnMap();
        if (userLocation != null)
            mapsFragment.zoomToLocation(userLocation.getLatitude(), userLocation.getLongitude());
    }

    private void showUsersMarkersOnMap() {
        for (User user : highScoresHandler.getTopTen()) {
            Location userLocation = user.getLocation();
            if (userLocation != null)
                mapsFragment.addLocationMarker(user.getName(), userLocation.getLatitude(), userLocation.getLongitude());
        }
    }

    private void showHighScoresTable(@Nullable User currentUser, int userPosition) {
        highScoresFragment.showUsers(highScoresHandler.getTopTen(), userPosition);
        if (userPosition < 0 && currentUser != null)
            highScoresFragment.showCurrentUser(currentUser);
        else {
            highScoresFragment.hideCurrentUserView();
            if (userPosition >= 0) {
                Handler handler = new Handler();
                handler.postDelayed(() -> highScoresFragment.scrollTo(userPosition), 1000);
            }
        }
    }

    private int updateHighScoresList(@NonNull User user) {
        // Try to enter the user to the list.
        int position = highScoresHandler.addUserToTopTen(user);
        // Check if the user was entered to the list.
        if (position < 0) return -1;

        highScoresHandler.saveHighScores();
        return position;
    }

    @Nullable
    private User loadUserDataFromBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            return null;
        BundleWrapper bundleWrapper = new BundleWrapper(bundle);
        return new User()
                .setName(bundleWrapper.getUserName())
                .setDistance(bundleWrapper.getUserDistance())
                .setNumCoins(bundleWrapper.getUserNumCoins())
                .setLocation(userLocation);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            ScreenUtils.hideSystemUI(this);
        }
    }
}