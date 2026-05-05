package com.cleanerapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.cleanerapp.R;
import com.cleanerapp.ui.cleaner.CleanerDashboardActivity;
import com.cleanerapp.ui.user.UserHomeActivity;
import com.cleanerapp.utils.SessionManager;

import dagger.hilt.android.AndroidEntryPoint;

import javax.inject.Inject;

@AndroidEntryPoint
public class SplashActivity extends AppCompatActivity {

    @Inject
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (sessionManager.isLoggedIn()) {
                if (sessionManager.isCleaner()) {
                    startActivity(new Intent(this, CleanerDashboardActivity.class));
                } else {
                    startActivity(new Intent(this, UserHomeActivity.class));
                }
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
        }, 2000);
    }
}
