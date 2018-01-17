package com.seriously.android.popularmovies.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.seriously.android.popularmovies.model.Review;
import com.seriously.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

public class ReviewLoader extends AsyncTaskLoader<List<Review>> {

    private final Context mContext;
    private List<Review> cache;
    private final URL mUrl;

    public ReviewLoader(Context context, URL url) {
        super(context);
        mContext = context;
        mUrl = url;
    }

    @Override
    public List<Review> loadInBackground() {
        return NetworkUtils.getReviewsByUrl(mUrl, mContext);
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
    public void deliverResult(List<Review> reviews) {
        cache = reviews;
        super.deliverResult(reviews);
    }
}
