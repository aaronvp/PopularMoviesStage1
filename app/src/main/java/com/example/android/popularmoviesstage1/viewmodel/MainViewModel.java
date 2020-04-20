package com.example.android.popularmoviesstage1.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.android.popularmoviesstage1.database.AppDatabase;
import com.example.android.popularmoviesstage1.model.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final LiveData<List<Movie>> movies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase movieDatabase = AppDatabase.getInstance(this.getApplication());
        movies = movieDatabase.movieDao().loadAllMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }
}
