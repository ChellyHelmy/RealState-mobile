package com.poly.realstate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3000; // 3 seconds delay
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // ✅ Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();

        // ✅ Full screen (hide status bar & nav bar)
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );

        // ✅ Delayed redirect
        new Handler().postDelayed(() -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser != null) {
                // ✅ User already logged in → HomeActivity
                startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
            } else {
                // ✅ Not logged in → SignInActivity
                startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
            }

            finish(); // close splash screen

        }, SPLASH_DELAY);
    }
}
