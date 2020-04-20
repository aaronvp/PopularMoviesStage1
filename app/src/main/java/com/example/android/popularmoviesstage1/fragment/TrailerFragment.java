package com.example.android.popularmoviesstage1.fragment;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.popularmoviesstage1.R;
import com.example.android.popularmoviesstage1.adapter.TrailerAdapter;
import com.example.android.popularmoviesstage1.model.Trailer;
import com.example.android.popularmoviesstage1.model.TrailerList;
import com.example.android.popularmoviesstage1.util.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrailerFragment extends Fragment implements TrailerAdapter.ListItemClickListener {

    RecyclerView trailerRecyclerView;
    TrailerAdapter trailerAdapter;
    TrailerList trailerList;

    private static final String MOVIE_TRAILERS_EXTRA = "movie_trailers";
    private static final int FETCH_TRAILERS_LOADER = 22;

    private int movieId;

    public TrailerFragment(int movieId) {
        this.movieId = movieId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trailer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trailerRecyclerView = view.findViewById(R.id.rv_trailers);
        trailerAdapter = new TrailerAdapter(this);
        trailerRecyclerView.setAdapter(trailerAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        trailerRecyclerView.setLayoutManager(layoutManager);
        trailerRecyclerView.addItemDecoration(
                new DividerItemDecoration(trailerRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        getTrailers();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Trailer trailer = trailerList.trailers.get(clickedItemIndex);
        NetworkUtils.openWebPage(getContext(), NetworkUtils.getYouTubeURL(trailer.getKey()));
    }

    private void getTrailers() {
        URL movieTrailersURL = NetworkUtils.buildTrailersURL(movieId);
        Bundle bundle = new Bundle();
        bundle.putString(MOVIE_TRAILERS_EXTRA, movieTrailersURL.toString());
        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        Loader<String> movieTrailersLoader = loaderManager.getLoader(FETCH_TRAILERS_LOADER);
        if (movieTrailersLoader == null) {
            loaderManager.initLoader(FETCH_TRAILERS_LOADER, bundle, new TrailersCallBack());
        } else {
            loaderManager.restartLoader(FETCH_TRAILERS_LOADER, bundle, new TrailersCallBack());
        }
    }

    private class TrailersCallBack implements LoaderManager.LoaderCallbacks<String> {

        @NonNull
        @Override
        public Loader<String> onCreateLoader(int i, final Bundle bundle) {
            return new AsyncTaskLoader<String>(getContext()) {

                String movieTrailersJson;

                @Override
                protected void onStartLoading() {
                    if (bundle == null) {
                        return;
                    }

                    if (movieTrailersJson != null) {
                        deliverResult(movieTrailersJson);
                    } else {
                        forceLoad();
                    }
                }

                @Nullable
                @Override
                public String loadInBackground() {
                    String movieTrailersURLString = bundle.getString(MOVIE_TRAILERS_EXTRA);
                    if (TextUtils.isEmpty(movieTrailersURLString)) {
                        return null;
                    }
                    try {
                        URL movieTrailersURL = new URL(movieTrailersURLString);
                        return NetworkUtils.getResponseFromHttpUrl(movieTrailersURL);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                public void deliverResult(@Nullable String movieTrailersJson) {
                    this.movieTrailersJson = movieTrailersJson;
                    super.deliverResult(movieTrailersJson);
                }
            };
        }

        @Override
        public void onLoadFinished(@NonNull Loader<String> loader, String trailers) {
            if (trailers == null) {
                Toast.makeText(getContext(), R.string.detail_error_message, Toast.LENGTH_SHORT).show();
            } else {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                try {
                    trailerList = gson.fromJson(trailers, TrailerList.class);
                    Log.i("Movies", "Setting trailer adapter : " + trailerList.trailers.size());
                    trailerAdapter.setTrailerData(trailerList.trailers);
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
