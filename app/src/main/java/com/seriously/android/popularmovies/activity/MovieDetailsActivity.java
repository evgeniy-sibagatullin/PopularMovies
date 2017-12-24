package com.seriously.android.popularmovies.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seriously.android.popularmovies.R;
import com.seriously.android.popularmovies.data.FavoritesContract.FavoriteEntry;
import com.seriously.android.popularmovies.model.Movie;
import com.seriously.android.popularmovies.utilities.ImageLoader;

import static com.seriously.android.popularmovies.fragment.MoviesFragment.EXTRA_MOVIE;

public class MovieDetailsActivity extends AppCompatActivity {

    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mVoteAverage;
    private ImageView mPoster;
    private TextView mOverview;
    private ImageView mFavoriteOff;
    private ImageView mFavoriteOn;

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details_activity);

        defineViews();
        handleIntent();
        addListenersToFavoriteViews();
    }

    private void defineViews() {
        mTitle = (TextView) findViewById(R.id.title);
        mReleaseDate = (TextView) findViewById(R.id.release_date);
        mVoteAverage = (TextView) findViewById(R.id.vote_average);
        mPoster = (ImageView) findViewById(R.id.poster);
        mOverview = (TextView) findViewById(R.id.overview);
        mFavoriteOff = (ImageView) findViewById(R.id.favorite_off);
        mFavoriteOn = (ImageView) findViewById(R.id.favorite_on);
    }

    private void handleIntent() {
        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_MOVIE)) {
            mMovie = (Movie) intent.getSerializableExtra(EXTRA_MOVIE);
            fillViewsByMovieData();
        } else {
            this.finish();
        }
    }

    private void fillViewsByMovieData() {
        mTitle.setText(mMovie.getTitle());
        mReleaseDate.setText(mMovie.getReleaseDate());
        mVoteAverage.setText(mMovie.getVoteAverage());
        ImageLoader.loadGridPosterImage(this, mPoster, mMovie.getPosterPath());
        mOverview.setText(mMovie.getOverview());
    }

    private void addListenersToFavoriteViews() {
        mFavoriteOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFavoriteOff.setVisibility(View.GONE);
                mFavoriteOn.setVisibility(View.VISIBLE);
            }
        });

        mFavoriteOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFavoriteOn.setVisibility(View.GONE);
                mFavoriteOff.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (mFavoriteOn.getVisibility() == View.VISIBLE) {
            ContentValues valuesToInsert = prepareFavoriteValues();
            Uri uri = getContentResolver().insert(FavoriteEntry.CONTENT_URI, valuesToInsert);

            if (uri != null) {
                Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private ContentValues prepareFavoriteValues() {
        ContentValues values = new ContentValues();
        values.put(FavoriteEntry.COLUMN_FAVORITE_ID, mMovie.getId());
        values.put(FavoriteEntry.COLUMN_FAVORITE_TITLE, mMovie.getTitle());
        values.put(FavoriteEntry.COLUMN_FAVORITE_POSTER_PATH, mMovie.getPosterPath());
        return values;
    }
}
