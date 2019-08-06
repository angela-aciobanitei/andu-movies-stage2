package com.ang.acb.popularmovies.data.remote;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.ui.movielist.MoviesFilter;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

/**
 * A simple data source factory which also provides a way to observe the last created
 * data source. This allows us to channel its network request status etc back to the UI.
 *
 * See: https://developer.android.com/topic/libraries/architecture/paging/data#custom-data-source
 * See: https://github.com/googlesamples/android-architecture-components/tree/master/PagingWithNetworkSample
 */
public class PagedMovieDataSourceFactory extends DataSource.Factory<Integer, Movie> {

    private MutableLiveData<PagedMovieDataSource> sourceLiveData = new MutableLiveData<>();

    private final ApiService movieService;
    private final MoviesFilter sortBy;
    private final Executor networkExecutor;

    public PagedMovieDataSourceFactory(ApiService movieService,
                                       MoviesFilter sortBy,
                                       Executor networkExecutor) {
        this.movieService = movieService;
        this.sortBy = sortBy;
        this.networkExecutor = networkExecutor;
    }

    @NotNull
    @Override
    public DataSource<Integer, Movie> create() {
        PagedMovieDataSource movieDataSource = new PagedMovieDataSource(
                movieService,
                sortBy,
                networkExecutor);
        sourceLiveData.postValue(movieDataSource);
        return movieDataSource;
    }

    public MutableLiveData<PagedMovieDataSource> getSourceLiveData() {
        return sourceLiveData;
    }
}