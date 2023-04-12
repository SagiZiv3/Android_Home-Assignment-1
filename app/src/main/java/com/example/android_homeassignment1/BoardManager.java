package com.example.android_homeassignment1;

import android.util.Log;

public class BoardManager {

    private final BoardUI boardUI;
    private final BoardController boardController;
    private final int numRows, numColumns;
    private GameOverListener gameOverListener;
    private CollisionListener collisionListener;
    private int health, nextAsteroidCountdown;
    private boolean gameOver;

    public BoardManager(BoardUI boardUI, BoardController boardController, int numRows, int numColumns, int maxHealth) {
        this.boardUI = boardUI;
        this.boardController = boardController;
        this.numRows = numRows;
        this.numColumns = numColumns;
        health = maxHealth;
        nextAsteroidCountdown = RandomUtils.getInstance().nextInt(7, 15);
        gameOver = false;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void Tick() {
        if (gameOver) {
            Log.w("pfff", "Game over, but manager still ticks!");
            return;
        }
        boolean collisionOccurred = updateBoard();

        updateBoardUI();
        if (collisionOccurred) {
            handleCollision();
        }
    }

    private boolean updateBoard() {
        boardUI.hideAllObstacles();
        boolean collisionOccurred = boardController.updateBoard();

        addAsteroidIfNeeded();
        return collisionOccurred;
    }

    private void addAsteroidIfNeeded() {
        nextAsteroidCountdown--;
        if (nextAsteroidCountdown == 0) {
            nextAsteroidCountdown = RandomUtils.getInstance().nextInt(3, 6);
//            System.out.println("Adding asteroid in " + nextAsteroidCountdown + " ticks");
            boardController.addObstacle();
        }
    }

    private void updateBoardUI() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                if (boardController.getElementAt(row, col) != BoardElement.None) {
                    boardUI.showObstacleAt(row, col);
                }
            }
        }
    }

    private void handleCollision() {
        health--;
        boardUI.updateHealth(health);
        if (collisionListener != null)
            collisionListener.onCollision();
        if (health == 0) {
            gameOver = true;
            if (gameOverListener != null) {
                gameOverListener.onGameOver();
            }
        }
    }

    public void moveCarLeft() {
        boardController.moveCarLeft();
        boardUI.setCarColumn(boardController.getCarColumn());
    }

    public void moveCarRight() {
        boardController.moveCarRight();
        boardUI.setCarColumn(boardController.getCarColumn());
    }

    public void setGameOverListener(GameOverListener gameOverListener) {
        this.gameOverListener = gameOverListener;
    }

    public void setCollisionListener(CollisionListener collisionListener) {
        this.collisionListener = collisionListener;
    }

    public interface CollisionListener {
        void onCollision();
    }

    public interface GameOverListener {
        void onGameOver();
    }
}
