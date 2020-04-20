package com.example.android.popularmoviesstage1.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.popularmoviesstage1.R;
import com.example.android.popularmoviesstage1.adapter.MovieAdapter;
import com.example.android.popularmoviesstage1.model.Movie;
import com.example.android.popularmoviesstage1.model.MovieList;
import com.example.android.popularmoviesstage1.util.ApplicationConstants;
import com.example.android.popularmoviesstage1.util.NetworkUtils;
import com.example.android.popularmoviesstage1.viewmodel.MainViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {

    private MovieAdapter movieAdapter;
    private ProgressBar loadingIndicator;
    private RecyclerView movieRecyclerView;
    private List<Movie> movies;
    private Context context;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String KEY_LIFECYCLE_MOVIES = "Movies";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        movieRecyclerView = findViewById(R.id.rv_movies);
        loadingIndicator = findViewById((R.id.pb_loading_indicator));

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        movieRecyclerView.setLayoutManager(gridLayoutManager);
        movieAdapter = new MovieAdapter(this);
        movieRecyclerView.setAdapter(movieAdapter);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_LIFECYCLE_MOVIES)) {
                Log.i(KEY_LIFECYCLE_MOVIES, "OnSaveInstanceState - Loading Saved Movies");
                movies = savedInstanceState.getParcelableArrayList(KEY_LIFECYCLE_MOVIES);
                movieAdapter.setMovieData(movies);
            }
        }

        makeMovieSearchQuery(ApplicationConstants.MOVIE_SORT_BY_MOST_POPULAR);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(KEY_LIFECYCLE_MOVIES, "OnSaveInstanceState - Saving Movies");
        outState.putParcelableArrayList(KEY_LIFECYCLE_MOVIES, new ArrayList<Parcelable>(movies));
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
        } else if (item.getItemId() == R.id.sort_by_favourites) {
            setupViewModel();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Movie movie = movies.get(clickedItemIndex);
        Intent intent = new Intent(context, Details.class);
        intent.putExtra(ApplicationConstants.INTENT_KEY_MOVIE, movie);
        startActivity(intent);
    }

    @SuppressLint("StaticFieldLeak")
    public class FetchMovies extends AsyncTask<URL, Void, String> {

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
                Log.i("Movies", "Fetching Movies from TheMovieDB");
                movieSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                Log.e(TAG, Objects.requireNonNull(e.getMessage()));
            }
            return movieSearchResults;
        }

        @Override
        protected void onPostExecute(String movieSearchResults) {
            loadingIndicator.setVisibility(View.INVISIBLE);
            if (movieSearchResults != null && !movieSearchResults.equals("")) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                try {
                    MovieList movieList = gson.fromJson(movieSearchResults, MovieList.class);
                    movies = movieList.movies;
                    movieAdapter.setMovieData(movies);
                } catch (Exception e) {
                    Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                }
            } else {
                Snackbar.make(movieRecyclerView, getString(R.string.error_loading_movies),
                        Snackbar.LENGTH_INDEFINITE).show();
                Log.e(TAG, "No movies results");
            }
        }
    }

    private void makeMovieSearchQuery(String sortBy) {
        URL movieSearchURL = NetworkUtils.buildUrl(sortBy);
        new FetchMovies().execute(movieSearchURL);
    }

    private void setupViewModel() {
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> favouriteMovies) {
                movies = favouriteMovies;
                movieAdapter.setMovieData(movies);
            }
        });
    }

}
