
package com.example.android.popularmoviesstage1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.android.popularmoviesstage1.database.AppDatabase;
import com.example.android.popularmoviesstage1.model.Movie;

public class AddFavouriteMovieViewModel extends ViewModel {

    private final LiveData<Movie> favouriteMovie;

    public AddFavouriteMovieViewModel(AppDatabase database, int movieId) {
        favouriteMovie = database.movieDao().loadMovieById(movieId);
    }

    public LiveData<Movie> getFavouriteMovie() {
        return favouriteMovie;
    }

}

