package com.ang.acb.popularmovies.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.data.vo.MovieDetails;

import java.util.List;

/**
 * Interface for database access on {@link Movie} related operations.
 */
@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovieDetails(Movie movie);

    @Transaction
    @Query("SELECT * FROM movie WHERE movie.id= :movieId")
    LiveData<MovieDetails> getAllMovieDetails(long movieId);

    @Query("SELECT * FROM movie WHERE is_favorite = 1")
    LiveData<List<Movie>> getAllFavoriteMovies();

    @Query("UPDATE movie SET is_favorite = 1 WHERE id = :movieId")
    void markAsFavorite(long movieId);

    @Query("UPDATE movie SET is_favorite = 0 WHERE id = :movieId")
    void markAsNotFavorite(long movieId);
}