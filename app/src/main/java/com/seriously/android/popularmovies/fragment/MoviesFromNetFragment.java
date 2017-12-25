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

public abstract class MoviesFromNetFragment extends MoviesFragment {

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new MovieNetLoader(getContext(), (URL) args.getSerializable(QUERY_URL));
    }

    @Override
    protected void handleConnection() {
        if (NetworkUtils.isConnected(getActivity())) {
            mNoConnection.setVisibility(View.GONE);
        } else {
            mNoConnection.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void restartLoader() {
        if (NetworkUtils.isConnected(getActivity())) {
            Bundle bundle = new Bundle();
            URL queryUrl = NetworkUtils.buildUrl(getQueryType(), getString(R.string.themoviedb_api_key));
            bundle.putSerializable(QUERY_URL, queryUrl);
            getActivity().getSupportLoaderManager().restartLoader(getLoaderId(), bundle, this);
        }
    }

    protected abstract String getQueryType();
}
