package com.seriously.android.popularmovies.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.seriously.android.popularmovies.model.Trailer;
import com.seriously.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

public class TrailerLoader extends AsyncTaskLoader<List<Trailer>> {

    private final Context mContext;
    private List<Trailer> cache;
    private final URL mUrl;

    public TrailerLoader(Context context, URL url) {
        super(context);
        mContext = context;
        mUrl = url;
    }

    @Override
    public List<Trailer> loadInBackground() {
        return NetworkUtils.getTrailersByUrl(mUrl, mContext);
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
    public void deliverResult(List<Trailer> trailers) {
        cache = trailers;
        super.deliverResult(trailers);
    }
}
