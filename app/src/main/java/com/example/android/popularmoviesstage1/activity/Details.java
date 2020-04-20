package com.example.android.popularmoviesstage1.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;
import com.example.android.popularmoviesstage1.R;
import com.example.android.popularmoviesstage1.adapter.MoviePagerAdapter;
import com.example.android.popularmoviesstage1.database.AppDatabase;
import com.example.android.popularmoviesstage1.databinding.DetailsBinding;
import com.example.android.popularmoviesstage1.model.Movie;
import com.example.android.popularmoviesstage1.util.AppExecutors;
import com.example.android.popularmoviesstage1.util.ApplicationConstants;
import com.example.android.popularmoviesstage1.util.NetworkUtils;
import com.example.android.popularmoviesstage1.viewmodel.AddFavouriteMovieViewModel;
import com.example.android.popularmoviesstage1.viewmodelfactory.AddFavouriteMovieViewModelFactory;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

@SuppressWarnings("deprecation")
public class Details extends AppCompatActivity {

    Movie movie;
    DetailsBinding movieDetailBinding;
    private AppDatabase movieDatabase;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        movieDatabase = AppDatabase.getInstance(context);
        movieDetailBinding = DataBindingUtil.setContentView(this, R.layout.details);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        } else {
            if (intent.hasExtra(ApplicationConstants.INTENT_KEY_MOVIE)) {
                this.movie = intent.getParcelableExtra(ApplicationConstants.INTENT_KEY_MOVIE);
                movieDetailBinding.detailMovieTitle.setText(movie.getTitle());
                Glide.with(this)
                        .load(NetworkUtils.buildImageUrl(movie.getPosterPath()))
                        .placeholder(R.drawable.large_movie_poster)
                        .into(movieDetailBinding.detailMoviePoster);
                movieDetailBinding.detailMovieYear.setText(movie.getReleaseDate());
                movieDetailBinding.detailMovieRating.setText(movie.getVoteAverage().toString() + " / 10");
                movieDetailBinding.detailOverview.setText(movie.getOverview());

                AddFavouriteMovieViewModelFactory movieViewModelFactory =
                        new AddFavouriteMovieViewModelFactory(movieDatabase, movie.getId());
                final AddFavouriteMovieViewModel movieViewModel = ViewModelProviders.of(this, movieViewModelFactory)
                        .get(AddFavouriteMovieViewModel.class);
                movieViewModel.getFavouriteMovie().observe(this, new Observer<Movie>() {
                    @Override
                    public void onChanged(Movie movie) {
                        movieViewModel.getFavouriteMovie().removeObserver(this);
                        movieDetailBinding.favButton.setChecked(true);
                    }
                });

                movieDetailBinding.favButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onFavouriteButtonClicked(movieDetailBinding.favButton.isChecked(), movie);
                    }
                });

                movieDetailBinding.movieViewPager.setAdapter(new MoviePagerAdapter(this, movie.getId()));
                TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(movieDetailBinding.tabLayout,
                        movieDetailBinding.movieViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                        switch (position) {
                            case 0: {
                                tab.setText(R.string.trailers);
                                tab.setIcon(R.drawable.ic_trailers);
                                break;
                            }
                            case 1: {
                                tab.setText(R.string.reviews);
                                tab.setIcon(R.drawable.ic_review);
                                break;
                            }
                        }
                    }
                });
                tabLayoutMediator.attach();

                movieDetailBinding.movieViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                    }
                });
            }
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    public void onFavouriteButtonClicked(final boolean save, final Movie movie) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (save) {
                    movieDatabase.movieDao().insertMovie(movie);
                } else {
                    movieDatabase.movieDao().deleteMovie(movie);
                }
            }
        });
    }

}
