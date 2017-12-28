package com.seriously.android.popularmovies.loader;

import android.content.Context;

import com.seriously.android.popularmovies.model.Movie;
import com.seriously.android.popularmovies.utilities.DbUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovieDbLoader extends MovieLoader {

    private static final Set<String> favoriteMovieIdsCache = new HashSet<>();

    public MovieDbLoader(Context context) {
        super(context);
    }

    @Override
    public List<Movie> loadInBackground() {
        List<Movie> movies = DbUtils.getMoviesFromDb(mContext);
        initializeFavoriteMovieIdsCache(movies);
        return movies;
    }

    public static boolean isMovieFavoriteById(String id) {
        return favoriteMovieIdsCache.contains(id);
    }

    public static void addFavoriteMovieIdToCache(String id) {
        favoriteMovieIdsCache.add(id);
    }

    private void initializeFavoriteMovieIdsCache(List<Movie> movies) {
        favoriteMovieIdsCache.clear();
        for (Movie movie : movies) {
            favoriteMovieIdsCache.add(movie.getId());
        }
    }
}
