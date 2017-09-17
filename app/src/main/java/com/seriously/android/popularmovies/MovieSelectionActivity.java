package com.seriously.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.seriously.android.popularmovies.utilities.NetworkUtils;

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
        handleConnection();
        mRequestUrl.setText(NetworkUtils.buildUrl(
                queryType, getString(R.string.themoviedb_api_key)).toString());
        mResultData.setText(R.string.result_promise);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleConnection();
    }

    private boolean handleConnection() {
        boolean isConnected = NetworkUtils.isConnected(this);
        mNoConnection.setVisibility(isConnected ? GONE : VISIBLE);
        return isConnected;
    }
}
