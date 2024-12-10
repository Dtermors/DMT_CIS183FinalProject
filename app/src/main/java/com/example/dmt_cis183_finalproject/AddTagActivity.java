package com.example.dmt_cis183_finalproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddTagActivity extends AppCompatActivity {

    private EditText tagNameEditText;
    private Button addButton;
    private Button homeButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);

        tagNameEditText = findViewById(R.id.tagNameEditText);
        addButton = findViewById(R.id.addButton);
        homeButton = findViewById(R.id.homeButton);

        dbHelper = new DatabaseHelper(this);

        addButton.setOnClickListener(v -> addTag());
        homeButton.setOnClickListener(v -> finish()); // Navigates back to GameListActivity
    }

    private void addTag() {
        String tagName = tagNameEditText.getText().toString();

        if (tagName.isEmpty()) {
            Toast.makeText(this, "Please enter a tag name", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Key_Genre_Name, tagName);

        long result = db.insert(DatabaseHelper.Table_Genres, null, values);
        if (result != -1) {
            Toast.makeText(this, "Tag added successfully", Toast.LENGTH_SHORT).show();
            finish(); // Navigates back to GameListActivity
        } else {
            Toast.makeText(this, "Failed to add tag", Toast.LENGTH_SHORT).show();
        }
    }
}