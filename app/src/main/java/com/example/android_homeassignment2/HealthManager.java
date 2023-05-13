package com.example.android_homeassignment2;

public class HealthManager {
    private int health;
    private boolean gameOver;
    private HealthListener healthListener;
    private GameOverListener gameOverListener;

    public HealthManager(int maxHealth) {
        this.health = maxHealth;
        gameOver = false;
    }

    public void reduceHealth() {
        health--;
        healthListener.onHealthChanged(health);
        if (health == 0) {
            gameOver = true;
            if (gameOverListener != null) {
                gameOverListener.onGameOver();
            }
        }
    }

    public int getHealth() {
        return health;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setHealthListener(HealthListener healthListener) {
        this.healthListener = healthListener;
    }

    public void setGameOverListener(GameOverListener gameOverListener) {
        this.gameOverListener = gameOverListener;
    }

    public interface GameOverListener {
        void onGameOver();
    }

    public interface HealthListener {
        void onHealthChanged(int newHealth);
    }
}
