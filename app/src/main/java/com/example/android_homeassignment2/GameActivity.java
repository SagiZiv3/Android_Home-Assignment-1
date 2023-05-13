package com.example.android_homeassignment2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android_homeassignment2.board.BoardController;
import com.example.android_homeassignment2.board.BoardManager;
import com.example.android_homeassignment2.board.BoardUI;
import com.example.android_homeassignment2.input.ButtonsInputHandler;
import com.example.android_homeassignment2.input.InputHandler;
import com.example.android_homeassignment2.input.SensorInputHandler;
import com.example.android_homeassignment2.utils.MyTimer;
import com.example.android_homeassignment2.utils.ScreenUtils;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class GameActivity extends AppCompatActivity {
    private static final int NUM_COLUMNS = 5;
    private static final int CAR_INITIAL_COLUMN = NUM_COLUMNS / 2;

    private ViewsCollection<ImageView> columnImages;
    private ViewsCollection<ImageView> heartImages;
    private TextView countDownLabel;
    private ImageView gameOverImage;
    private CountDownHandler countDownHandler;
    private GameManager gameManager;
    private MyTimer gameTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ScreenUtils.hideSystemUI(this);

        BundleWrapper wrapper = new BundleWrapper(getIntent().getExtras());
        boolean useSensors = wrapper.useSensorsAsInput();
        int playSpeed = wrapper.getPlaySpeed();
        findViews();
        BoardUI boardUI = createBoardUI(useSensors);
        BoardController boardController = new BoardController(CAR_INITIAL_COLUMN, boardUI.getNumRows(),
                NUM_COLUMNS);
        boardController.reset();


        countDownLabel = findViewById(R.id.main_LBL_countdown);
        countDownHandler = new CountDownHandler(Integer.parseInt(countDownLabel.getText().toString()),
                countDownLabel, this::startGame);

        InputHandler inputHandler;
        if (useSensors)
            inputHandler = new SensorInputHandler(movementCallbacks, this);
        else
            inputHandler = new ButtonsInputHandler(
                    movementCallbacks,
                    columnImages.getView(columnImages.length() - (NUM_COLUMNS - 1)),
                    columnImages.getView(columnImages.length())
            );
        createManagers(boardUI, boardController, inputHandler);
        gameTimer = new MyTimer(playSpeed, gameManager::Tick);
        addControllersListeners();
    }

    private final InputHandler.MovementCallbacks movementCallbacks = new InputHandler.MovementCallbacks() {
        @Override
        public void moveLeft() {
            gameManager.moveCarLeft();
            FeedbackHandler.getInstance().vibrate(50);
        }

        @Override
        public void moveRight() {
            gameManager.moveCarRight();
            FeedbackHandler.getInstance().vibrate(50);
        }

        @Override
        public void setSpeedMultiplier(float multiplier) {
            gameTimer.setIntervalMultiplier(multiplier);
        }
    };

    private void startGame() {
        gameManager.start();
        gameTimer.start();
        countDownLabel.setVisibility(View.GONE);
    }

    private void createManagers(BoardUI boardUI, BoardController boardController, InputHandler inputHandler) {
        BoardManager boardManager = new BoardManager(boardUI, boardController, boardUI.getNumRows(), NUM_COLUMNS);
        ScoreManager scoreManager = new ScoreManager();
        HealthManager healthManager = new HealthManager(heartImages.length());
        gameManager = new GameManager(boardManager, healthManager, scoreManager, inputHandler);
        gameManager.setGameOverListener(this::gameOver);
        gameManager.setFeedbackCallbacks(new GameManager.FeedbackCallbacks() {
            @Override
            public void onCollision() {
                FeedbackHandler.getInstance().toast("💥 " + getString(R.string.collision) + " 💥", Toast.LENGTH_SHORT);
                FeedbackHandler.getInstance().vibrate(500);
                FeedbackHandler.getInstance().playAudio(R.raw.explosion);
            }

            @Override
            public void onCoinCollected() {
                FeedbackHandler.getInstance().toast("\uD83E\uDE99 " + getString(R.string.coin_collected) + " \uD83E\uDE99", Toast.LENGTH_SHORT);
                FeedbackHandler.getInstance().playAudio(R.raw.chime_sound);
            }

            @Override
            public void onGameOver() {
                FeedbackHandler.getInstance().toast("! " + getString(R.string.game_over) + " !", Toast.LENGTH_LONG);
                FeedbackHandler.getInstance().playAudio(R.raw.game_over);
            }
        });

        gameManager.initialize();
    }

    private void addControllersListeners() {
        gameOverImage.setOnClickListener(e -> {
            BundleWrapper bundleWrapper = new BundleWrapper(getIntent().getExtras());
            bundleWrapper.setUserDistance(gameManager.getScoreManager().getDistance());
            bundleWrapper.setUserNumCoins(gameManager.getScoreManager().getNumCoins());
            Intent intent = new Intent(this, HighScoresActivity.class);
            intent.putExtras(bundleWrapper.getBundle());
            startActivity(intent);
            finish();
            // startActivity(getIntent());
        });
    }

    @NonNull
    private BoardUI createBoardUI(boolean useSensor) {
        boolean bottomRowIsInput = !useSensor; // If we don't use sensor, it means we use the bottom row for buttons
        BoardUI boardUI = new BoardUI(NUM_COLUMNS, bottomRowIsInput, columnImages, heartImages);
        boardUI.drawBoard(R.drawable.ic_car);
        // Hide the obstacles
        boardUI.hideAllObstacles();
        // Start in the middle
        boardUI.setCarColumn(CAR_INITIAL_COLUMN);
        // Draw arrow controllers
        boardUI.drawControlsRow(R.drawable.ic_arrow, true);
        return boardUI;
    }

    private void gameOver() {
        gameManager.stop();
        gameTimer.stop();
        gameOverImage.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (gameManager.isGameOver()) return;
        if (countDownHandler.isPaused())
            countDownHandler.resume();
        if (!countDownHandler.isRunning()) {
            countDownHandler.start();
            countDownLabel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (countDownHandler.isRunning())
            countDownHandler.pause();
        else {
            gameManager.stop();
            gameTimer.stop();
        }
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