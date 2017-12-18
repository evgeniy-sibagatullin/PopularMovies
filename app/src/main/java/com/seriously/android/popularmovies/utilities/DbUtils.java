package com.seriously.android.popularmovies.utilities;

import android.content.Context;
import android.database.Cursor;

import com.seriously.android.popularmovies.data.FavoritesContract.FavoriteEntry;
import com.seriously.android.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;


public class DbUtils {

    private DbUtils() {
    }

    public static List<Movie> getMoviesFromDb(Context context) {
        Cursor cursor = context.getContentResolver().query(FavoriteEntry.CONTENT_URI,
                null, null, null, null);
        return convertCursorToMovieList(cursor);
    }

    private static List<Movie> convertCursorToMovieList(Cursor cursor) {
        List<Movie> movies = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            movies.add(FavoriteEntry.getMovieFromCursor(cursor));
        }

        return movies;
    }
}
