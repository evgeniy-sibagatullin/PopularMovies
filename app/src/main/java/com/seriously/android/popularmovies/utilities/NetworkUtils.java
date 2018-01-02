package com.seriously.android.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.seriously.android.popularmovies.R;
import com.seriously.android.popularmovies.model.Movie;

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

    public static URL buildUrl(String queryType, String apiKey) {
        Uri builtUri = Uri.parse(API_BASE_URL + queryType).buildUpon()
                .appendQueryParameter(API_KEY, apiKey).build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static List<Movie> getMoviesByUrl(URL url, Context context) {
        String jsonString = null;

        try {
            jsonString = getJsonStringFromUrl(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, context.getString(R.string.problem_http), e);
        }

        return extractMoviesFromJson(jsonString, context);
    }

    @Nullable
    private static String getJsonStringFromUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
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
}