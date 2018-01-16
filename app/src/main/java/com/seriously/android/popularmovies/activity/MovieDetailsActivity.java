package com.seriously.android.popularmovies.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.seriously.android.popularmovies.R;
import com.seriously.android.popularmovies.data.FavoritesContract.FavoriteEntry;
import com.seriously.android.popularmovies.databinding.MovieDetailsActivityBinding;
import com.seriously.android.popularmovies.loader.MovieDbLoader;
import com.seriously.android.popularmovies.model.Movie;
import com.seriously.android.popularmovies.utilities.ImageLoader;
import com.seriously.android.popularmovies.utilities.NetworkUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.seriously.android.popularmovies.data.FavoritesContract.FavoriteEntry.COLUMN_FAVORITE_ID;
import static com.seriously.android.popularmovies.fragment.MoviesFragment.EXTRA_MOVIE;
import static com.seriously.android.popularmovies.loader.MovieDbLoader.removeFavoriteMovieIdFromCache;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final int ANIMATION_DURATION = 500;

    private MovieDetailsActivityBinding mBinding;
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.movie_details_activity);

        handleIntent();
        initializeFavoriteViews();
        initializeNoConnectionView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String movieId = mMovie.getId();

        if (mBinding.favoriteOn.getVisibility() == VISIBLE && !mMovie.isFavorite()) {
            handleSetMovieFavorite(movieId);
        } else if (mBinding.favoriteOff.getVisibility() == VISIBLE && mMovie.isFavorite()) {
            handleSetMovieNotFavorite(movieId);
        }
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
        mBinding.title.setText(mMovie.getTitle());
        mBinding.releaseDate.setText(mMovie.getReleaseDate());
        mBinding.voteAverage.setText(mMovie.getVoteAverage());
        ImageLoader.loadGridPosterImage(this, mBinding.poster, mMovie.getPosterPath());
        mBinding.overview.setText(mMovie.getOverview());
    }

    private void initializeFavoriteViews() {
        addListenersToFavoriteViews();
        setViewVisibility(mMovie.isFavorite() ? mBinding.favoriteOff : mBinding.favoriteOn, GONE);
    }

    private void addListenersToFavoriteViews() {
        mBinding.favoriteOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewVisibility(mBinding.favoriteOff, GONE);
                setViewVisibility(mBinding.favoriteOn, VISIBLE);
            }
        });

        mBinding.favoriteOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewVisibility(mBinding.favoriteOff, VISIBLE);
                setViewVisibility(mBinding.favoriteOn, GONE);
            }
        });
    }

    private void setViewVisibility(View view, int visibility) {
        view.setVisibility(visibility);
    }

    private void initializeNoConnectionView() {
        handleConnection();

        mBinding.noConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleConnection();
            }
        });
    }

    private void handleConnection() {
        if (NetworkUtils.isConnected(this)) {
            mBinding.noConnection.setVisibility(View.GONE);
            showReviewsLoadingAnimation();
        } else {
            mBinding.noConnection.setVisibility(View.VISIBLE);
            hideReviewsLoadingAnimation();
        }
    }

    private void showReviewsLoadingAnimation() {
        mBinding.reviewsProgressImageContainer.setVisibility(VISIBLE);
        mBinding.reviewsProgressImage.startAnimation(prepareAnimationForProgressImage());
    }

    private void hideReviewsLoadingAnimation() {
        mBinding.reviewsProgressImageContainer.setVisibility(GONE);
        mBinding.reviewsProgressImage.clearAnimation();
    }

    private RotateAnimation prepareAnimationForProgressImage() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(ANIMATION_DURATION);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        return rotateAnimation;
    }

    private void handleSetMovieFavorite(String movieId) {
        ContentValues valuesToInsert = prepareFavoriteValues();
        Uri uri = getContentResolver().insert(FavoriteEntry.CONTENT_URI, valuesToInsert);

        if (uri != null) {
            MovieDbLoader.addFavoriteMovieIdToCache(movieId);
        }
    }

    private void handleSetMovieNotFavorite(String movieId) {
        String whereClause = COLUMN_FAVORITE_ID + "=?";
        String[] whereArgs = new String[]{movieId};

        if (getContentResolver().delete(FavoriteEntry.CONTENT_URI, whereClause, whereArgs) > 0) {
            removeFavoriteMovieIdFromCache(movieId);
        }
    }

    private ContentValues prepareFavoriteValues() {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FAVORITE_ID, mMovie.getId());
        values.put(FavoriteEntry.COLUMN_FAVORITE_TITLE, mMovie.getTitle());
        values.put(FavoriteEntry.COLUMN_FAVORITE_RELEASE_DATE, mMovie.getReleaseDate());
        values.put(FavoriteEntry.COLUMN_FAVORITE_POSTER_PATH, mMovie.getPosterPath());
        values.put(FavoriteEntry.COLUMN_FAVORITE_VOTE_AVERAGE, mMovie.getVoteAverage());
        values.put(FavoriteEntry.COLUMN_FAVORITE_OVERVIEW, mMovie.getOverview());
        return values;
    }
}
