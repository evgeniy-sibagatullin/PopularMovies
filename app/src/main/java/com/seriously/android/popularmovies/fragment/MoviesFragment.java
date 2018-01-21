package com.seriously.android.popularmovies.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
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
import com.seriously.android.popularmovies.databinding.MovieSelectionFragmentBinding;
import com.seriously.android.popularmovies.model.Movie;

import java.util.List;

public abstract class MoviesFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<Movie>>, MovieAdapter.MovieClickListener {

    private MovieSelectionFragmentBinding mBinding;

    public static final String EXTRA_MOVIE = "intent.extra.MOVIE";
    protected static final String QUERY_URL = "query_url";
    private static final int GRID_COLUMNS_PORTRAIT = 2;
    private static final int GRID_COLUMNS_LANDSCAPE = 3;

    protected View mNoConnection;
    protected View mProgressImageContainer;
    protected View mProgressImage;
    protected RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.movie_selection_fragment, container, false);
        View rootView = mBinding.getRoot();
        prepareViews();
        updateView();
        return rootView;
    }

    @Override
    public void openDetails(Movie movie) {
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

    private void prepareViews() {
        mNoConnection = mBinding.noConnection;
        mNoConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateView();
            }
        });

        mProgressImageContainer = mBinding.progressImageContainer;
        mProgressImage = mBinding.progressImage;

        mRecyclerView = mBinding.movieGrid;
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), getGridSpanCount()));
        mRecyclerView.setHasFixedSize(true);
    }

    private int getGridSpanCount() {
        int currentOrientation = getResources().getConfiguration().orientation;
        return currentOrientation == Configuration.ORIENTATION_LANDSCAPE ?
                GRID_COLUMNS_LANDSCAPE : GRID_COLUMNS_PORTRAIT;
    }

    protected void updateView() {
        handleConnection();
        restartLoader();
    }
}
