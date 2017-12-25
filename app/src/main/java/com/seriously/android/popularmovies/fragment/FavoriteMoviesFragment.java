package com.seriously.android.popularmovies.fragment;

public class FavoriteMoviesFragment extends MoviesFromDbFragment {

    private static final int FAVORITE_MOVIES_LOADER_ID = 0;

    @Override
    protected int getLoaderId() {
        return FAVORITE_MOVIES_LOADER_ID;
    }
}
