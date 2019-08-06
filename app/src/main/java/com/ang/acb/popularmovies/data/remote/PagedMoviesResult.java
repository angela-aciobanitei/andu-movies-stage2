package com.ang.acb.popularmovies.data.remote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.data.vo.Resource;

/**
 *  By using PagedList + DataSource we only load small pages to memory that we
 *  actually need. Reading the pages (and elements) occurs on background thread.
 *
 *  The Paging Library gives us:
 *      – PagedList: exposes data
 *      – DataSource: fills paged list
 *      – DataSource.Factory: creates a data source
 *      – PagedListAdapter: consumes paged list
 *      – LivePagedListBuilder: creates new data source after invalidation
 *
 *  To show the loading indicator while the data source is retrieving data
 *  we need to expose the „latest loading status” of „the latest data source”
 *  that was created by the factory. The factory can expose data source via
 *  LiveData. The data source can expose status via  LiveData.
 *
 *  See: https://www.slideshare.net/GaborVaradi3/paging-like-a-pro
 */
public class PagedMoviesResult {

    private MutableLiveData<PagedMovieDataSource> pagedDataSource;
    private LiveData<PagedList<Movie>> pagedData;
    private LiveData<Resource> networkState;

    public PagedMoviesResult(MutableLiveData<PagedMovieDataSource> pagedDataSource,
                             LiveData<PagedList<Movie>> pagedData,
                             LiveData<Resource> networkState) {
        this.pagedDataSource = pagedDataSource;
        this.pagedData = pagedData;
        this.networkState = networkState;
    }

    public MutableLiveData<PagedMovieDataSource> getPagedDataSource() {
        return pagedDataSource;
    }

    public LiveData<PagedList<Movie>> getPagedData() {
        return pagedData;
    }

    public LiveData<Resource> getNetworkState() {
        return networkState;
    }
}
