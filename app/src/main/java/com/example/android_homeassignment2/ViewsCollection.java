package com.example.android_homeassignment2;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewsCollection<T extends View> {

    private final Map<Integer, T> views;
    private int startIndex;

    private ViewsCollection() {
        views = new HashMap<>();
        startIndex = Integer.MAX_VALUE;
    }

    public T getView(int index) {
        if (views.containsKey(index))
            return views.get(index);
        throw new IndexOutOfBoundsException("Can't find view with index " + index);
    }

    public Iterable<T> getIterator() {
        return views.values();
    }

    public int length() {
        return views.size();
    }

    public void print() {
        for (Map.Entry<Integer, T> mp : views.entrySet()) {
            System.out.println(mp.getKey() + "→" + mp.getValue());
        }
    }

    public int getStartIndex() {
        return startIndex;
    }

    public static <T extends View> ViewsCollection<T> findViewsForPattern(Activity application, @NonNull Field[] allIds, Pattern pattern) {
        ViewsCollection<T> instance = new ViewsCollection<>();
        for (Field field : allIds) {
            Matcher matcher = pattern.matcher(field.getName());
            if (!matcher.find())
                continue;
            try {
                String match = matcher.group(1);
                if (match == null) {
                    Log.e("pfff", "Invalid pattern, missing index group");
                    // No need to continue, the pattern is invalid.
                    return null;
                }
                int index = Integer.parseInt(match);
                instance.views.put(index, application.findViewById(field.getInt(null)));
                instance.startIndex = Integer.min(instance.startIndex, index);
            } catch (IllegalAccessException e) {
                System.err.printf("Bad field: %s%n", field.getName());
                Log.e("pfff", String.format("Bad field: %s%n", field.getName()), e);
            }
        }

        return instance;
    }
}
