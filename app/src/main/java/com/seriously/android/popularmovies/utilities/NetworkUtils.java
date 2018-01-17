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

    private static final String API_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY = "api_key";
    private static final String API_REVIEWS_ENDPOINT = "%s/reviews";

    private static final String JSON_RESULTS = "results";

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
        return buildUrl(API_BASE_URL + queryType, context);
    }

    public static URL buildReviewsUrl(String movieId, Context context) {
        return buildUrl(API_BASE_URL + String.format(API_REVIEWS_ENDPOINT, movieId), context);
    }

    public static List<Movie> getMoviesByUrl(URL url, Context context) {
        String jsonString = getJsonStringFromUrl(url, context);
        return extractMoviesFromJson(jsonString, context);
    }

    public static List<Review> getReviewsByUrl(URL url, Context context) {
        String jsonString = getJsonStringFromUrl(url, context);
        return extractReviewsFromJson(jsonString, context);
    }

    private static URL buildUrl(String uriString, Context context) {
        String apiKey = context.getString(R.string.themoviedb_api_key);
        Uri builtUri = Uri.parse(uriString).buildUpon().appendQueryParameter(API_KEY, apiKey).build();
        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
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
}