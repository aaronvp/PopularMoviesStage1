package com.example.android.popularmoviesstage1.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.popularmoviesstage1.R;
import com.example.android.popularmoviesstage1.adapter.ReviewAdapter;
import com.example.android.popularmoviesstage1.model.ReviewList;
import com.example.android.popularmoviesstage1.util.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment {

    private ReviewAdapter reviewAdapter;

    private static final String MOVIE_REVIEWS_EXTRA = "movie_reviews";
    private static final int FETCH_REVIEWS_LOADER = 23;
    
    private final int movieId;

    public ReviewFragment(int movieId) {
        this.movieId = movieId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView reviewRecyclerView = view.findViewById(R.id.rv_reviews);
        reviewAdapter = new ReviewAdapter();
        reviewRecyclerView.setAdapter(reviewAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        reviewRecyclerView.setLayoutManager(layoutManager);
        reviewRecyclerView.addItemDecoration(
                new DividerItemDecoration(reviewRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        getReviews();
    } 

    private void getReviews() {
        Log.i("Movies", "Fetching Reviews");
        URL movieReviewsURL = NetworkUtils.buildReviewsURL(movieId);
        Bundle bundle = new Bundle();
        bundle.putString(MOVIE_REVIEWS_EXTRA, movieReviewsURL.toString());
        LoaderManager loaderManager = Objects.requireNonNull(getActivity()).getSupportLoaderManager();
        Loader<String> movieTrailersLoader = loaderManager.getLoader(FETCH_REVIEWS_LOADER);
        if (movieTrailersLoader == null) {
            loaderManager.initLoader(FETCH_REVIEWS_LOADER, bundle, new ReviewsCallBack());
        } else {
            loaderManager.restartLoader(FETCH_REVIEWS_LOADER, bundle, new ReviewsCallBack());
        }
    }
    
    private class ReviewsCallBack implements LoaderManager.LoaderCallbacks<String> {

        @NonNull
        @Override
        public Loader<String> onCreateLoader(int i, final Bundle bundle) {
            return new AsyncTaskLoader<String>(Objects.requireNonNull(getContext())) {

                String movieReviewsJson;

                @Override
                protected void onStartLoading() {
                    if (bundle == null) {
                        return;
                    }

                    if (movieReviewsJson != null) {
                        deliverResult(movieReviewsJson);
                    } else {
                        forceLoad();
                    }
                }

                @Nullable
                @Override
                public String loadInBackground() {
                    String movieReviewsURLString = bundle.getString(MOVIE_REVIEWS_EXTRA);
                    if (TextUtils.isEmpty(movieReviewsURLString)) {
                        return null;
                    }
                    try {
                        URL movieReviewsURL = new URL(movieReviewsURLString);
                        return NetworkUtils.getResponseFromHttpUrl(movieReviewsURL);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                public void deliverResult(@Nullable String movieReviewsJson) {
                    this.movieReviewsJson = movieReviewsJson;
                    super.deliverResult(movieReviewsJson);
                }
            };
        }

        @Override
        public void onLoadFinished(@NonNull Loader<String> loader, String reviews) {
            if (reviews == null) {
                Toast.makeText(getContext(), R.string.detail_error_message, Toast.LENGTH_SHORT).show();
            } else {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                try {
                    ReviewList reviewList = gson.fromJson(reviews, ReviewList.class);
                    reviewAdapter.setReviewData(reviewList.reviews);
                } catch (Exception e) {
                    Log.e("Error", Objects.requireNonNull(e.getMessage()));
                }
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<String> loader) {
            // No implementation required
        }
    }

}
