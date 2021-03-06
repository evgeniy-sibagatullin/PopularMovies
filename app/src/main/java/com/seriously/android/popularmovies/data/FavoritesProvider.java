package com.seriously.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.seriously.android.popularmovies.data.FavoritesContract.FavoriteEntry.PROJECTION;
import static com.seriously.android.popularmovies.data.FavoritesContract.FavoriteEntry.TABLE_NAME;

public class FavoritesProvider extends ContentProvider {

    private static final String LOG_TAG = FavoritesProvider.class.getSimpleName();
    private static final int FAVORITES = 100;
    private static final int FAVORITE_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(FavoritesContract.CONTENT_AUTHORITY,
                FavoritesContract.PATH_FAVORITES, FAVORITES);
        sUriMatcher.addURI(FavoritesContract.CONTENT_AUTHORITY,
                FavoritesContract.PATH_FAVORITES + "/#", FAVORITE_ID);
    }

    private PopularMovieDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new PopularMovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        switch (sUriMatcher.match(uri)) {
            case FAVORITES:
                return queryFavorites(uri);
            default:
                throw new IllegalArgumentException("Query is not supported for " + uri);
        }
    }

    private Cursor queryFavorites(Uri uri) {
        Cursor cursor = mDbHelper.getReadableDatabase().query(TABLE_NAME, PROJECTION,
                null, null, null, null, null, null);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        switch (sUriMatcher.match(uri)) {
            case FAVORITES:
                return insertFavorite(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertFavorite(Uri uri, ContentValues contentValues) {
        long id = mDbHelper.getWritableDatabase().insert(TABLE_NAME, null, contentValues);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;

        switch (sUriMatcher.match(uri)) {
            case FAVORITES:
                rowsDeleted = database.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
