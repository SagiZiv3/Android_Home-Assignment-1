package com.example.android_homeassignment2.board;

import com.example.android_homeassignment2.utils.RandomUtils;

import java.util.Arrays;

public class BoardController {
    private final BoardElement[][] boardStatus;
    private int carColumn;

    public BoardController(int carColumn, int numRows, int numColumns) {
        this.carColumn = carColumn;
        this.boardStatus = new BoardElement[numRows][numColumns];
    }

    public void reset() {
        for (BoardElement[] row : boardStatus) {
            Arrays.fill(row, BoardElement.None);
        }
    }

    public BoardElement updateBoard() {
        BoardElement collidedWith = BoardElement.None;
        // For the last row, we can't move the obstacle down. We need to check collision with the car
        int row = boardStatus.length - 1;
        for (int column = 0; column < boardStatus[row].length; column++) {
            BoardElement boardElement = boardStatus[row][column];
            if (boardElement == BoardElement.None) continue;
            boardStatus[row][column] = BoardElement.None;
            if (column == carColumn) {
                collidedWith = boardElement;
            }
        }
        // And move all obstacles one row down.
        for (row = boardStatus.length - 1; row >= 0; row--) {
            for (int column = 0; column < boardStatus[row].length; column++) {
                if (boardStatus[row][column] == BoardElement.None) continue;
                // Move the element down 1 row, and delete it from the current row.
                boardStatus[row + 1][column] = boardStatus[row][column];
                boardStatus[row][column] = BoardElement.None;
            }
        }

        return collidedWith;
    }

    public int addObstacle() {
        int column = RandomUtils.getInstance().nextInt(boardStatus[0].length);
        boardStatus[0][column] = BoardElement.selectRandomBoardElement();
        return column;
    }

    public BoardElement getElementAt(int row, int column) {
        return boardStatus[row][column];
    }

    public int getCarColumn() {
        return carColumn;
    }

    public void moveCarLeft() {
        if (carColumn == 0) return;
        carColumn--;
    }

    public void moveCarRight() {
        if (carColumn == boardStatus[0].length - 1) return;
        carColumn++;
    }
}
