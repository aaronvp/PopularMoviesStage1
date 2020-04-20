package com.example.android.popularmoviesstage1.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.android.popularmoviesstage1.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> loadAllMovies();

    @Query("SELECT * FROM movie WHERE id = :movieId")
    LiveData<Movie> loadMovieById(int movieId);

    @Insert
    void insertMovie(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

}

