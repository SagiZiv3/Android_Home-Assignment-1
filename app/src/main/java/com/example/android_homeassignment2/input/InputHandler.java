package com.example.android_homeassignment2.input;

public abstract class InputHandler {
    private final MovementCallbacks movementCallbacks;

    protected InputHandler(MovementCallbacks movementCallbacks) {
        this.movementCallbacks = movementCallbacks;
    }

    public abstract void start();

    public abstract void stop();

    protected void moveLeft() {
        movementCallbacks.moveLeft();
    }

    protected void moveRight() {
        movementCallbacks.moveRight();
    }

    void setSpeedMultiplier(float multiplier) {
        movementCallbacks.setSpeedMultiplier(multiplier);
    }

    public interface MovementCallbacks {
        void moveLeft();

        void moveRight();

        void setSpeedMultiplier(float multiplier);
    }
}
