package com.seriously.android.popularmovies.fragment;

public class PopularMoviesFragment extends MoviesFromNetFragment {

    private static final int POPULAR_MOVIES_LOADER_ID = 1;
    private static final String QUERY_TYPE_POPULAR = "popular";

    @Override
    protected int getLoaderId() {
        return POPULAR_MOVIES_LOADER_ID;
    }

    @Override
    protected String getQueryType() {
        return QUERY_TYPE_POPULAR;
    }
}
