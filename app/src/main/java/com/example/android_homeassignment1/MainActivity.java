package com.example.android_homeassignment1;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static final int NUM_COLUMNS = 3;
    private static final int CAR_INITIAL_COLUMN = NUM_COLUMNS / 2;

    private ViewsCollection<ImageView> columnImages;
    private ViewsCollection<ImageView> heartImages;
    private TextView countDownLabel;
    private MyTimer timer;
    private ImageView gameOverImage;
    private CountDownHandler countDownHandler;
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
        countDownLabel = findViewById(R.id.main_LBL_countdown);
        countDownHandler = new CountDownHandler(Integer.parseInt(countDownLabel.getText().toString()),
                countDownLabel, this::startGame);
//        countDownHandler.start();
        addControllersListeners();
    }

    private void startGame() {
        timer.start();
        countDownLabel.setVisibility(View.GONE);
    }

    private void createGameManager(BoardUI boardUI, BoardController boardController) {
        boardManager = new BoardManager(boardUI, boardController, boardUI.getNumRows(), NUM_COLUMNS, heartImages.length());
        boardManager.setGameOverListener(this::gameOver);
        boardManager.setCollisionListener(this::onCollision);
    }

    private void addControllersListeners() {
        columnImages.getView(columnImages.length() - 2)
                .setOnClickListener(e -> {
                    boardManager.moveCarLeft();
                    FeedbackHandler.getInstance().vibrate(50);
                });
        columnImages.getView(columnImages.length())
                .setOnClickListener(e -> {
                    boardManager.moveCarRight();
                    FeedbackHandler.getInstance().vibrate(50);
                });

        gameOverImage.setOnClickListener(e -> {
            finish();
            startActivity(getIntent());
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
        FeedbackHandler.getInstance().vibrate(500);
    }

    private void gameOver() {
        FeedbackHandler.getInstance().toast("! GAME OVER !", Toast.LENGTH_LONG);
        timer.stop();
        gameOverImage.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (boardManager.isGameOver()) return;
        if (countDownHandler.isPaused())
            countDownHandler.resume();
        if (!countDownHandler.isRunning()) {
            countDownHandler.start();
            countDownLabel.setVisibility(View.VISIBLE);
        }
        System.out.println("Hello from tablet");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (countDownHandler.isRunning())
            countDownHandler.pause();
        else
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
        gameOverImage = findViewById(R.id.main_IMG_gameover);
    }
}