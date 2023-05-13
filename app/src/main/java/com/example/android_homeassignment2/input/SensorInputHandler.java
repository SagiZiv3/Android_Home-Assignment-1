package com.example.android_homeassignment2.input;

import android.content.Context;
import android.hardware.Sensor;

import androidx.annotation.NonNull;

import com.example.android_homeassignment2.SensorHandler;

public class SensorInputHandler extends InputHandler {
    private final SensorHandler sensorHandler;
    private int carPosition;

    public SensorInputHandler(MovementCallbacks movementCallbacks, Context context) {
        super(movementCallbacks);
        carPosition = 0;
        sensorHandler = new SensorHandler(Sensor.TYPE_ACCELEROMETER, context);
        sensorHandler.setValueChangedCallback(this::onSensorValuesChanged);
    }

    @Override
    public void start() {
        sensorHandler.start();
    }

    @Override
    public void stop() {
        sensorHandler.stop();
    }

    private void onSensorValuesChanged(@NonNull float[] values) {
        float xTilt = values[0];
        handleCarMovement(xTilt);

        float yTilt = values[1];
        handlePlaySpeed(yTilt);
    }

    private void handlePlaySpeed(float yTilt) {
        // Clamp the value to valid range
        if (yTilt < -2)
            yTilt = -2;
        else if (yTilt > 8)
            yTilt = 8;
        // Map the value to the multiplier's range.
        float mappedValue = map(yTilt, -2, 8, 2.5f, 1);
        setSpeedMultiplier(mappedValue);
    }

    private static float map(float x, float inFrom, float inTo, float outFrom, float outTo) {
        // Source: https://arduino.stackexchange.com/a/32159
        return (x - inFrom) * (outTo - outFrom) / (inTo - inFrom) + outFrom;
    }

    private void handleCarMovement(float xTilt) {
        int targetPosition = getTargetPosition(xTilt);

        // Negative tilt means moving right.
        targetPosition *= -Math.signum(xTilt);
        moveCar(targetPosition);
    }

    private void moveCar(int targetPosition) {
        int numSteps = Math.abs(targetPosition - carPosition);
        if (targetPosition > carPosition) {
            moveRight(numSteps);
        } else {
            moveLeft(numSteps);
        }
        carPosition = targetPosition;
    }

    private static int getTargetPosition(float xTilt) {
        int targetPosition;
        if (Math.abs(xTilt) < 2)
            targetPosition = 0;
        else if (Math.abs(xTilt) < 4)
            targetPosition = 1;
        else
            targetPosition = 2;
        return targetPosition;
    }

    private void moveLeft(int numSteps) {
        for (int i = 0; i < numSteps; i++) {
            moveLeft();
        }
    }

    private void moveRight(int numSteps) {
        for (int i = 0; i < numSteps; i++) {
            moveRight();
        }
    }
}
