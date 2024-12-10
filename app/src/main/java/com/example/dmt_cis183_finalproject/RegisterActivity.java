package com.example.dmt_cis183_finalproject;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etFirstName, etLastName;
    private RadioGroup rgRole;
    private RadioButton rbPlayer, rbDeveloper;
    private Button btnCreateUser;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize database helper and UI components
        dbHelper = new DatabaseHelper(this);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        rgRole = findViewById(R.id.rgRole);
        rbPlayer = findViewById(R.id.rbPlayer);
        rbDeveloper = findViewById(R.id.rbDeveloper);
        btnCreateUser = findViewById(R.id.btnRegister);

        // Handle create user button click
        btnCreateUser.setOnClickListener(v -> createNewUser());
    }

    private void createNewUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();

        // Determine selected role
        int selectedRoleId = rgRole.getCheckedRadioButtonId();
        String role = (selectedRoleId == R.id.rbPlayer) ? "player" : "developer";

        if (username.isEmpty() || password.isEmpty() || role == null) {
            Toast.makeText(this, "Please fill all fields and select a role.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert new user into the database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Key_Username, username);
        values.put(DatabaseHelper.Key_Password, password);
        values.put(DatabaseHelper.Key_Role, role); // Set the role here
        long userId = db.insert(DatabaseHelper.Table_Users, null, values);

        if (userId != -1) {
            Toast.makeText(this, "Account created successfully! Welcome, " + username, Toast.LENGTH_SHORT).show();
            finish(); // Close activity and return to login
        } else {
            Toast.makeText(this, "Error creating account. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
