package com.example.android_homeassignment1;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class FeedbackHandler {
    private Context context;
    private Vibrator vibrator;


    private static FeedbackHandler mySignal;

    private FeedbackHandler(Context context) {
        this.context = context;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public static void init(Context context) {
        if (mySignal == null) {
            mySignal = new FeedbackHandler(context.getApplicationContext());
        }
    }

    public static FeedbackHandler getInstance() {
        return mySignal;
    }


    public void vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrator.vibrate(500);
        }
    }

    @IntDef({Toast.LENGTH_SHORT, Toast.LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ToastDuration {
    }

    public void toast(String message, @ToastDuration int toastLength) {
        new Handler(Looper.getMainLooper()).post(() -> {
            try {
                Toast.makeText(context, message, toastLength).show();
            } catch (IllegalStateException ignored) {
            }
        });
    }

    public void toast(String message) {
        toast(message, Toast.LENGTH_SHORT);
    }
}
