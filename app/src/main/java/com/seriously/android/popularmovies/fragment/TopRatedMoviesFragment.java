package com.seriously.android.popularmovies.fragment;

public class TopRatedMoviesFragment extends MoviesFromNetFragment {

    private static final int TOP_RATED_MOVIES_LOADER_ID = 2;
    private static final String QUERY_TYPE_TOP_RATED = "top_rated";

    @Override
    protected int getLoaderId() {
        return TOP_RATED_MOVIES_LOADER_ID;
    }

    @Override
    protected String getQueryType() {
        return QUERY_TYPE_TOP_RATED;
    }
}
