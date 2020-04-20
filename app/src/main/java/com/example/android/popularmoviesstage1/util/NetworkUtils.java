package com.example.android.popularmoviesstage1.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    private static final String MOVIE_DB_DISCOVER_URL = "https://api.themoviedb.org/3/discover/movie?";
    private static final String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    private static final String PARAM_API_KEY = "api_key";
    // Add in API key here
    private static final String MOVIE_DB_API_KEY = "";
    public static final String MOVIE_POSTER_SIZE = "w500";
    private static final String PARAM_SORT = "sort_by";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_VIDEOS = "videos";
    private static final String PARAM_REVIEWS = "reviews";
    static final String PAGE = "1";
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private NetworkUtils() {

    }

    /**
     * Builds the URL used to query GitHub.
     *
     * @return The URL to use to query the GitHub server.
     */
    public static URL buildFetchMoviesUrl(String sortBy) {
        Uri builtUri = Uri.parse(MOVIE_DB_DISCOVER_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, MOVIE_DB_API_KEY)
                .appendQueryParameter(PARAM_SORT, sortBy)
                .appendQueryParameter(PARAM_PAGE, PAGE)
                .appendQueryParameter(PARAM_PAGE, PAGE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }

        return url;
    }

    public static URL buildImageUrl(String imageURL) {
        Uri builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(MOVIE_POSTER_SIZE)
                .appendEncodedPath(imageURL)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
        return url;
    }

    public static URL buildTrailersURL(int movieId) {
        return buildMovieDetailURL(movieId, PARAM_VIDEOS);
    }

    public static URL buildReviewsURL(int movieId) {
        return buildMovieDetailURL(movieId, PARAM_REVIEWS);
    }

    private static URL buildMovieDetailURL(int movieId, String movieDetail) {
        Uri builtUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(String.valueOf(movieId))
                .appendEncodedPath(movieDetail)
                .appendQueryParameter(PARAM_API_KEY, MOVIE_DB_API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
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

    public static String getYouTubeURL(String videoKey) {
        return YOUTUBE_BASE_URL.concat(videoKey);
    }

    public static void openWebPage(Context context, String url) {

        Uri webPage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

}

