package com.example.android_homeassignment2;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.android_homeassignment2.entities.User;

import java.util.Locale;

public class UserViewHandler {
    private TextView nameTextView, distanceTextView, coinsTextView, positionTextView;
    private ImageView mapIconImageView;
    private View rootView;

    public void findViews(@NonNull View view) {
        nameTextView = view.findViewById(R.id.list_LBL_username);
        distanceTextView = view.findViewById(R.id.list_LBL_distance);
        coinsTextView = view.findViewById(R.id.list_LBL_coins);
        positionTextView = view.findViewById(R.id.list_LBL_position);
        mapIconImageView = view.findViewById(R.id.list_IMG_map);
        rootView = view.findViewById(R.id.list_LAY_root);
    }

    public void showUserData(@NonNull User user, int position) {
        nameTextView.setText(user.getName());
        distanceTextView.setText(String.format(Locale.getDefault(),
                "%s %d", rootView.getContext().getString(R.string.distance), user.getDistance()));
        coinsTextView.setText(String.format(Locale.getDefault(),
                "%s %d", rootView.getContext().getString(R.string.coins), user.getNumCoins()));
        positionTextView.setText(String.format(Locale.getDefault(), "%02d)", position));

        if (user.getLocation() == null) {
            mapIconImageView.setEnabled(false);
            mapIconImageView.setImageResource(R.drawable.map_pin_crossed);
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mapIconImageView.setOnClickListener(onClickListener);
    }

    public void toMarkedUserStyle() {
        Context context = rootView.getContext();
        mapIconImageView.setColorFilter(ContextCompat.getColor(context, R.color.purple_500));
        rootView.setBackgroundResource(R.color.purple_200);

    }
}
