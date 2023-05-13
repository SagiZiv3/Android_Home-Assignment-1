package com.example.android_homeassignment2;

import androidx.annotation.NonNull;

import com.example.android_homeassignment2.board.BoardElement;
import com.example.android_homeassignment2.board.BoardManager;
import com.example.android_homeassignment2.input.InputHandler;

public class GameManager {
    private final BoardManager boardManager;
    private final HealthManager healthManager;
    private final ScoreManager scoreManager;
    private final InputHandler inputHandler;
    private GameOverListener gameOverListener;
    private FeedbackCallbacks feedbackCallbacks;

    public GameManager(BoardManager boardManager, HealthManager healthManager, ScoreManager scoreManager, InputHandler inputHandler) {
        this.boardManager = boardManager;
        this.healthManager = healthManager;
        this.scoreManager = scoreManager;
        this.inputHandler = inputHandler;
    }

    public void initialize() {
        boardManager.setCollisionListener(this::onCollision);
        healthManager.setGameOverListener(this::gameOver);
        healthManager.setHealthListener(boardManager::updateHealth);
    }

    public void start() {
        inputHandler.start();
    }

    public void stop() {
        inputHandler.stop();
    }

    public void Tick() {
        boardManager.Tick();
        scoreManager.increaseDistance();
    }

    public void setGameOverListener(GameOverListener gameOverListener) {
        this.gameOverListener = gameOverListener;
    }

    public void setFeedbackCallbacks(FeedbackCallbacks feedbackCallbacks) {
        this.feedbackCallbacks = feedbackCallbacks;
    }

    private void onCollision(@NonNull BoardElement boardElement) {
        switch (boardElement) {
            case Rock:
                feedbackCallbacks.onCollision();
                healthManager.reduceHealth();
                break;
            case Coin:
                feedbackCallbacks.onCoinCollected();
                scoreManager.coinCollected();
                break;
        }
    }

    private void gameOver() {
        boardManager.setGameOver(true);
        gameOverListener.onGameOver();
        feedbackCallbacks.onGameOver();
    }

    public boolean isGameOver() {
        return healthManager.isGameOver();
    }

    public void moveCarLeft() {
        boardManager.moveCarLeft();
    }

    public void moveCarRight() {
        boardManager.moveCarRight();
    }

    public ScoreManager getScoreManager(){
        return scoreManager;
    }

    public interface GameOverListener {
        void onGameOver();
    }

    public interface FeedbackCallbacks {
        void onCollision();

        void onCoinCollected();

        void onGameOver();
    }
}
