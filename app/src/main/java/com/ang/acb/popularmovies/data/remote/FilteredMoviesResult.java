package com.ang.acb.popularmovies.data.remote;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.data.vo.Resource;

import javax.inject.Inject;

public class FilteredMoviesResult {

    private LiveData<FilteredMovieDataSource> pagedDataSource;
    private LiveData<PagedList<Movie>> pagedData;
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
