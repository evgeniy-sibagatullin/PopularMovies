package com.seriously.android.popularmovies.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.seriously.android.popularmovies.R;
import com.seriously.android.popularmovies.fragment.FavoriteMoviesFragment;
import com.seriously.android.popularmovies.fragment.PopularMoviesFragment;
import com.seriously.android.popularmovies.fragment.TopRatedMoviesFragment;

public class MovieTypePagerAdapter extends FragmentPagerAdapter {

    private static final int MOVIE_TYPES_COUNT = 3;
    private Context mContext;

    public MovieTypePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new PopularMoviesFragment();
            case 2:
                return new TopRatedMoviesFragment();
            default:
                return new FavoriteMoviesFragment();
        }
    }

    @Override
    public int getCount() {
        return MOVIE_TYPES_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 1:
                return mContext.getString(R.string.menu_query_type_popular);
            case 2:
                return mContext.getString(R.string.menu_query_type_top_rated);
            default:
                return mContext.getString(R.string.menu_query_type_favorites);
        }
    }
}
