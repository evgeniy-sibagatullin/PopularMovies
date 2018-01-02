package com.seriously.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.seriously.android.popularmovies.data.FavoritesContract.FavoriteEntry;

class PopularMovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    PopularMovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE " + FavoriteEntry.TABLE_NAME + " ("
                + FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FavoriteEntry.COLUMN_FAVORITE_ID + " TEXT NOT NULL, "
                + FavoriteEntry.COLUMN_FAVORITE_TITLE + " TEXT NOT NULL, "
                + FavoriteEntry.COLUMN_FAVORITE_RELEASE_DATE + " TEXT NOT NULL, "
                + FavoriteEntry.COLUMN_FAVORITE_POSTER_PATH + " TEXT NOT NULL, "
                + FavoriteEntry.COLUMN_FAVORITE_VOTE_AVERAGE + " TEXT NOT NULL, "
                + FavoriteEntry.COLUMN_FAVORITE_OVERVIEW + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
