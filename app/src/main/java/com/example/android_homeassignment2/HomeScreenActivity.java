package com.example.android_homeassignment2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.android_homeassignment2.utils.ScreenUtils;
import com.google.android.material.textfield.TextInputLayout;

public class HomeScreenActivity extends AppCompatActivity {

    private SwitchCompat inputTypeSwitch, playSpeedSwitch;
    private EditText nameTextField;
    private TextInputLayout nameTextInputLayout;
    private Button startGameButton, highScoresButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ScreenUtils.hideSystemUI(this);
        findViews();

        registerListeners();
    }

    private void findViews() {
        inputTypeSwitch = findViewById(R.id.home_SWT_inputType);
        playSpeedSwitch = findViewById(R.id.home_SWT_playSpeed);
        nameTextField = findViewById(R.id.home_TXT_name);
        nameTextInputLayout = findViewById(R.id.home_TXT_nameLayout);
        startGameButton = findViewById(R.id.home_BTN_start);
        highScoresButton = findViewById(R.id.home_BTN_highScores);
    }

    private void registerListeners() {
        nameTextField.addTextChangedListener(nameTextWatcher);
        startGameButton.setOnClickListener(v -> loadGame());
        highScoresButton.setOnClickListener(v -> loadHighScores());
    }

    private final TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(@NonNull Editable s) {
            if (s.toString().trim().length() < 2) {
                nameTextInputLayout.setError(getString(R.string.name_too_short_error) + "!");
                startGameButton.setEnabled(false);
            } else {
                nameTextInputLayout.setError(null);
                startGameButton.setEnabled(true);
            }
        }
    };

    private void loadGame() {
        hideKeyboard();
        Intent intent = new Intent(this, GameActivity.class);
        BundleWrapper bundleWrapper = new BundleWrapper(new Bundle());
        bundleWrapper.setInputMethod(inputTypeSwitch.isChecked());
        bundleWrapper.setPlaySpeed(playSpeedSwitch.isChecked() ? 100 : 200);
        bundleWrapper.setUserName(nameTextField.getText().toString());
        intent.putExtras(bundleWrapper.getBundle());
        startActivity(intent);
    }

    private void loadHighScores() {
        hideKeyboard();
        Intent intent = new Intent(this, HighScoresActivity.class);
        startActivity(intent);
    }

    private void hideKeyboard() {
        // Source: https://stackoverflow.com/a/1109108
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            ScreenUtils.hideSystemUI(this);
        }
    }
}