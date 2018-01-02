package com.seriously.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoritesContract {

    static final String CONTENT_AUTHORITY = "com.seriously.android.popularmovies";
    static final String PATH_FAVORITES = "favorites";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private FavoritesContract() {
    }

    public static final class FavoriteEntry implements BaseColumns {
        static final String TABLE_NAME = "favorites";
        static final String _ID = BaseColumns._ID;
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FAVORITES);
        public static final String COLUMN_FAVORITE_ID = "id";
        public static final String COLUMN_FAVORITE_TITLE = "title";
        public static final String COLUMN_FAVORITE_RELEASE_DATE = "release_date";
        public static final String COLUMN_FAVORITE_POSTER_PATH = "poster_path";
        public static final String COLUMN_FAVORITE_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_FAVORITE_OVERVIEW = "overview";
        final static String[] PROJECTION = new String[]
                {_ID, COLUMN_FAVORITE_ID, COLUMN_FAVORITE_TITLE,
                        COLUMN_FAVORITE_RELEASE_DATE, COLUMN_FAVORITE_POSTER_PATH,
                        COLUMN_FAVORITE_VOTE_AVERAGE, COLUMN_FAVORITE_OVERVIEW};
    }
}
