package com.example.dmt_cis183_finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameDetailsActivity extends AppCompatActivity {
    private TextView tvGameTitle, tvGameDetails;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        tvGameTitle = findViewById(R.id.tvGameTitle);
        tvGameDetails = findViewById(R.id.tvGameDetails);
        Button btnEditGame = findViewById(R.id.btnEditGame);
        Button homebtn = findViewById(R.id.homeButton);

        db = new DatabaseHelper(this);

        String gameTitle = getIntent().getStringExtra("gameTitle");
        loadGameDetails(gameTitle);

        btnEditGame.setOnClickListener(v -> {
            Intent intent = new Intent(GameDetailsActivity.this, AddGameActivity.class);
            intent.putExtra("gameTitle", gameTitle);
            startActivity(intent);
        });

        homebtn.setOnClickListener(v -> finish());
    }

    private void loadGameDetails(String gameTitle) {
        SQLiteDatabase database = db.getReadableDatabase();
        // Query the Games table for the game title
        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + DatabaseHelper.Table_Games + " WHERE Title = ?",
                new String[]{gameTitle});

        if (cursor.moveToFirst()) {
            // Get genre ID from the game details
            int genreID = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Key_Genre_ID));

            // Query the Genres table for the genre name based on the genre ID
            Cursor genreCursor = database.rawQuery(
                    "SELECT " + DatabaseHelper.Key_Genre_Name + " FROM " + DatabaseHelper.Table_Genres + " WHERE GenreID = ?",
                    new String[]{String.valueOf(genreID)});

            String genreName = "";
            if (genreCursor.moveToFirst()) {
                genreName = genreCursor.getString(genreCursor.getColumnIndex(DatabaseHelper.Key_Genre_Name));
            }
            genreCursor.close();

            // Now retrieve other game details
            String details = "Price: $" + cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.Key_Price)) +
                    "\nGenre: " + genreName +
                    "\nRelease Date: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.Key_Release_Date)) +
                    "\nDLC: " + (cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Key_DLC)) == 1 ? "Yes" : "No");

            tvGameTitle.setText(gameTitle);
            tvGameDetails.setText(details);
        }
        cursor.close();
    }
}