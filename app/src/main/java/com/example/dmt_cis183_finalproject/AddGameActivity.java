package com.example.dmt_cis183_finalproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AddGameActivity extends AppCompatActivity {

    private EditText etTitle, etPrice, etReleaseDate, etDLC;
    private Spinner genreSpinner;
    private Button btnSaveGame, btnCancel, btnAddGenre;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        // Initialize views
        etTitle = findViewById(R.id.gameNameEditText);
        etPrice = findViewById(R.id.priceEditText);
        etReleaseDate = findViewById(R.id.releaseDateEditText);
        etDLC = findViewById(R.id.dlcEditText);
        genreSpinner = findViewById(R.id.genreSpinner);
        btnSaveGame = findViewById(R.id.postButton);
        btnCancel = findViewById(R.id.homeButton);
        btnAddGenre = findViewById(R.id.addGenreButton); // New button to add genres

        dbHelper = new DatabaseHelper(this);

        // Load genres into the spinner
        loadGenresIntoSpinner();

        // Save game on clicking the save button
        btnSaveGame.setOnClickListener(v -> {
            if (addGameToDatabase()) {
                setResult(RESULT_OK);
                finish();
            }
        });

        // Cancel and return to the previous screen
        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        // Add Genre button
        btnAddGenre.setOnClickListener(v -> {
            // Navigate to the Add Genre activity
            startActivity(new Intent(AddGameActivity.this, AddGenreActivity.class));
        });
    }

    private void loadGenresIntoSpinner() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.Table_Genres, null);

        List<String> genreNames = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String genreName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Key_Genre_Name));
                genreNames.add(genreName);
            } while (cursor.moveToNext());
        }

        cursor.close();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genreNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(spinnerAdapter);
    }

    private boolean addGameToDatabase() {
        String title = etTitle.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String releaseDate = etReleaseDate.getText().toString().trim();
        String dlcStr = etDLC.getText().toString().trim();

        if (title.isEmpty() || priceStr.isEmpty() || releaseDate.isEmpty() || dlcStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int dlc = Integer.parseInt(dlcStr);

            // Get selected genre from the spinner
            String selectedGenre = genreSpinner.getSelectedItem().toString();
            int genreID = getGenreID(selectedGenre);

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.Key_Title, title);
            values.put(DatabaseHelper.Key_Price, price);
            values.put(DatabaseHelper.Key_Release_Date, releaseDate);
            values.put(DatabaseHelper.Key_DLC, dlc);
            values.put(DatabaseHelper.Key_Genre_ID, genreID);

            long result = db.insert(DatabaseHelper.Table_Games, null, values);

            if (result == -1) {
                Toast.makeText(this, "Failed to add game", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                Toast.makeText(this, "Game added successfully", Toast.LENGTH_SHORT).show();
                return true;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input. Please check your entries.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private int getGenreID(String genreName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT GenreID FROM " + DatabaseHelper.Table_Genres + " WHERE " + DatabaseHelper.Key_Genre_Name + " = ?", new String[]{genreName});

        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Key_Genre_ID));
        }

        cursor.close();
        return -1;  // Return -1 if no genre was found
    }

    private void addNewGenre(String genreName) {
        boolean success = dbHelper.addGenre(genreName);

        if (success) {
            loadGenresIntoSpinner();  // Refresh the spinner after adding a new genre
            Toast.makeText(AddGameActivity.this, "Genre added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AddGameActivity.this, "Failed to add genre", Toast.LENGTH_SHORT).show();
        }
    }
}