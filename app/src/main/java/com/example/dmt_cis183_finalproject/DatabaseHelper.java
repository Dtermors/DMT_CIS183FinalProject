package com.example.dmt_cis183_finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final int Database_Version = 1;
    private static final String Database_Name = "gameCatalog";
    //for the purpous of testing some of these are made public but might not be by the end of the project
    // Table names
    public static final String Table_Users = "Users";
    public static final String Table_Games = "Games";
    public static final String Table_Genres = "Genres";
    public static final String Table_Ratings = "Ratings";

    // Columns
    public static final String Key_User_ID = "UserID";
    public static final String Key_Username = "Username";
    public static final String Key_Password = "Password";
    public static final String Key_Role = "Role"; // Developer / Player

    static final String Key_Game_ID = "GameID";
    public static final String Key_Title = "Title";
    static final String Key_Genre_ID = "GenreID";
    public static final String Key_Price = "Price";
    static final String Key_Release_Date = "ReleaseDate";
    static final String Key_DLC = "DLC";

    static final String Key_Genre_Name = "GenreName";

    // Columns for Ratings table
    private static final String Key_Rating_ID = "RatingID";
    public static final String Key_Rating_Value = "RatingValue"; // Rating (1-5)
    public static final String Key_Comment = "Comment"; // Optional comment
    public static final String Key_Rating_User_ID = "UserID"; // Foreign key to Users table
    public static final String Key_Rating_Game_ID = "GameID"; // Foreign key to Games table

    public DatabaseHelper(Context context) {
        super(context, Database_Name, null, Database_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + Table_Users + " (" + Key_User_ID + " integer primary key autoincrement, " + Key_Username + " text, " + Key_Password + " text, " + Key_Role + " text)";
        db.execSQL(CREATE_USERS_TABLE);

        // Create Genres table
        String CREATE_GENRES_TABLE = "CREATE TABLE " + Table_Genres + " (" + "GenreID integer primary key autoincrement, " + Key_Genre_Name + " TEXT)";
        db.execSQL(CREATE_GENRES_TABLE);

        // Create Games table
        String CREATE_GAMES_TABLE = "CREATE TABLE " + Table_Games + " (" + Key_Game_ID + " integer primary key autoincrement, " + Key_Title + " TEXT, " + "DeveloperID INTEGER, " + Key_Genre_ID + " INTEGER, " + Key_Price + " REAL, " + Key_DLC + " INTEGER, " + Key_Release_Date + " TEXT, " + "FOREIGN KEY(DeveloperID) REFERENCES " + Table_Users + "(UserID), " + "FOREIGN KEY(GenreID) REFERENCES " + Table_Genres + "(GenreID))";
        db.execSQL(CREATE_GAMES_TABLE);

        // Create Ratings table
        String CREATE_RATINGS_TABLE = "CREATE TABLE " + Table_Ratings + " (" + Key_Rating_ID + " integer primary key autoincrement, " + Key_Rating_Value + " INTEGER, " + Key_Comment + " TEXT, " + Key_Rating_User_ID + " INTEGER, " + Key_Rating_Game_ID + " INTEGER, " + "FOREIGN KEY(" + Key_Rating_User_ID + ") REFERENCES " + Table_Users + "(" + Key_User_ID + "), " + "FOREIGN KEY(" + Key_Rating_Game_ID + ") REFERENCES " + Table_Games + "(" + Key_Game_ID + "))";
        db.execSQL(CREATE_RATINGS_TABLE);

        // Insert Dummy Data
        insertDummyData(db);
    }

    private void insertDummyData(SQLiteDatabase db) {
        // Insert dummy Users data
        ContentValues userValues = new ContentValues();
        userValues.put(Key_Username, "die_hard");
        userValues.put(Key_Password, "password123");
        userValues.put(Key_Role, "developer");
        db.insert(Table_Users, null, userValues);

        userValues.put(Key_Username, "player_1");
        userValues.put(Key_Password, "password123");
        userValues.put(Key_Role, "player");
        db.insert(Table_Users, null, userValues);

        // Insert dummy Genres data
        ContentValues genreValues = new ContentValues();
        genreValues.put(Key_Genre_Name, "Action");
        db.insert(Table_Genres, null, genreValues);

        genreValues.put(Key_Genre_Name, "Adventure");
        db.insert(Table_Genres, null, genreValues);

        // Insert dummy Games data
        ContentValues gameValues = new ContentValues();
        gameValues.put(Key_Title, "Game A");
        gameValues.put("DeveloperID", 1); // Dev Hero
        gameValues.put(Key_Genre_ID, 1); // Action
        gameValues.put(Key_Price, 59.99);
        gameValues.put(Key_DLC, 1); // DLC available
        gameValues.put(Key_Release_Date, "2024-01-15");
        db.insert(Table_Games, null, gameValues);

        gameValues.put(Key_Title, "Game B");
        gameValues.put("DeveloperID", 2); // Player as dev (could be error case)
        gameValues.put(Key_Genre_ID, 2); // Adventure
        gameValues.put(Key_Price, 49.99);
        gameValues.put(Key_DLC, 0); // No DLC
        gameValues.put(Key_Release_Date, "2023-11-10");
        db.insert(Table_Games, null, gameValues);

        // Insert dummy Ratings data
        ContentValues ratingValues = new ContentValues();
        ratingValues.put(Key_Rating_Value, 4); // Rating (4 out of 5)
        ratingValues.put(Key_Comment, "Great game, love the action!");
        ratingValues.put(Key_Rating_User_ID, 2); // User 2 (Player)
        ratingValues.put(Key_Rating_Game_ID, 1); // Game A
        db.insert(Table_Ratings, null, ratingValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_Users);
        db.execSQL("DROP TABLE IF EXISTS " + Table_Games);
        db.execSQL("DROP TABLE IF EXISTS " + Table_Genres);
        db.execSQL("DROP TABLE IF EXISTS " + Table_Ratings);
        onCreate(db);
    }

    public boolean addGenre(String genreName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Key_Genre_Name, genreName);  // Add the genre name

        long result = db.insert(Table_Genres, null, values);
        return result != -1;  // Return true if the genre was added successfully
    }
}
