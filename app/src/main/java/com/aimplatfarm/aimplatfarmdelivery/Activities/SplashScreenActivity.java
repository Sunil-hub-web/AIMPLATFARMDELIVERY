package com.aimplatfarm.aimplatfarmdelivery.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.aimplatfarm.aimplatfarmdelivery.R;
import com.aimplatfarm.aimplatfarmdelivery.session_manager.SessionManager;

public class SplashScreenActivity extends AppCompatActivity {
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sessionManager = new SessionManager(this);
        init();
    }

    private void init() {
        // loading the new activity
        final Handler handler = new Handler();
        handler.postDelayed(() -> checkToken(), 2000);
    }

    private void checkToken() {

        if (sessionManager.isLogin()) {
            // open the main activity
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

        } else {
            // ask the user to login
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}