package com.ang.acb.popularmovies.data.remote;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.data.vo.Resource;

import javax.inject.Inject;

public class FilteredMoviesResult {

    private LiveData<FilteredMovieDataSource> pagedDataSource;
    // The LiveData of paged lists for the UI to observe.
    // A PagedList loads chunks of data, or pages. As more data is needed,
    // it's paged into the existing PagedList object. If any loaded data
    // changes, a new instance of PagedList is emitted to the observable
    // data holder from a LiveData or RxJava2-based object.
    private LiveData<PagedList<Movie>> pagedData;
    // The network request status to show to the user.
    private LiveData<Resource> networkState;

    @Inject
    FilteredMoviesResult(LiveData<FilteredMovieDataSource> pagedDataSource,
                         LiveData<PagedList<Movie>> pagedData,
                         LiveData<Resource> networkState) {
        this.pagedDataSource = pagedDataSource;
        this.pagedData = pagedData;
        this.networkState = networkState;
    }

    public LiveData<FilteredMovieDataSource> getPagedDataSource() {
        return pagedDataSource;
    }

    public LiveData<PagedList<Movie>> getPagedData() {
        return pagedData;
    }

    public LiveData<Resource> getNetworkState() {
        return networkState;
    }
}
