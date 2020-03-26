package com.example.android.popularmoviesstage1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.android.popularmoviesstage1.R;
import com.example.android.popularmoviesstage1.model.Movie;
import com.example.android.popularmoviesstage1.util.ApplicationConstants;
import com.example.android.popularmoviesstage1.util.NetworkUtils;

@SuppressWarnings("deprecation")
public class MovieDetails extends AppCompatActivity {

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        ImageView moviePosterIV = findViewById(R.id.iv_movie_details_poster);
        TextView movieTitleTV = findViewById(R.id.tv_movie_details_title);
        TextView voteAverageTV = findViewById(R.id.tv_movie_details_vote_average);
        TextView releaseDateTV = findViewById(R.id.tv_movie_details_release_date);
        TextView overviewTV = findViewById(R.id.tv_movie_details_overview);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        } else {
            if (intent.hasExtra(ApplicationConstants.INTENT_KEY_MOVIE)) {
                this.movie = intent.getParcelableExtra(ApplicationConstants.INTENT_KEY_MOVIE);
            }

            Glide.with(this)
                    .load(NetworkUtils.buildImageUrl(movie.getPosterPath()))
                    .placeholder(R.drawable.large_movie_poster)
                    .into(moviePosterIV);
            movieTitleTV.setText(movie.getTitle());
            voteAverageTV.setText(String.valueOf(movie.getVoteAverage()));
            releaseDateTV.setText(movie.getReleaseDate());
            overviewTV.setText(movie.getOverview());
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

}
