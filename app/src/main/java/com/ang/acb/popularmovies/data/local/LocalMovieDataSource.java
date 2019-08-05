package com.ang.acb.popularmovies.data.local;

import androidx.lifecycle.LiveData;

import com.ang.acb.popularmovies.data.vo.Cast;
import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.data.vo.MovieDetails;
import com.ang.acb.popularmovies.data.vo.Review;
import com.ang.acb.popularmovies.data.vo.Trailer;
import com.ang.acb.popularmovies.utils.AppExecutors;

import java.util.List;

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
        saveCast(movie.getCreditsResponse().getCast(), movie.getId());
        saveTrailers(movie.getTrailersResponse().getTrailers(), movie.getId());
        saveReviews(movie.getReviewsResponse().getReviews(), movie.getId());
    }

    private void saveCast(List<Cast> castList, long movieId) {
        for (Cast cast : castList) {
            cast.setMovieId(movieId);
        }
        database.castDao().insertCast(castList);
        Timber.d("%s cast inserted into the database.", castList.size());
    }

    private void saveTrailers(List<Trailer> trailers, long movieId) {
        for (Trailer trailer : trailers) {
            trailer.setMovieId(movieId);
        }
        database.trailerDao().insertTrailers(trailers);
        Timber.d("%s trailers inserted into the database.", trailers.size());
    }

    private void saveReviews(List<Review> reviews, long movieId) {
        for (Review review : reviews) {
            review.setMovieId(movieId);
        }
        database.reviewDao().insertReviews(reviews);
        Timber.d("%s reviews inserted into the database.", reviews.size());
    }

    public LiveData<MovieDetails> getMovieDetails(long movieId) {
        Timber.d("Loading movie details.");
        return database.movieDao().getMovieDetails(movieId);
    }

    public LiveData<List<Movie>> getAllFavoriteMovies() {
        return database.movieDao().getAllFavoriteMovies();
    }

    public void markAsFavorite(Movie movie) {
        database.movieDao().markAsFavorite(movie.getId());
    }

    public void markAsNotFavorite(Movie movie) {
        database.movieDao().markAsNotFavorite(movie.getId());
    }
}
