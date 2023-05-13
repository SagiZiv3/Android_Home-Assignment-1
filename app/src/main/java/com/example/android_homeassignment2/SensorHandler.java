package com.example.android_homeassignment2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class SensorHandler implements SensorEventListener {

    private final SensorManager sensorManager;
    private final Sensor sensor;
    private final int sensorType, sensorDelay;
    private ValueChangedCallback valueChangedCallback;

    public SensorHandler(@SensorType int sensorType, @SensorDelay int sensorDelay, Context context) {
        this.sensorType = sensorType;
        this.sensorDelay = sensorDelay;

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensorType);
        if (sensor == null) {
            throw new RuntimeException("Unable to find required sensor");
        }
    }

    public SensorHandler(@SensorType int sensorType, Context context) {
        this(sensorType, SensorManager.SENSOR_DELAY_NORMAL, context);
    }

    public void setValueChangedCallback(ValueChangedCallback valueChangedCallback) {
        this.valueChangedCallback = valueChangedCallback;
    }

    public void start() {
        sensorManager.registerListener(this, sensor, sensorDelay);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(@NonNull SensorEvent event) {
        if (event.sensor.getType() != sensorType)
            return;

        valueChangedCallback.onValuesChanged(event.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface ValueChangedCallback {
        void onValuesChanged(float[] newValues);
    }

    //<editor-fold desc="Annotations">
    @IntDef({Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_MAGNETIC_FIELD, Sensor.TYPE_GYROSCOPE,
            Sensor.TYPE_LIGHT, Sensor.TYPE_PRESSURE, Sensor.TYPE_PROXIMITY,
            Sensor.TYPE_GRAVITY, Sensor.TYPE_LINEAR_ACCELERATION,
            Sensor.TYPE_ROTATION_VECTOR, Sensor.TYPE_RELATIVE_HUMIDITY,
            Sensor.TYPE_AMBIENT_TEMPERATURE, Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED,
            Sensor.TYPE_GAME_ROTATION_VECTOR, Sensor.TYPE_GYROSCOPE_UNCALIBRATED})
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.PARAMETER)
    public @interface SensorType {
    }

    @IntDef({SensorManager.SENSOR_DELAY_UI, SensorManager.SENSOR_DELAY_NORMAL,
            SensorManager.SENSOR_DELAY_GAME, SensorManager.SENSOR_DELAY_FASTEST})
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.PARAMETER)
    public @interface SensorDelay {
    }
    //</editor-fold>
}
