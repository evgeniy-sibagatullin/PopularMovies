package com.seriously.android.popularmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.seriously.android.popularmovies.R;
import com.seriously.android.popularmovies.model.Movie;
import com.seriously.android.popularmovies.utilities.ImageLoader;

import static com.seriously.android.popularmovies.activity.MovieSelectionActivity.EXTRA_MOVIE;

public class MovieDetailsActivity extends AppCompatActivity {

    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mVoteAverage;
    private ImageView mPoster;
    private TextView mOverview;
    private ImageView mFavoriteOff;
    private ImageView mFavoriteOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

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
            fillViewsByMovieData((Movie) intent.getSerializableExtra(EXTRA_MOVIE));
        } else {
            this.finish();
        }
    }

    private void fillViewsByMovieData(Movie movie) {
        mTitle.setText(movie.getTitle());
        mReleaseDate.setText(movie.getReleaseDate());
        mVoteAverage.setText(movie.getVoteAverage());
        ImageLoader.loadFullPosterImage(this, mPoster, movie.getPosterPath());
        mOverview.setText(movie.getOverview());
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
}
