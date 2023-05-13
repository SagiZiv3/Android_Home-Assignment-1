package com.example.android_homeassignment2.board;

import android.util.Log;

import com.example.android_homeassignment2.utils.RandomUtils;

public class BoardManager {

    private final BoardUI boardUI;
    private final BoardController boardController;
    private final int numRows, numColumns;
    private CollisionListener collisionListener;
    private int nextAsteroidCountdown;
    private boolean gameOver;

    public BoardManager(BoardUI boardUI, BoardController boardController, int numRows, int numColumns) {
        this.boardUI = boardUI;
        this.boardController = boardController;
        this.numRows = numRows;
        this.numColumns = numColumns;
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
        BoardElement collidedWith = updateBoard();

        updateBoardUI();
        if (collidedWith != BoardElement.None) {
            collisionListener.onCollision(collidedWith);
        }
    }

    private BoardElement updateBoard() {
        boardUI.hideAllObstacles();
        BoardElement collisionOccurred = boardController.updateBoard();

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
                BoardElement boardElement = boardController.getElementAt(row, col);
                if (boardElement != BoardElement.None) {
                    boardUI.showObstacleAt(row, col, boardElement.getImage());
                }
            }
        }
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void moveCarLeft() {
        boardController.moveCarLeft();
        boardUI.setCarColumn(boardController.getCarColumn());
    }

    public void moveCarRight() {
        boardController.moveCarRight();
        boardUI.setCarColumn(boardController.getCarColumn());
    }

    public void updateHealth(int health){
        boardUI.updateHealth(health);
    }

    public void setCollisionListener(CollisionListener collisionListener) {
        this.collisionListener = collisionListener;
    }

    public interface CollisionListener {
        void onCollision(BoardElement collidedWith);
    }
}
