package com.example.android_homeassignment1;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static final int NUM_COLUMNS = 3;
    private static final int CAR_INITIAL_COLUMN = NUM_COLUMNS / 2;

    private ViewsCollection<ImageView> columnImages;
    private ViewsCollection<ImageView> heartImages;
    private MyTimer timer;
    private BoardManager boardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScreenUtils.hideSystemUI(this);

        findViews();
        BoardUI boardUI = createBoardUI();
        BoardController boardController = new BoardController(CAR_INITIAL_COLUMN, boardUI.getNumRows(), NUM_COLUMNS);
        boardController.reset();
        createGameManager(boardUI, boardController);

        timer = new MyTimer(150, boardManager::Tick);
        addControllersListeners();
    }

    private void createGameManager(BoardUI boardUI, BoardController boardController) {
        boardManager = new BoardManager(boardUI, boardController, boardUI.getNumRows(), NUM_COLUMNS, heartImages.length());
        boardManager.setGameOverListener(this::gameOver);
        boardManager.setCollisionListener(this::onCollision);
    }

    private void addControllersListeners() {
        columnImages.getView(columnImages.length() - 2).setOnClickListener(e -> {
            boardManager.moveCarLeft();
        });
        columnImages.getView(columnImages.length()).setOnClickListener(e -> {
            boardManager.moveCarRight();
        });
    }

    @NonNull
    private BoardUI createBoardUI() {
        BoardUI boardUI = new BoardUI(NUM_COLUMNS, true, columnImages, heartImages);
        boardUI.drawBoard(R.drawable.ic_rock, R.drawable.ic_car);
        // Hide the obstacles
        boardUI.hideAllObstacles();
        // Start in the middle
        boardUI.setCarColumn(CAR_INITIAL_COLUMN);
        // Draw arrow controllers
        boardUI.drawControlsRow(R.drawable.ic_arrow, true);
        return boardUI;
    }

    private void onCollision() {
        FeedbackHandler.getInstance().toast("💥 BOOM 💥", Toast.LENGTH_SHORT);
        FeedbackHandler.getInstance().vibrate();
    }

    private void gameOver() {
        FeedbackHandler.getInstance().toast("! GAME OVER !", Toast.LENGTH_LONG);
        timer.stop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!boardManager.isGameOver())
            timer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.stop();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            ScreenUtils.hideSystemUI(this);
        }
    }

    private void findViews() {
        Field[] fields = R.id.class.getFields();
        heartImages = ViewsCollection.findViewsForPattern(
                this,
                fields,
                Pattern.compile("main_IMG_heart(\\d+)")
        );
        columnImages = ViewsCollection.findViewsForPattern(
                this,
                fields,
                Pattern.compile("main_IMG_column(\\d+)")
        );
    }
}