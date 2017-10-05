package com.seriously.android.popularmovies.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.seriously.android.popularmovies.model.Movie;
import com.seriously.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private final Context mContext;
    private final URL mUrl;
    private final Map<URL, List<Movie>> cache = new HashMap<>();

    public MovieLoader(Context context, URL url) {
        super(context);
        mContext = context;
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        if (cache.containsKey(mUrl)) {
            deliverResult(cache.get(mUrl));
        } else {
            forceLoad();
        }
    }

    @Override
    public List<Movie> loadInBackground() {
        return NetworkUtils.getMoviesByUrl(mUrl, mContext);
    }

    @Override
    public void deliverResult(List<Movie> movies) {
        cache.put(mUrl, movies);
        super.deliverResult(movies);
    }
}
