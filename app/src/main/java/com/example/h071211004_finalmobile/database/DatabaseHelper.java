package com.example.h071211004_finalmobile.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.h071211004_finalmobile.data.model.Movie;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.DatabaseEntry.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseContract.DatabaseEntry.SQL_DROP_TABLE);
        onCreate(db);
    }

    public long insertMovie(Movie movie) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.DatabaseEntry.COLUMN_TITLE, movie.getTitle());
        values.put(DatabaseContract.DatabaseEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(DatabaseContract.DatabaseEntry.COLUMN_OVERVIEW, movie.getOverview());
        values.put(DatabaseContract.DatabaseEntry.COLUMN_POSTER_URL, movie.getPosterPath());
        values.put(DatabaseContract.DatabaseEntry.COLUMN_BACKDROP_URL, movie.getBackdropUrl());
        values.put(DatabaseContract.DatabaseEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(DatabaseContract.DatabaseEntry.COLUMN_GENRE_IDS, movie.getId());

        return db.insert(DatabaseContract.DatabaseEntry.TABLE_NAME, null, values);
    }

    public Cursor getAllMovies() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                DatabaseContract.DatabaseEntry._ID,
                DatabaseContract.DatabaseEntry.COLUMN_TITLE,
                DatabaseContract.DatabaseEntry.COLUMN_RELEASE_DATE,
                DatabaseContract.DatabaseEntry.COLUMN_OVERVIEW,
                DatabaseContract.DatabaseEntry.COLUMN_POSTER_URL,
                DatabaseContract.DatabaseEntry.COLUMN_BACKDROP_URL,
                DatabaseContract.DatabaseEntry.COLUMN_VOTE_AVERAGE,
                DatabaseContract.DatabaseEntry.COLUMN_GENRE_IDS
        };
        return db.query(
                DatabaseContract.DatabaseEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
    }

    public int updateMovie(Movie movie) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.DatabaseEntry.COLUMN_TITLE, movie.getTitle());
        values.put(DatabaseContract.DatabaseEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(DatabaseContract.DatabaseEntry.COLUMN_OVERVIEW, movie.getOverview());
        values.put(DatabaseContract.DatabaseEntry.COLUMN_POSTER_URL, movie.getPosterPath());
        values.put(DatabaseContract.DatabaseEntry.COLUMN_BACKDROP_URL, movie.getBackdropUrl());
        values.put(DatabaseContract.DatabaseEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(DatabaseContract.DatabaseEntry.COLUMN_GENRE_IDS, movie.getId());

        String selection = DatabaseContract.DatabaseEntry._ID + "=?";
        String[] selectionArgs = {String.valueOf(movie.getId())};

        return db.update(DatabaseContract.DatabaseEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public int deleteMovie(String nama) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = DatabaseContract.DatabaseEntry.COLUMN_TITLE + "=?";
        String[] selectionArgs = {String.valueOf(nama)};

        return db.delete(DatabaseContract.DatabaseEntry.TABLE_NAME, selection, selectionArgs);
    }

    public boolean isMovieInFavorites(String movieTitle) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + DatabaseContract.DatabaseEntry.TABLE_NAME +
                    " WHERE " + DatabaseContract.DatabaseEntry.COLUMN_TITLE + " = ?";
            String[] selectionArgs = {String.valueOf(movieTitle)};
            cursor = db.rawQuery(query, selectionArgs);
            if (cursor != null && cursor.getCount() > 0) {
                return true;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }


}
