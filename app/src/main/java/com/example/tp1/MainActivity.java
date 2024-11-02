package com.example.tp1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextInputEditText usernameInput, passwordInput;
    private MaterialCheckBox stayConnectedCheckbox;
    private MaterialButton signInButton, exitButton;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_STAY_CONNECTED = "stayConnected";
    
    // Default credentials - in a real app, these would be stored securely
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            setContentView(R.layout.activity_main);
            
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }

            sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            
            // Initialize default credentials if first time
            initializeDefaultCredentials();
            
            initializeViews();
            checkPreviousLogin();
            setupClickListeners();

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: ", e);
            Toast.makeText(this, "An error occurred while starting the app", Toast.LENGTH_LONG).show();
        }
    }

    private void initializeDefaultCredentials() {
        if (!sharedPreferences.contains(KEY_USERNAME)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_USERNAME, DEFAULT_USERNAME);
            editor.putString(KEY_PASSWORD, DEFAULT_PASSWORD);
            editor.apply();
        }
    }

    private void handleLogin() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (validateCredentials(username, password)) {
            if (stayConnectedCheckbox.isChecked()) {
                saveLoginState(username);
            }
            navigateToAccueil();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            passwordInput.setText("");
        }
    }

    private boolean validateCredentials(String username, String password) {
        try {
            // Clear any previous errors
            usernameInput.setError(null);
            passwordInput.setError(null);

            // Check for empty fields
            if (TextUtils.isEmpty(username)) {
                usernameInput.setError("Username is required");
                usernameInput.requestFocus();
                return false;
            }

            if (TextUtils.isEmpty(password)) {
                passwordInput.setError("Password is required");
                passwordInput.requestFocus();
                return false;
            }

            // Validate against stored credentials
            String storedUsername = sharedPreferences.getString(KEY_USERNAME, DEFAULT_USERNAME);
            String storedPassword = sharedPreferences.getString(KEY_PASSWORD, DEFAULT_PASSWORD);

            return username.equals(storedUsername) && password.equals(storedPassword);

        } catch (Exception e) {
            Log.e(TAG, "Error validating credentials: ", e);
            return false;
        }
    }

    // Add a method to change password (you can call this from a settings screen)
    public void changePassword(String oldPassword, String newPassword) {
        String storedPassword = sharedPreferences.getString(KEY_PASSWORD, DEFAULT_PASSWORD);
        
        if (oldPassword.equals(storedPassword)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_PASSWORD, newPassword);
            editor.apply();
            Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    // Add a method to logout
    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_STAY_CONNECTED);
        editor.apply();
        
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void initializeViews() {
        try {
            usernameInput = findViewById(R.id.auth_nom);
            passwordInput = findViewById(R.id.auth_pwd);
            stayConnectedCheckbox = findViewById(R.id.stay_connected_btn);
            signInButton = findViewById(R.id.auth_validate_button);
            exitButton = findViewById(R.id.auth_quit_button);
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views: ", e);
            throw e;
        }
    }

    private void checkPreviousLogin() {
        try {
            if (sharedPreferences.getBoolean(KEY_STAY_CONNECTED, false)) {
                String savedUsername = sharedPreferences.getString(KEY_USERNAME, "");
                if (!TextUtils.isEmpty(savedUsername)) {
                    navigateToAccueil();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error checking previous login: ", e);
        }
    }

    private void setupClickListeners() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    handleLogin();
                } catch (Exception e) {
                    Log.e(TAG, "Error in login button click: ", e);
                    Toast.makeText(MainActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveLoginState(String username) {
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_USERNAME, username);
            editor.putBoolean(KEY_STAY_CONNECTED, true);
            editor.apply();
        } catch (Exception e) {
            Log.e(TAG, "Error saving login state: ", e);
        }
    }

    private void navigateToAccueil() {
        try {
            Intent intent = new Intent(MainActivity.this, Accueil.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Error navigating to Accueil: ", e);
            Toast.makeText(this, "Error opening main screen", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (isTaskRoot()) {
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Exit App")
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes", (dialog, which) -> finish())
                        .setNegativeButton("No", null)
                        .show();
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error handling back press: ", e);
            super.onBackPressed();
        }
    }
}