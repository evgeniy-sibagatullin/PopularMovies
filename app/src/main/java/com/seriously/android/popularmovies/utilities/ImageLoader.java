package com.seriously.android.popularmovies.utilities;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageLoader {

    private static final String TMDB_IMAGE_PATH_PREFIX = "http://image.tmdb.org/t/p/";
    private static final String W_185_SIZE_PATH = "w185/";
    private static final String ORIGINAL_SIZE_PATH = "original/";

    public static void loadGridPosterImage(Context context, ImageView view, String path) {
        Picasso.with(context).load(TMDB_IMAGE_PATH_PREFIX + W_185_SIZE_PATH + path).into(view);
    }

    public static void loadFullPosterImage(Context context, ImageView view, String path) {
        Picasso.with(context).load(TMDB_IMAGE_PATH_PREFIX + ORIGINAL_SIZE_PATH + path).into(view);
    }
}
