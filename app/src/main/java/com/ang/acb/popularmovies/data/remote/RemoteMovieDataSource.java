package com.ang.acb.popularmovies.data.remote;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.data.vo.Resource;
import com.ang.acb.popularmovies.ui.movielist.MoviesFilter;
import com.ang.acb.popularmovies.utils.AppExecutors;

public class RemoteMovieDataSource {

    // For Singleton instantiation.
    private static volatile RemoteMovieDataSource sInstance;

    private final AppExecutors appExecutors;
    private final ApiService apiService;

    private static final int PAGE_SIZE = 20;

    // Prevent direct instantiation.
    private RemoteMovieDataSource(ApiService movieService,
                                  AppExecutors executors) {
        apiService = movieService;
        appExecutors = executors;
    }

    // Returns the single instance of this class, creating it if necessary.
    public static RemoteMovieDataSource getInstance(ApiService movieService,
                                                    AppExecutors executors) {
        if (sInstance == null) {
            synchronized (AppExecutors.class) {
                if (sInstance == null) {
                    sInstance = new RemoteMovieDataSource(movieService, executors);
                }
            }
        }
        return sInstance;
    }

    public LiveData<ApiResponse<Movie>> loadMovie(final long movieId) {
        return apiService.getMovieDetails(movieId);
    }

    public PagedMoviesResult loadMoviesFilteredBy(MoviesFilter sortBy) {
        // Create the data source factory.
        PagedMovieDataSourceFactory sourceFactory =  new PagedMovieDataSourceFactory(
                apiService,
                appExecutors.networkIO(),
                sortBy);

        // Create the Paging configuration.
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(PAGE_SIZE)
                .build();

        // Get the paged result.
        LiveData<PagedList<Movie>> moviesPagedList = new LivePagedListBuilder<>(sourceFactory, config)
                // Provide custom executor for network requests, otherwise it will default
                // to Arch Components' IO pool which is also used for disk access.
                .setFetchExecutor(appExecutors.networkIO())
                .build();

        // Get the network state.
        LiveData<Resource> networkState = Transformations.switchMap(
                sourceFactory.sourceLiveData,
                new Function<PagedMovieDataSource, LiveData<Resource>>() {
                    @Override
                    public LiveData<Resource> apply(PagedMovieDataSource input) {
                        return input.networkState;
                    }
                });

        // Expose the paged list result and network status to the view model.
        return new PagedMoviesResult(
                moviesPagedList,
                networkState,
                sourceFactory.sourceLiveData
        );
    }
}
