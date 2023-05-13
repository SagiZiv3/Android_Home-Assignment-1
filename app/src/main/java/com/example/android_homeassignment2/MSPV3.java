package com.example.android_homeassignment2;

import android.content.Context;
import android.content.SharedPreferences;

public class MSPV3 {
    private static final String SP_FILE_NAME = "SP_FILE_NAME";
    private SharedPreferences prefs = null;

    private static MSPV3 instance;

    private MSPV3(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new MSPV3(context);
        }
    }

    public static MSPV3 getInstance() {
        return instance;
    }

    public void saveString(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String readString(String key, String def) {
        return prefs.getString(key, def);
    }
}
