package com.example.dmt_cis183_finalproject;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        // gets Users from the database for testing/milestone 1 allows for mass testing of tables as well as allows for implementation-
        //- of some of my practice code and testing ive been doing on the side its not pretty but it works for what it needs to do as a testing system
        getUsers();
        getGames();
        fetchRatings();
    }


    private void getUsers() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.Table_Users, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int usernameIndex = cursor.getColumnIndex(DatabaseHelper.Key_Username); // Use the constant
                if (usernameIndex != -1) {
                    String username = cursor.getString(usernameIndex);
                    Log.d(TAG, "Username: " + username);
                } else {
                    Log.d(TAG, "Username column does not exist in the table.");
                }
            }
            cursor.close();
        }
    }

    private void getGames() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.Table_Games, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int titleIndex = cursor.getColumnIndex(DatabaseHelper.Key_Title);
                if (titleIndex != -1) {
                    String title = cursor.getString(titleIndex);
                    Log.d(TAG, "Game Title: " + title);
                } else {
                    Log.d(TAG, "Title column does not exist in the table.");
                }
                int priceIndex = cursor.getColumnIndex(DatabaseHelper.Key_Price);
                if (priceIndex != -1) {
                    double price = cursor.getDouble(priceIndex);
                    Log.d(TAG, "Game Price: " + price);
                } else {
                    Log.d(TAG, "Price column does not exist in the table.");
                }

            }
            cursor.close();
        }
    }

    private void fetchRatings() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.Table_Ratings, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int ratingValueIndex = cursor.getColumnIndex(DatabaseHelper.Key_Rating_Value);
                if (ratingValueIndex != -1) {
                    int ratingValue = cursor.getInt(ratingValueIndex);
                    Log.d(TAG, "Rating Value: " + ratingValue);
                } else {
                    Log.d(TAG, "Rating Value column does not exist in the table.");
                }
                int commentIndex = cursor.getColumnIndex(DatabaseHelper.Key_Comment);
                if (commentIndex != -1) {
                    String comment = cursor.getString(commentIndex);
                    Log.d(TAG, "Comment: " + comment);
                } else {
                    Log.d(TAG, "Comment column does not exist in the table.");
                }

                int userIdIndex = cursor.getColumnIndex(DatabaseHelper.Key_Rating_User_ID);
                if (userIdIndex != -1) {
                    int userId = cursor.getInt(userIdIndex);
                    Log.d(TAG, "User ID: " + userId);
                } else {
                    Log.d(TAG, "User ID column does not exist in the table.");
                }

                int gameIdIndex = cursor.getColumnIndex(DatabaseHelper.Key_Rating_Game_ID);
                if (gameIdIndex != -1) {
                    int gameId = cursor.getInt(gameIdIndex);
                    Log.d(TAG, "Game ID: " + gameId);
                } else {
                    Log.d(TAG, "Game ID column does not exist in the table.");
                }
            }
            cursor.close();
        }
    }
}