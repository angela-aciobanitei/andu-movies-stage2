package com.ang.acb.popularmovies.data.remote;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.data.vo.MoviesResponse;
import com.ang.acb.popularmovies.data.vo.Resource;
import com.ang.acb.popularmovies.data.vo.MoviesFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A custom data source that uses the before/after keys returned in page requests.
 *
 * See: https://developer.android.com/topic/libraries/architecture/paging/data#custom-data-source
 * See: https://github.com/googlesamples/android-architecture-components/tree/master/PagingWithNetworkSample
 */
public class PagedMovieDataSource extends PageKeyedDataSource<Integer, Movie> {

    private static final int FIRST_PAGE_KEY = 1;

    private final ApiService movieService;
    private final MoviesFilter sortBy;
    private final Executor networkExecutor;

    private MutableLiveData<Resource> networkState = new MutableLiveData<>();
    private RetryCallback retryCallback = null;

    public interface RetryCallback {
        void retry();
    }

    @Inject
    public PagedMovieDataSource(ApiService movieService,
                                MoviesFilter sortBy,
                                Executor networkExecutor) {
        this.movieService = movieService;
        this.sortBy = sortBy;
        this.networkExecutor = networkExecutor;
    }

    public RetryCallback getRetryCallback() {
        return retryCallback;
    }

    public MutableLiveData<Resource> getNetworkState() {
        return networkState;
    }

    /**
     * This method is responsible for initially loading the data when app is
     * launched for the first time. We are fetching the first page data from
     * the API and passing it via the callback method to the UI.
     *
     * @param params Parameters for initial load, including requested load size.
     * @param callback Callback that receives initial load data.
     */
    @Override
    public void loadInitial(@NonNull final LoadInitialParams<Integer> params,
                            @NonNull final LoadInitialCallback<Integer, Movie> callback) {

        // Send loading state to the UI.
        networkState.postValue(Resource.loading(null));

        // Fetch the first page data from the API.
        Call<MoviesResponse> request;
        if (sortBy == MoviesFilter.POPULAR) {
            request = movieService.getPopularMovies(FIRST_PAGE_KEY);
        } else if (sortBy == MoviesFilter.TOP_RATED){
            request = movieService.getTopRatedMovies(FIRST_PAGE_KEY);
        } else {
            request = movieService.getNowPlayingMovies(FIRST_PAGE_KEY);
        }

        try {
            // Triggered by a refresh, we better execute sync.
            // Note: execute() invokes the request immediately and
            // blocks until the response can be processed or is in error.
            MoviesResponse response = request.execute().body();
            List<Movie> movieList = response != null ? response.getResults() : Collections.emptyList();
            // No need to retry data loading.
            retryCallback = null;
            // Send loading state to the UI.
            networkState.postValue(Resource.success(null));
            // Pass loaded data from the data source.
            callback.onResult(movieList, FIRST_PAGE_KEY, FIRST_PAGE_KEY + 1);
        } catch (IOException e) {
            // Retry data loading.
            retryCallback = () -> networkExecutor.execute(() -> loadInitial(params, callback));
            // Publish error.
            networkState.postValue(Resource.error(e.getMessage(), null));
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params,
                           @NonNull LoadCallback<Integer, Movie> callback) {
        // Ignored, since we only ever append to our initial load.
    }

    /**
     * This method it is responsible for the subsequent call to load the data
     * page wise and is executed in the background thread. We are fetching the
     * next page data from the API and passing it via the callback method to the UI.
     *
     * @param params Parameters for the load, including the key for the new page, and requested load size.
     * @param callback Callback that receives loaded data.
     */
    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params,
                          @NonNull final LoadCallback<Integer, Movie> callback) {
        // Send loading state to the UI.
        networkState.postValue(Resource.loading(null));

        // Fetch the next page data from the API.
        Call<MoviesResponse> request;
        if (sortBy == MoviesFilter.POPULAR) {
            request = movieService.getPopularMovies(params.key);
        } else if (sortBy == MoviesFilter.TOP_RATED) {
            request = movieService.getTopRatedMovies(params.key);
        } else {
            request = movieService.getNowPlayingMovies(params.key);
        }

        // Note: unlike execute(), enqueue() schedules the request
        // to be executed at some point in the future.
        request.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()) {
                    MoviesResponse data = response.body();
                    List<Movie> movieList = data != null ? data.getResults() : Collections.emptyList();
                    // No need to retry data loading.
                    retryCallback = null;
                    // Pass the loading state to the UI.
                    networkState.postValue(Resource.success(null));
                    // Pass the page data to the UI.
                    callback.onResult(movieList, params.key + 1);
                } else {
                    // Retry data loading.
                    retryCallback = () -> loadAfter(params, callback);
                    // Publish error.
                    networkState.postValue(Resource.error(
                            "error code: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable throwable) {
                // Retry data loading.
                retryCallback = () -> networkExecutor.execute(() -> loadAfter(params, callback));
                // Publish error.
                networkState.postValue(Resource.error(
                        throwable != null ? throwable.getMessage() : "Unknown error", null));
            }
        });
    }
}
