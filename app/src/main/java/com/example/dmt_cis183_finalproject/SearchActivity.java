package com.example.dmt_cis183_finalproject;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.widget.Button;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {

    private Button homeButton;
    private Button btnSearchGames;
    private EditText etSearchQuery;
    private ListView lvSearchResults;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize UI components
        homeButton = findViewById(R.id.homeButton);
        btnSearchGames = findViewById(R.id.btnSearchGames);
        etSearchQuery = findViewById(R.id.etSearchQuery);
        lvSearchResults = findViewById(R.id.searchResultsListView);

        // Set up Home button functionality
        homeButton.setOnClickListener(v -> finish()); // Navigates back to GameListActivity

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Set up search button functionality
        btnSearchGames.setOnClickListener(v -> {
            String query = etSearchQuery.getText().toString().trim();
            if (!query.isEmpty()) {
                searchGames(query);
            } else {
                Toast.makeText(SearchActivity.this, "Please enter a search term", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchGames(String query) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Perform search query in the database
        Cursor cursor = db.rawQuery(
                "SELECT Games.GameID AS _id, Games.Title, Genres.GenreName, Games.Price " +
                        "FROM Games " +
                        "JOIN Genres ON Games.GenreID = Genres.GenreID " +
                        "WHERE Games.Title LIKE ? OR Genres.GenreName LIKE ? OR Games.Price LIKE ?",
                new String[]{"%" + query + "%", "%" + query + "%", "%" + query + "%"}
        );

        if (cursor != null && cursor.getCount() > 0) {
            // Populate ListView with search results
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this,
                    android.R.layout.simple_list_item_2, // Built-in layout for two lines
                    cursor,
                    new String[]{"Title", "GenreName"},
                    new int[]{android.R.id.text1, android.R.id.text2}, // Title and Genre in ListView
                    0
            );
            lvSearchResults.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show();
        }
    }
}