package com.ang.acb.popularmovies.data.local;

import androidx.lifecycle.LiveData;

import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.utils.AppExecutors;

import timber.log.Timber;

/**
 * Concrete implementation of a data source as a database.
 *
 * See: https://github.com/googlesamples/android-architecture/tree/todo-mvp/todoapp
 */
public class LocalMovieDataSource {

    // For Singleton instantiation.
    private static volatile LocalMovieDataSource sInstance;
    private final AppDatabase database;

    // Prevent direct instantiation.
    private LocalMovieDataSource(AppDatabase database) {
        this.database = database;
    }

    // Returns the single instance of this class, creating it if necessary.
    public static LocalMovieDataSource getInstance(AppDatabase database) {
        if (sInstance == null) {
            synchronized (AppExecutors.class) {
                if (sInstance == null) {
                    sInstance = new LocalMovieDataSource(database);
                }
            }
        }
        return sInstance;
    }

    public void saveAllMovieDetails(Movie movie) {
        database.movieDao().insertMovieDetails(movie);
    }

    public LiveData<Movie> getMovieDetails(long movieId) {
        Timber.d("Loading movie details.");
        return database.movieDao().getMovieDetails(movieId);
    }

}
