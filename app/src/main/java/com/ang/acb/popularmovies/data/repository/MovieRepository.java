package com.ang.acb.popularmovies.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.ang.acb.popularmovies.data.local.LocalMovieDataSource;
import com.ang.acb.popularmovies.data.remote.ApiResponse;
import com.ang.acb.popularmovies.data.remote.PagedMoviesResult;
import com.ang.acb.popularmovies.data.remote.RemoteMovieDataSource;
import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.data.vo.MovieDetails;
import com.ang.acb.popularmovies.data.vo.Resource;
import com.ang.acb.popularmovies.ui.movielist.MoviesFilter;
import com.ang.acb.popularmovies.utils.AppExecutors;

import java.util.List;

import timber.log.Timber;

/**
 * Repository module for handling data operations.
 *
 * See: https://developer.android.com/jetpack/docs/guide#truth
 * See: https://github.com/googlesamples/android-architecture-components/tree/master/GithubBrowserSample
 */
public class MovieRepository implements MovieDataSource {

    // For Singleton instantiation.
    private static volatile MovieRepository sInstance;

    private final LocalMovieDataSource localDataSource;
    private final RemoteMovieDataSource remoteDataSource;
    private final AppExecutors appExecutors;

    // Prevent direct instantiation.
    private MovieRepository(LocalMovieDataSource localDataSource,
                            RemoteMovieDataSource remoteDataSource,
                            AppExecutors appExecutors) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
        this.appExecutors = appExecutors;
    }

    // Returns the single instance of this class, creating it if necessary.
    public static MovieRepository getInstance(LocalMovieDataSource localDataSource,
                                              RemoteMovieDataSource remoteDataSource,
                                              AppExecutors executors) {
        if (sInstance == null) {
            synchronized (MovieRepository.class) {
                if (sInstance == null) {
                    sInstance = new MovieRepository(localDataSource, remoteDataSource, executors);
                }
            }
        }
        return sInstance;
    }

    @Override
    public LiveData<Resource<MovieDetails>> loadAllMovieDetails(final long movieId) {
        // Here we are using the NetworkBoundResource that we've created earlier which
        // can provide a resource backed by both the SQLite database and the network.
        return new NetworkBoundResource<MovieDetails, Movie>(appExecutors) {

            @NonNull
            @Override
            protected LiveData<ApiResponse<Movie>> createCall() {
                // Create the API call to load the movie details.
                Timber.d("Downloading movie from network");
                return remoteDataSource.loadAllMovieDetails(movieId);
            }

            @Override
            protected void saveCallResult(@NonNull Movie item) {
                // Save the result of the API response into the database.
                localDataSource.saveAllMovieDetails(item);
                Timber.d("Movie saved to database");
            }

            @Override
            protected boolean shouldFetch(@Nullable MovieDetails data) {
                // Decide whether to fetch potentially updated data from the network.
                // Note: only fetch fresh data if it doesn't exist in database.
                return data == null;
            }

            @NonNull
            @Override
            protected void onFetchFailed() {
                // Ignored
                Timber.d("Fetch failed!!");
            }

            @NonNull
            @Override
            protected LiveData<MovieDetails> loadFromDb() {
                // Get the cached data from the database.
                Timber.d("Loading movie from database");
                return localDataSource.getAllMovieDetails(movieId);
            }
        }.getAsLiveData();
    }

    @Override
    public PagedMoviesResult loadMoviesFilteredBy(MoviesFilter sortBy) {
        return remoteDataSource.loadMoviesFilteredBy(sortBy);
    }

    @Override
    public LiveData<List<Movie>> getAllFavoriteMovies() {
        return localDataSource.getAllFavoriteMovies();
    }

    @Override
    public void markAsFavorite(final Movie movie) {
        appExecutors.diskIO().execute(() -> {
            Timber.d("Adding movie to favorites");
            localDataSource.markAsFavorite(movie);
        });
    }

    @Override
    public void markAsNotFavorite(final Movie movie) {
        appExecutors.diskIO().execute(() -> {
            Timber.d("Removing movie from favorites");
            localDataSource.markAsNotFavorite(movie);
        });
    }

}
