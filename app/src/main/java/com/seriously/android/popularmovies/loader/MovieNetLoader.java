package com.seriously.android.popularmovies.loader;

import android.content.Context;

import com.seriously.android.popularmovies.model.Movie;
import com.seriously.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

public class MovieNetLoader extends MovieLoader {

    private final URL mUrl;

    public MovieNetLoader(Context context, URL url) {
        super(context);
        mUrl = url;
    }

    @Override
    public List<Movie> loadInBackground() {
        return NetworkUtils.getMoviesByUrl(mUrl, mContext);
    }
}
