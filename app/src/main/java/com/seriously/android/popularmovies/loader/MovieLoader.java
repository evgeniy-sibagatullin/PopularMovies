package com.seriously.android.popularmovies.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.seriously.android.popularmovies.model.Movie;

import java.util.List;

abstract class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    final Context mContext;
    private List<Movie> cache;

    MovieLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        if (cache != null) {
            deliverResult(cache);
        } else {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(List<Movie> movies) {
        cache = movies;
        super.deliverResult(movies);
    }
}
