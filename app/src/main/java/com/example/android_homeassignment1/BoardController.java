package com.example.android_homeassignment1;

public class BoardController {
    private final BoardElement[][] boardStatus;
    private int carColumn;

    public BoardController(int carColumn, int numRows, int numColumns) {
        this.carColumn = carColumn;
        this.boardStatus = new BoardElement[numRows][numColumns];
    }

    public void updateBoard() {
        // For the last row, we can't move the obstacle down. We need to check collision with the car
        int row = boardStatus.length - 1;
        for (int column = 0; column < boardStatus[row].length; column++) {
            if (boardStatus[row][column] == BoardElement.None) continue;
            boardStatus[row][column] = BoardElement.None;
            if (column == carColumn) {
                // TODO: 4/1/2023 Handle collision with car
            }
        }
        // And move all obstacles one row down.
        for (row = 0; row < boardStatus.length - 1; row++) {
            for (int column = 0; column < boardStatus[row].length; column++) {
                if (boardStatus[row][column] == BoardElement.None) continue;
                // Move the element down 1 row, and delete it from the current row.
                boardStatus[row + 1][column] = boardStatus[row][column];
                boardStatus[row][column] = BoardElement.None;
            }
        }
    }

    public int getCarColumn() {
        return carColumn;
    }

    public void moveCarLeft() {
        if (carColumn <= 0) return;
        carColumn--;
    }

    public void moveCarRight() {
        if (carColumn >= boardStatus[0].length) return;
        carColumn++;
    }
}
