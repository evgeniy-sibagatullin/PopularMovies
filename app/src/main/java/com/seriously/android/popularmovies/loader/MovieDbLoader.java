package com.seriously.android.popularmovies.loader;

import android.content.Context;

import com.seriously.android.popularmovies.model.Movie;
import com.seriously.android.popularmovies.utilities.DbUtils;

import java.util.List;

public class MovieDbLoader extends MovieLoader {

    public MovieDbLoader(Context context) {
        super(context);
    }

    @Override
    public List<Movie> loadInBackground() {
        return DbUtils.getMoviesFromDb(mContext);
    }
}
