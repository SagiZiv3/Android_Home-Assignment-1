package com.example.android_homeassignment2.input;

import android.view.View;

public class ButtonsInputHandler extends InputHandler {

    private final View leftButton, rightButton;

    public ButtonsInputHandler(MovementCallbacks movementCallbacks, View leftButton, View rightButton) {
        super(movementCallbacks);
        this.leftButton = leftButton;
        this.rightButton = rightButton;
    }

    @Override
    public void start() {
        leftButton.setOnClickListener(v -> moveLeft());
        rightButton.setOnClickListener(v -> moveRight());
    }

    @Override
    public void stop() {
        leftButton.setOnClickListener(null);
        rightButton.setOnClickListener(null);
    }
}
