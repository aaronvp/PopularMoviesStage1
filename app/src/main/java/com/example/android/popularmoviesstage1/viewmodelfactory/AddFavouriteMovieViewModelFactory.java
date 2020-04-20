
package com.example.android.popularmoviesstage1.viewmodelfactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.android.popularmoviesstage1.database.AppDatabase;
import com.example.android.popularmoviesstage1.viewmodel.AddFavouriteMovieViewModel;

public class AddFavouriteMovieViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    // COMPLETED (2) Add two member variables. One for the database and one for the taskId
    private final AppDatabase movieDatabase;
    private final int movieId;

    // COMPLETED (3) Initialize the member variables in the constructor with the parameters received
    public AddFavouriteMovieViewModelFactory(AppDatabase database, int taskId) {
        movieDatabase = database;
        movieId = taskId;
    }

    // COMPLETED (4) Uncomment the following method
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        // noinspection unchecked
        return (T) new AddFavouriteMovieViewModel(movieDatabase, movieId);
    }
}

