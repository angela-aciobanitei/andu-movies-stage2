package com.ang.acb.popularmovies.data.remote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.data.vo.Resource;
import com.ang.acb.popularmovies.data.vo.MoviesFilter;
import com.ang.acb.popularmovies.utils.AppExecutors;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RemoteMovieDataSource {

    private final AppExecutors appExecutors;
    private final ApiService apiService;

    private static final int PAGE_SIZE = 20;

    @Inject
    public RemoteMovieDataSource(ApiService apiService,
                                  AppExecutors appExecutors) {
        this.apiService = apiService;
        this.appExecutors = appExecutors;
    }

    public LiveData<ApiResponse<Movie>> loadAllMovieDetails(final long movieId) {
        return apiService.getAllMovieDetails(movieId);
    }

    public PagedMoviesResult loadMoviesFilteredBy(MoviesFilter sortBy) {
        // Create the paged data source factory.
        PagedMovieDataSourceFactory sourceFactory =  new PagedMovieDataSourceFactory(
                apiService,
                sortBy,
                appExecutors.networkIO());

        // Create the Paging configuration.
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(PAGE_SIZE)
                .build();

        // Get the paged data.
        LiveData<PagedList<Movie>> pagedData = new LivePagedListBuilder<>(sourceFactory, config)
                // Provide custom executor for network requests, otherwise it will default
                // to Arch Components' IO pool which is also used for disk access.
                .setFetchExecutor(appExecutors.networkIO())
                .build();

        // Get the network state.
        LiveData<Resource> networkState = Transformations.switchMap(
                sourceFactory.getPagedDataSource(),
                PagedMovieDataSource::getNetworkState);

        // Expose the paged data and network status to the view model.
        return new PagedMoviesResult(
                sourceFactory.getPagedDataSource(),
                pagedData,
                networkState);
    }
}
