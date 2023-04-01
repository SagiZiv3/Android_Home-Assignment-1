package com.example.android_homeassignment1;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

public class BoardUI {

    private final int numColumnsPerRow;
    private final boolean bottomRowIsInput;
    private final ViewsCollection<ImageView> columnImages, heartImages;

    public BoardUI(int numColumnsPerRow, boolean bottomRowIsInput, ViewsCollection<ImageView> columnImages, ViewsCollection<ImageView> heartImages) {
        this.numColumnsPerRow = numColumnsPerRow;
        this.bottomRowIsInput = bottomRowIsInput;
        this.columnImages = columnImages;
        this.heartImages = heartImages;
    }

    public void drawBoard(@DrawableRes int obstacleImageId, @DrawableRes int carImageId) {
        int index = columnImages.getStartIndex();
        int endIndex = getCarColumnIndex();

        // Draw the obstacles
        for (; index <= endIndex; index += numColumnsPerRow) {
            for (int offset = 0; offset < numColumnsPerRow; offset++)
                columnImages.getView(index + offset).setImageResource(obstacleImageId);
        }

        // Draw the car.
        endIndex += numColumnsPerRow;
        for (; index <= endIndex; index++) {
            columnImages.getView(index).setImageResource(carImageId);
        }
    }

    public void drawControlsRow(@DrawableRes int arrowIcon, boolean flipLeft){
        if (!bottomRowIsInput) return;
        int controlsIndex = getCarColumnIndex() + numColumnsPerRow + columnImages.getStartIndex();
        columnImages.getView(controlsIndex).setImageResource(arrowIcon);
        if (flipLeft)
            columnImages.getView(controlsIndex).setScaleX(-1f);
        columnImages.getView(controlsIndex+2).setImageResource(arrowIcon);
        if (!flipLeft)
            columnImages.getView(controlsIndex + 2).setScaleX(-1f);
        columnImages.getView(controlsIndex+1).setVisibility(View.INVISIBLE);
    }

    public void hideAllObstacles() {
        int index = columnImages.getStartIndex();
        int endIndex = getCarColumnIndex();

        // Draw the obstacles
        for (; index <= endIndex; index += numColumnsPerRow) {
            for (int offset = 0; offset < numColumnsPerRow; offset++)
                columnImages.getView(index + offset).setVisibility(View.INVISIBLE);
        }
    }

    public void updateHealth(int health) {
        heartImages.getView(health + heartImages.getStartIndex()).setVisibility(View.INVISIBLE);
    }

    public int getNumRows() {
        return getCarColumnIndex() / numColumnsPerRow;
    }

    public void showObstacleAt(int row, int column) {
        int index = getIndexFrom2DPosition(row, column);
        columnImages.getView(index).setVisibility(View.VISIBLE);
    }

    public void hideObstacleAt(int row, int column) {
        int index = getIndexFrom2DPosition(row, column);
        columnImages.getView(index).setVisibility(View.INVISIBLE);
    }

    public void setCarColumn(int newColumn) {
        if (newColumn < 0 || newColumn > numColumnsPerRow) {
            Log.w("pfff", "Invalid car column: " + newColumn);
            newColumn %= numColumnsPerRow;
        }
        // Hide the car
        for (int offset = columnImages.getStartIndex(); offset <= numColumnsPerRow; offset++)
            columnImages.getView(getCarColumnIndex() + offset).setVisibility(View.INVISIBLE);
        // Show the car in the new position
        columnImages.getView(getCarColumnIndex() + newColumn + 1).setVisibility(View.VISIBLE);
    }

    private int getIndexFrom2DPosition(int row, int column) {
        return columnImages.getStartIndex() + column + row * numColumnsPerRow;
    }

    private int getCarColumnIndex() {
        int carColumnIndex = columnImages.length() - numColumnsPerRow;
        if (bottomRowIsInput)
            carColumnIndex -= numColumnsPerRow;
        return carColumnIndex;
    }
}
