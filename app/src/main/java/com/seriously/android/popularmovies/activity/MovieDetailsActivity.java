package com.seriously.android.popularmovies.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.seriously.android.popularmovies.R;
import com.seriously.android.popularmovies.adapter.ReviewAdapter;
import com.seriously.android.popularmovies.data.FavoritesContract.FavoriteEntry;
import com.seriously.android.popularmovies.databinding.MovieDetailsActivityBinding;
import com.seriously.android.popularmovies.loader.MovieDbLoader;
import com.seriously.android.popularmovies.loader.ReviewLoader;
import com.seriously.android.popularmovies.model.Movie;
import com.seriously.android.popularmovies.model.Review;
import com.seriously.android.popularmovies.utilities.ImageLoader;
import com.seriously.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.seriously.android.popularmovies.data.FavoritesContract.FavoriteEntry.COLUMN_FAVORITE_ID;
import static com.seriously.android.popularmovies.fragment.MoviesFragment.EXTRA_MOVIE;
import static com.seriously.android.popularmovies.loader.MovieDbLoader.removeFavoriteMovieIdFromCache;

public class MovieDetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Review>>, ReviewAdapter.ReviewClickListener {

    private static final int ANIMATION_DURATION = 500;
    private static final int REVIEWS_LOADER_ID = 100;
    private static final String QUERY_URL = "query_url";

    private MovieDetailsActivityBinding mBinding;
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.movie_details_activity);
        handleIntent();

        initializeFavoriteViews();
        addListenerToNoConnectionView();
        mBinding.reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateView();
    }

    @Override
    public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
        return new ReviewLoader(this, (URL) args.getSerializable(QUERY_URL));
    }

    @Override
    public void onLoadFinished(Loader<List<Review>> loader, List<Review> data) {
        hideReviewsLoadingAnimation();
        mBinding.reviewsRecyclerView.setAdapter(new ReviewAdapter(this, this, data));
        handleNoReviewsDisplay(data.size());
    }

    @Override
    public void onLoaderReset(Loader<List<Review>> loader) {
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

    @Override
    public void openReview(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
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

    private void addListenerToNoConnectionView() {
        mBinding.noConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateView();
            }
        });
    }

    private void updateView() {
        handleConnection();
        restartReviewsLoader();
    }

    private void handleConnection() {
        int visibility = NetworkUtils.isConnected(this) ? View.GONE : View.VISIBLE;
        mBinding.noConnection.setVisibility(visibility);
    }

    private void restartReviewsLoader() {
        if (NetworkUtils.isConnected(this)) {
            showReviewsLoadingAnimation();
            Bundle bundle = prepareBundleForReviewsLoader();
            getSupportLoaderManager().restartLoader(REVIEWS_LOADER_ID, bundle, this);
        }
    }

    private Bundle prepareBundleForReviewsLoader() {
        Bundle bundle = new Bundle();
        URL queryUrl = NetworkUtils.buildReviewsUrl(mMovie.getId(), this);
        bundle.putSerializable(QUERY_URL, queryUrl);
        return bundle;
    }

    private void showReviewsLoadingAnimation() {
        mBinding.reviewsProgressImageContainer.setVisibility(VISIBLE);
        mBinding.reviewsProgressImage.startAnimation(prepareAnimationForProgressImage());
    }

    private void hideReviewsLoadingAnimation() {
        mBinding.reviewsProgressImageContainer.setVisibility(GONE);
        mBinding.reviewsProgressImage.clearAnimation();
    }

    private void handleNoReviewsDisplay(int reviewsCount) {
        int reviewsVisibility = reviewsCount == 0 ? VISIBLE : GONE;
        mBinding.noReviews.setVisibility(reviewsVisibility);
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
