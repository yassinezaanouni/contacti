package com.example.tp1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 3000; // 5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        ImageView splashImage = findViewById(R.id.splash_logo);

        // Load the animated vector drawable
        AnimatedVectorDrawableCompat avdCompat = AnimatedVectorDrawableCompat.create(this, R.drawable.animated_logo);
        splashImage.setImageDrawable(avdCompat);

        // Start the animation
        if (avdCompat != null) {
            avdCompat.start();
        }

        // Delay for 5 seconds before launching MainActivity
        new Handler().postDelayed(() -> {
            // Start MainActivity
            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish(); // Close the SplashActivity so it's not in the back stack
        }, SPLASH_DISPLAY_LENGTH);
    }
}