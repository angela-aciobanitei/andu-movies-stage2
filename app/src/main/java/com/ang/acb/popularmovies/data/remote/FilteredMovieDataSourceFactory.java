package com.ang.acb.popularmovies.data.remote;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.data.vo.MoviesFilter;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

import javax.inject.Inject;

/**
 * A simple data source factory which also provides a way to observe the last created
 * data source. This allows us to channel its network request status etc back to the UI.
 *
 * See: https://developer.android.com/topic/libraries/architecture/paging/data#custom-data-source
 * See: https://github.com/googlesamples/android-architecture-components/tree/master/PagingWithNetworkSample
 */
public class FilteredMovieDataSourceFactory extends DataSource.Factory<Integer, Movie> {

    private final ApiService apiService;
    private final MoviesFilter filter;
    private final Executor networkExecutor;

    private MutableLiveData<FilteredMovieDataSource> pagedDataSource = new MutableLiveData<>();

    @Inject
    FilteredMovieDataSourceFactory(ApiService apiService,
                                   MoviesFilter filter,
                                   Executor networkExecutor) {
        this.apiService = apiService;
        this.filter = filter;
        this.networkExecutor = networkExecutor;
    }

    @NotNull
    @Override
    public DataSource<Integer, Movie> create() {
        FilteredMovieDataSource movieDataSource = new FilteredMovieDataSource(
                apiService,
                filter,
                networkExecutor);
        pagedDataSource.postValue(movieDataSource);
        return movieDataSource;
    }

    public MutableLiveData<FilteredMovieDataSource> getPagedDataSource() {
        return pagedDataSource;
    }
}