package com.ang.acb.popularmovies.data.local;

import androidx.lifecycle.LiveData;

import com.ang.acb.popularmovies.data.vo.Cast;
import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.data.vo.MovieDetails;
import com.ang.acb.popularmovies.data.vo.Review;
import com.ang.acb.popularmovies.data.vo.Trailer;
import com.ang.acb.popularmovies.utils.AppExecutors;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Concrete implementation of a data source as a database.
 */
@Singleton
public class LocalMovieDataSource {


    private final AppDatabase database;

    @Inject
    public LocalMovieDataSource(AppDatabase database) {
        this.database = database;
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
        database.castDao().insertCastList(castList);
        Timber.d("%s cast members inserted into the database.", castList.size());
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

    public LiveData<MovieDetails> getAllMovieDetails(long movieId) {
        Timber.d("Loading movie details.");
        return database.movieDao().getAllMovieDetails(movieId);
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
