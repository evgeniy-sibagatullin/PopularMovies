package com.seriously.android.popularmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.seriously.android.popularmovies.R;
import com.seriously.android.popularmovies.adapter.MovieAdapter;
import com.seriously.android.popularmovies.loader.MovieLoader;
import com.seriously.android.popularmovies.model.Movie;
import com.seriously.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MovieSelectionActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Movie>>, MovieAdapter.MovieClickListener {

    public static final String EXTRA_MOVIE = "intent.extra.MOVIE";

    private static final int LOADER_ID = 0;
    private static final int GRID_COLUMNS = 2;
    private static final String QUERY_URL = "query_url";
    private static final String QUERY_TYPE_POPULAR = "popular";
    private static final String QUERY_TYPE_TOP_RATED = "top_rated";
    public static final String STATE_QUERY_TYPE = "state_query_type";

    private String mQueryType;
    private View mNoConnection;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_selection);

        mNoConnection = findViewById(R.id.no_connection);

        mRecyclerView = (RecyclerView) findViewById(R.id.movie_grid);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, GRID_COLUMNS));
        mRecyclerView.setHasFixedSize(true);

        mQueryType = savedInstanceState != null ?
                savedInstanceState.getString(STATE_QUERY_TYPE) :
                QUERY_TYPE_POPULAR;

        handleQueryTypeSelection(mQueryType);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_QUERY_TYPE, mQueryType);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.query_type, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_query_type_popular:
                handleQueryTypeSelection(QUERY_TYPE_POPULAR);
                return true;
            case R.id.item_query_type_top_rated:
                handleQueryTypeSelection(QUERY_TYPE_TOP_RATED);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleQueryTypeSelection(String queryType) {
        URL queryUrl = NetworkUtils.buildUrl(queryType, getString(R.string.themoviedb_api_key));
        boolean isConnected = NetworkUtils.isConnected(this);
        setupNoConnectionView(isConnected);

        if (isConnected) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(QUERY_URL, queryUrl);

            if (queryType.equals(mQueryType)) {
                getSupportLoaderManager().initLoader(LOADER_ID, bundle, this);
            } else {
                getSupportLoaderManager().restartLoader(LOADER_ID, bundle, this);
                mQueryType = queryType;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupNoConnectionView(NetworkUtils.isConnected(this));
    }

    private void setupNoConnectionView(boolean isConnected) {
        mNoConnection.setVisibility(isConnected ? GONE : VISIBLE);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new MovieLoader(this, (URL) args.getSerializable(QUERY_URL));
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        mRecyclerView.setAdapter(new MovieAdapter(this, movies));
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
    }

    @Override
    public void onMovieClick(Movie movie) {
        Intent startMovieDetailsActivityIntent = new Intent(this, MovieDetailsActivity.class);
        startMovieDetailsActivityIntent.putExtra(EXTRA_MOVIE, movie);
        startActivity(startMovieDetailsActivityIntent);
    }
}
