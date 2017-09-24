package com.seriously.android.popularmovies.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.seriously.android.popularmovies.model.Movie;
import com.seriously.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private final Context mContext;
    private final URL mUrl;

    public MovieLoader(Context context, URL url) {
        super(context);
        mContext = context;
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        return NetworkUtils.getMoviesByUrl(mUrl, mContext);
    }
}
