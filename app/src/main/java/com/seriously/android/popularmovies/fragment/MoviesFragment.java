package com.seriously.android.popularmovies.fragment;

import android.content.Intent;
import android.os.Bundle;
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
    protected View mProgressImageContainer;
    protected View mProgressImage;
    protected RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_selection_fragment, container, false);
        findAndPrepareViews(rootView);
        updateView();
        return rootView;
    }

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

    protected abstract void handleConnection();

    protected abstract void restartLoader();

    protected abstract int getLoaderId();

    private void findAndPrepareViews(View rootView) {
        mNoConnection = rootView.findViewById(R.id.no_connection);
        mNoConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateView();
            }
        });

        mProgressImageContainer = rootView.findViewById(R.id.progress_image_container);
        mProgressImage = rootView.findViewById(R.id.progress_image);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.movie_grid);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), GRID_COLUMNS));
        mRecyclerView.setHasFixedSize(true);
    }

    private void updateView() {
        handleConnection();
        restartLoader();
    }
}
