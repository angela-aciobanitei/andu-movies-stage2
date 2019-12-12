package com.ang.acb.popularmovies.data.remote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.data.vo.Resource;

import javax.inject.Inject;

public class PagedMoviesResult {

    private LiveData<PagedMovieDataSource> pagedDataSource;
    private LiveData<PagedList<Movie>> pagedData;
    private LiveData<Resource> networkState;

    @Inject
    PagedMoviesResult(LiveData<PagedMovieDataSource> pagedDataSource,
                      LiveData<PagedList<Movie>> pagedData,
                      LiveData<Resource> networkState) {
        this.pagedDataSource = pagedDataSource;
        this.pagedData = pagedData;
        this.networkState = networkState;
    }

    public LiveData<PagedMovieDataSource> getPagedDataSource() {
        return pagedDataSource;
    }

    public LiveData<PagedList<Movie>> getPagedData() {
        return pagedData;
    }

    public LiveData<Resource> getNetworkState() {
        return networkState;
    }
}
