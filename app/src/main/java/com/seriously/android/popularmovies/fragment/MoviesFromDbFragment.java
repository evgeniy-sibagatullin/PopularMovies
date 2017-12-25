package com.seriously.android.popularmovies.fragment;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;

import com.seriously.android.popularmovies.loader.MovieDbLoader;
import com.seriously.android.popularmovies.model.Movie;

import java.util.List;

public abstract class MoviesFromDbFragment extends MoviesFragment {

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new MovieDbLoader(getContext());
    }

    @Override
    protected void handleConnection() {
        mNoConnection.setVisibility(View.GONE);
    }

    @Override
    protected void restartLoader() {
        getActivity().getSupportLoaderManager().restartLoader(getLoaderId(), null, this);
    }
}
