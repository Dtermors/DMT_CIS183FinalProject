package com.example.dmt_cis183_finalproject;

import android.os.Bundle;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddGenreActivity extends AppCompatActivity {

    private EditText etGenreName;
    private Button btnAddGenre, btnCancel;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_genre);

        etGenreName = findViewById(R.id.genreNameEditText);
        btnAddGenre = findViewById(R.id.addGenreButtonTo);
        btnCancel = findViewById(R.id.cancelButton);
        dbHelper = new DatabaseHelper(this);

        btnAddGenre.setOnClickListener(v -> {
            String genreName = etGenreName.getText().toString().trim();
            if (!genreName.isEmpty()) {
                addGenreToDatabase(genreName);
                finish(); // Close this activity after adding genre
            } else {
                Toast.makeText(this, "Please enter a genre", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> finish());
    }

    private void addGenreToDatabase(String genreName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Key_Genre_Name, genreName);  // Assuming column name is Key_Genre_Name

        long result = db.insert(DatabaseHelper.Table_Genres, null, values);

        if (result == -1) {
            Toast.makeText(this, "Failed to add genre", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Genre added successfully", Toast.LENGTH_SHORT).show();
        }
    }
}