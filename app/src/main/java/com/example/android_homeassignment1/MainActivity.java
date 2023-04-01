package com.example.android_homeassignment1;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private ViewsCollection<ImageView> columnImages;
    private ViewsCollection<ImageView> heartImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("-----------------------------------------");
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

        BoardUI board = new BoardUI(3, false, columnImages);
        board.drawBoard(R.drawable.ic_rock, R.drawable.ic_car);
        // Hide the obstacles
        board.hideAllObstacles();
        // Start in the middle
        board.setCarColumn(1);
    }
}