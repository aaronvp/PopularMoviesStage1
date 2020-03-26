package com.example.android.popularmoviesstage1.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.popularmoviesstage1.R;
import com.example.android.popularmoviesstage1.adapter.MovieAdapter;
import com.example.android.popularmoviesstage1.model.Movie;
import com.example.android.popularmoviesstage1.model.MovieList;
import com.example.android.popularmoviesstage1.util.ApplicationConstants;
import com.example.android.popularmoviesstage1.util.NetworkUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {

    private MovieAdapter movieAdapter;
    private ProgressBar loadingIndicator;
    RecyclerView movieRecyclerView;
    MovieList movieList;
    Context context;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieRecyclerView = findViewById(R.id.rv_movies);
        loadingIndicator = findViewById((R.id.pb_loading_indicator));
        context = MainActivity.this;

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        movieRecyclerView.setLayoutManager(gridLayoutManager);

        movieAdapter = new MovieAdapter(this);
        movieRecyclerView.setAdapter(movieAdapter);
        makeMovieSearchQuery(ApplicationConstants.MOVIE_SORT_BY_MOST_POPULAR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sort_by_most_popular) {
            makeMovieSearchQuery(ApplicationConstants.MOVIE_SORT_BY_MOST_POPULAR);
            return true;
        } else if (item.getItemId() == R.id.sort_by_highest_rating) {
            makeMovieSearchQuery(ApplicationConstants.MOVIE_SORT_BY_HIGHEST_RATING);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Movie movie = movieList.movies.get(clickedItemIndex);
        Intent intent = new Intent(context, MovieDetails.class);
        intent.putExtra(ApplicationConstants.INTENT_KEY_MOVIE, movie);
        startActivity(intent);
    }

    @SuppressLint("StaticFieldLeak")
    public class MovieDatabaseQuery extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String movieSearchResults = null;
            try {
                movieSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                Log.e(TAG, Objects.requireNonNull(e.getMessage()));
            }
            return movieSearchResults;
        }

        @Override
        protected void onPostExecute(String movieSearchResults) {
            // COMPLETED (27) As soon as the loading is complete, hide the loading indicator
            loadingIndicator.setVisibility(View.INVISIBLE);
            if (movieSearchResults != null && !movieSearchResults.equals("")) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                try {
                    movieList = gson.fromJson(movieSearchResults, MovieList.class);
                    movieAdapter.setMovieData(movieList.movies);
                } catch (Exception e) {
                    Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                }
            } else {
                Snackbar.make(movieRecyclerView, getString(R.string.error_loading_movies), Snackbar.LENGTH_INDEFINITE).show();
                Log.e(TAG, "No movies results");
            }
        }
    }

    private void makeMovieSearchQuery(String sortBy) {
        URL movieSearchURL = NetworkUtils.buildUrl(sortBy);
        new MovieDatabaseQuery().execute(movieSearchURL);
    }

}
