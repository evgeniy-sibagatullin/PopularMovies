package com.seriously.android.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.seriously.android.popularmovies.R;
import com.seriously.android.popularmovies.model.Movie;
import com.seriously.android.popularmovies.model.Review;
import com.seriously.android.popularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NetworkUtils {

    private NetworkUtils() {
    }

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    private static final String SCHEME = "https";
    private static final String AUTHORITY = "api.themoviedb.org";
    private static final String API_BASE_URL_PART_1 = "3";
    private static final String API_BASE_URL_PART_2 = "movie";
    private static final String API_KEY = "api_key";
    private static final String API_REVIEWS_ENDPOINT = "reviews";
    private static final String API_TRAILERS_ENDPOINT = "trailers";

    private static final String JSON_RESULTS = "results";
    private static final String JSON_YOUTUBE = "youtube";

    public static boolean isConnected(Context context) {
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo(context);
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Nullable
    private static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager == null ? null : connectivityManager.getActiveNetworkInfo();
    }

    public static URL buildMoviesUrl(String queryType, Context context) {
        return buildUrl(queryType, null, context);
    }

    public static URL buildReviewsUrl(String movieId, Context context) {
        return buildUrl(API_REVIEWS_ENDPOINT, movieId, context);
    }

    public static URL buildTrailersUrl(String movieId, Context context) {
        return buildUrl(API_TRAILERS_ENDPOINT, movieId, context);
    }

    private static URL buildUrl(String endpoint, String movieId, Context context) {
        Uri.Builder builder = new Uri.Builder()
                .scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(API_BASE_URL_PART_1)
                .appendPath(API_BASE_URL_PART_2);

        if (movieId != null) {
            builder.appendPath(movieId);
        }

        Uri builtUri = builder
                .appendPath(endpoint)
                .appendQueryParameter(API_KEY, context.getString(R.string.themoviedb_api_key))
                .build();
        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static List<Movie> getMoviesByUrl(URL url, Context context) {
        String jsonString = getJsonStringFromUrl(url, context);
        return extractMoviesFromJson(jsonString, context);
    }

    public static List<Review> getReviewsByUrl(URL url, Context context) {
        String jsonString = getJsonStringFromUrl(url, context);
        return extractReviewsFromJson(jsonString, context);
    }

    public static List<Trailer> getTrailersByUrl(URL url, Context context) {
        String jsonString = getJsonStringFromUrl(url, context);
        return extractTrailersFromJson(jsonString, context);
    }

    @Nullable
    private static String getJsonStringFromUrl(URL url, Context context) {
        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            if (scanner.hasNext()) {
                return scanner.next();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, context.getString(R.string.problem_http), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    private static List<Movie> extractMoviesFromJson(String moviesJson, Context context) {
        List<Movie> movies = new ArrayList<>();

        if (moviesJson != null) {
            try {
                JSONArray moviesArray = new JSONObject(moviesJson).getJSONArray(JSON_RESULTS);

                for (int index = 0; index < moviesArray.length(); index++) {
                    movies.add(Movie.getInstance(moviesArray.getJSONObject(index)));
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, context.getString(R.string.problem_json), e);
            }
        }

        return movies;
    }

    private static List<Review> extractReviewsFromJson(String reviewsJson, Context context) {
        List<Review> reviews = new ArrayList<>();

        if (reviewsJson != null) {
            try {
                JSONArray reviewsArray = new JSONObject(reviewsJson).getJSONArray(JSON_RESULTS);

                for (int index = 0; index < reviewsArray.length(); index++) {
                    reviews.add(Review.getInstance(reviewsArray.getJSONObject(index)));
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, context.getString(R.string.problem_json), e);
            }
        }

        return reviews;
    }

    private static List<Trailer> extractTrailersFromJson(String trailersJson, Context context) {
        List<Trailer> trailers = new ArrayList<>();

        if (trailersJson != null) {
            try {
                JSONArray reviewsArray = new JSONObject(trailersJson).getJSONArray(JSON_YOUTUBE);

                for (int index = 0; index < reviewsArray.length(); index++) {
                    trailers.add(Trailer.getInstance(reviewsArray.getJSONObject(index)));
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, context.getString(R.string.problem_json), e);
            }
        }

        return trailers;
    }
}