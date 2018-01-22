package com.seriously.android.popularmovies.utilities;

import android.content.Context;
import android.widget.ImageView;

import com.seriously.android.popularmovies.R;
import com.squareup.picasso.Picasso;

public class ImageLoader {

    private static final String TMDB_IMAGE_PATH_PREFIX = "http://image.tmdb.org/t/p/";
    private static final String W_185_SIZE_PATH = "w185/";

    public static void loadGridPosterImage(Context context, ImageView view, String path) {
        Picasso.with(context).load(TMDB_IMAGE_PATH_PREFIX + W_185_SIZE_PATH + path)
                .placeholder(R.drawable.loading)
                .error(R.drawable.missing)
                .into(view);
    }
}
