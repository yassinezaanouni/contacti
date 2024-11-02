package com.example.tp1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class Accueil extends AppCompatActivity {
    private static final String TAG = "Accueil";
    private MaterialCardView viewContactsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_accueil);
            
            // Hide action bar if needed
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }

            // Initialize views
            viewContactsButton = findViewById(R.id.all_contacts_button);

            // Set click listener
            viewContactsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(Accueil.this, Affichage.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e(TAG, "Error navigating to Affichage: ", e);
                        Toast.makeText(Accueil.this, "Error opening contacts", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: ", e);
            Toast.makeText(this, "Error initializing screen", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            // Show confirmation dialog before exiting
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Exit App")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        finishAffinity(); // This will close all activities
                    })
                    .setNegativeButton("No", null)
                    .show();
        } catch (Exception e) {
            Log.e(TAG, "Error handling back press: ", e);
            super.onBackPressed();
        }
    }
}