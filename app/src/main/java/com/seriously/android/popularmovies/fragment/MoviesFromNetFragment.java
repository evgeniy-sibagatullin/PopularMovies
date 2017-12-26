package com.seriously.android.popularmovies.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.seriously.android.popularmovies.R;
import com.seriously.android.popularmovies.loader.MovieNetLoader;
import com.seriously.android.popularmovies.model.Movie;
import com.seriously.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

public abstract class MoviesFromNetFragment extends MoviesFragment {

    private static final int ANIMATION_DURATION = 2000;

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
            handleProgressImageOnRestartNetLoader();
            Bundle bundle = prepareBundleForLoader();
            getActivity().getSupportLoaderManager().restartLoader(getLoaderId(), bundle, this);
        }
    }

    private void handleProgressImageOnRestartNetLoader() {
        mNoConnection.setVisibility(View.GONE);
        mProgressImageContainer.setVisibility(View.VISIBLE);
        mProgressImage.startAnimation(prepareAnimationForProgressImage());
    }

    private RotateAnimation prepareAnimationForProgressImage() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(ANIMATION_DURATION);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        return rotateAnimation;
    }

    private Bundle prepareBundleForLoader() {
        Bundle bundle = new Bundle();
        URL queryUrl = NetworkUtils.buildUrl(getQueryType(), getString(R.string.themoviedb_api_key));
        bundle.putSerializable(QUERY_URL, queryUrl);
        return bundle;
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        super.onLoadFinished(loader, movies);
        handleProgressImageOnLoadFinished();
    }

    private void handleProgressImageOnLoadFinished() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressImageContainer.setVisibility(View.GONE);
                mProgressImage.clearAnimation();
            }
        }, ANIMATION_DURATION);
    }

    protected abstract String getQueryType();
}
