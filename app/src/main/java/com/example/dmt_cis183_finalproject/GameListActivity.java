package com.example.dmt_cis183_finalproject;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GameListActivity extends AppCompatActivity {

    private ListView lvGames;
    private Button btnAddGame, btnSearch, btnLogout;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        lvGames = findViewById(R.id.lvGames);
        btnAddGame = findViewById(R.id.newGameButton);
        btnSearch = findViewById(R.id.searchButton);
        btnLogout = findViewById(R.id.logoutButton);
        db = new DatabaseHelper(this);

        // Initialize game list
        loadGameList();

        // Button for adding new games
        btnAddGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Activity for adding new game
                Intent intent = new Intent(GameListActivity.this, AddGameActivity.class);
                startActivity(intent);
            }
        });

        // Button for searching games
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Activity for searching games
                Intent intent = new Intent(GameListActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        // Button for logging out
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform logout logic (Clear session, etc.)
                Intent intent = new Intent(GameListActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // End current activity
            }
        });

        // Set an item click listener to open GameDetailsActivity when a game is clicked
        lvGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected game title
                String selectedGameTitle = (String) parent.getItemAtPosition(position);

                // Create an intent to start GameDetailsActivity
                Intent intent = new Intent(GameListActivity.this, GameDetailsActivity.class);
                intent.putExtra("gameTitle", selectedGameTitle);
                startActivity(intent);
            }
        });
    }

    private void loadGameList() {
        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.Table_Games, null);

        // Create an array list to store game titles
        ArrayList<String> gameTitles = new ArrayList<>();

        // Iterate through the cursor to get the game titles
        while (cursor.moveToNext()) {
            String gameTitle = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Key_Title)); // Use the constant for the column name
            gameTitles.add(gameTitle);
        }

        // Set the adapter for the ListView to display game titles
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gameTitles);
        lvGames.setAdapter(adapter);

        // Close the cursor after use
        cursor.close();
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadGameList();  // Reload the game list every time we return to this activity
    }
}