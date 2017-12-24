package com.seriously.android.popularmovies.fragment;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;

import com.seriously.android.popularmovies.R;
import com.seriously.android.popularmovies.loader.MovieNetLoader;
import com.seriously.android.popularmovies.model.Movie;
import com.seriously.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

public class TopRatedMoviesFragment extends MoviesFragment {

    private static final int TOP_RATED_MOVIES_LOADER_ID = 2;
    private static final String QUERY_TYPE_TOP_RATED = "top_rated";

    protected void handleConnection() {
        if (NetworkUtils.isConnected(getActivity())) {
            mNoConnection.setVisibility(View.GONE);
        } else {
            mNoConnection.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new MovieNetLoader(getContext(), (URL) args.getSerializable(QUERY_URL));
    }

    protected void restartLoader() {
        if (NetworkUtils.isConnected(getActivity())) {
            Bundle bundle = new Bundle();
            URL queryUrl = NetworkUtils.buildUrl(QUERY_TYPE_TOP_RATED, getString(R.string.themoviedb_api_key));
            bundle.putSerializable(QUERY_URL, queryUrl);
            getActivity().getSupportLoaderManager().restartLoader(TOP_RATED_MOVIES_LOADER_ID, bundle, this);
        }
    }
}
