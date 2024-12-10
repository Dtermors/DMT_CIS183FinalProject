package com.example.dmt_cis183_finalproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnNewUser;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize database helper and UI components
        dbHelper = new DatabaseHelper(this);
        etUsername = findViewById(R.id.usernameEditText);
        etPassword = findViewById(R.id.passwordEditText);
        btnLogin = findViewById(R.id.loginButton);
        btnNewUser = findViewById(R.id.registerButton);

        // Handle login button click
        btnLogin.setOnClickListener(v -> loginUser());

        // Handle new user button click
        btnNewUser.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Authenticate user
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT Role FROM Users WHERE Username = ? AND Password = ?",
                new String[]{username, password});

        if (cursor.moveToFirst()) {
            String userRole = cursor.getString(0); // Retrieve user role (player or developer)
            Toast.makeText(this, "Login successful! Welcome, " + username, Toast.LENGTH_SHORT).show();

            // Navigate to the Game List screen
            Intent intent = new Intent(MainActivity.this, GameListActivity.class);
            intent.putExtra("username", username); // Pass username to next activity
            intent.putExtra("userRole", userRole); // Pass role to next activity
            startActivity(intent);

            // Clear input fields
            etUsername.setText("");
            etPassword.setText("");
        } else {
            Toast.makeText(this, "Invalid username or password. Please try again.", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }
}