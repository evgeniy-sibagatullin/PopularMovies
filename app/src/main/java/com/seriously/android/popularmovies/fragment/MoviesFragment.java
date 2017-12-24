package com.seriously.android.popularmovies.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seriously.android.popularmovies.R;
import com.seriously.android.popularmovies.activity.MovieDetailsActivity;
import com.seriously.android.popularmovies.adapter.MovieAdapter;
import com.seriously.android.popularmovies.model.Movie;

import java.util.List;

public abstract class MoviesFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<Movie>>, MovieAdapter.MovieClickListener {

    public static final String EXTRA_MOVIE = "intent.extra.MOVIE";
    protected static final String QUERY_URL = "query_url";
    private static final int GRID_COLUMNS = 2;

    protected View mNoConnection;
    protected RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.movie_selection_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNoConnection = getActivity().findViewById(R.id.no_connection);

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.movie_grid);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), GRID_COLUMNS));
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        handleConnection();
        restartLoader();
    }

    protected abstract void handleConnection();

    protected abstract void restartLoader();

    @Override
    public void onMovieClick(Movie movie) {
        Intent startMovieDetailsActivityIntent = new Intent(getActivity(), MovieDetailsActivity.class);
        startMovieDetailsActivityIntent.putExtra(EXTRA_MOVIE, movie);
        startActivity(startMovieDetailsActivityIntent);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        mRecyclerView.setAdapter(new MovieAdapter(getActivity(), this, movies));
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
    }
}
