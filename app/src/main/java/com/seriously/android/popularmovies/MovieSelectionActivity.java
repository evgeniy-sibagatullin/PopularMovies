package com.seriously.android.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.seriously.android.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MovieSelectionActivity extends AppCompatActivity {

    private View mNoConnection;
    private TextView mRequestUrl;
    private TextView mResultData;

    final static String QUERY_TYPE_POPULAR = "popular";
    final static String QUERY_TYPE_TOP_RATED = "top_rated";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_selection);

        mNoConnection = findViewById(R.id.no_connection);
        mRequestUrl = (TextView) findViewById(R.id.request_url);
        mResultData = (TextView) findViewById(R.id.result_data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.query_type, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_query_type_popular:
                handleQueryTypeSelection(QUERY_TYPE_POPULAR);
                return true;
            case R.id.item_query_type_top_rated:
                handleQueryTypeSelection(QUERY_TYPE_TOP_RATED);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleQueryTypeSelection(String queryType) {
        URL queryUlr = NetworkUtils.buildUrl(queryType, getString(R.string.themoviedb_api_key));
        mRequestUrl.setText(queryUlr.toString());

        boolean isConnected = NetworkUtils.isConnected(this);
        setupNoConnectionView(isConnected);

        if (isConnected) {
            new QueryTask().execute(queryUlr);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupNoConnectionView(NetworkUtils.isConnected(this));
    }

    private void setupNoConnectionView(boolean isConnected) {
        mNoConnection.setVisibility(isConnected ? GONE : VISIBLE);
    }

    private class QueryTask extends AsyncTask<URL, Void, String> {

        @Override
        @Nullable
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String response = null;

            try {
                response = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null && !response.trim().isEmpty()) {
                mResultData.setText(response);
            }
        }
    }
}
