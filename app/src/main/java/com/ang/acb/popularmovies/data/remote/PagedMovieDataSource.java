package com.ang.acb.popularmovies.data.remote;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.data.vo.MoviesResponse;
import com.ang.acb.popularmovies.data.vo.Resource;
import com.ang.acb.popularmovies.data.vo.MoviesFilter;

import org.jetbrains.annotations.NotNull;

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
 * See: https://www.youtube.com/watch?v=BE5bsyGGLf4
 * See: https://developer.android.com/topic/libraries/architecture/paging/data#custom-data-source
 * See: https://github.com/android/android-architecture-components/tree/master/PagingWithNetworkSample
 */
public class PagedMovieDataSource extends PageKeyedDataSource<Integer, Movie> {

    private static final int FIRST_PAGE_KEY = 1;

    private final ApiService apiService;
    private final MoviesFilter filter;
    private final Executor networkExecutor;

    private MutableLiveData<Resource> networkState = new MutableLiveData<>();
    private RetryCallback retryCallback = null;

    public interface RetryCallback {
        void retry();
    }

    @Inject
    PagedMovieDataSource(ApiService apiService,
                         MoviesFilter filter,
                         Executor networkExecutor) {
        this.apiService = apiService;
        this.filter = filter;
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
     * launched for the first time. We are fetching the first page of data from
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

        // Fetch the first page of data from the TMDB API.
        Call<MoviesResponse> request;
        if (filter == MoviesFilter.POPULAR) {
            request = apiService.getPopularMovies(FIRST_PAGE_KEY);
        } else if (filter == MoviesFilter.TOP_RATED){
            request = apiService.getTopRatedMovies(FIRST_PAGE_KEY);
        } else {
            request = apiService.getNowPlayingMovies(FIRST_PAGE_KEY);
        }

        final RetryCallback retryInitialLoad =
                () -> networkExecutor.execute(() -> loadInitial(params, callback));

        try {
            // Triggered by a refresh, we better execute sync.
            // Note: execute() invokes the request immediately and
            // blocks until the response can be processed or is in error.
            Response response = request.execute();
            if (response.isSuccessful()) {
                MoviesResponse moviesResponse = (MoviesResponse) response.body();
                List<Movie> movieList = moviesResponse != null ?
                        moviesResponse.getResults() : Collections.emptyList();

                // No need to retry data loading.
                retryCallback = null;

                // Send loading state to the UI.
                networkState.postValue(Resource.success(null));

                // Pass loaded data from the data source.
                callback.onResult(movieList, FIRST_PAGE_KEY, FIRST_PAGE_KEY + 1);
            } else {
                // Retry data loading.
                retryCallback = retryInitialLoad;

                // Publish error.
                networkState.postValue(Resource.error(
                        "Error code: " + response.code(), null));
            }

        } catch (IOException e) {
            // Retry data loading.
            retryCallback = retryInitialLoad;

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
     * @param params Parameters for the load, including the key for the new page,
     *               and requested load size.
     * @param callback Callback that receives loaded data.
     */
    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params,
                          @NonNull final LoadCallback<Integer, Movie> callback) {
        // Send loading state to the UI.
        networkState.postValue(Resource.loading(null));

        // Fetch the next page of data from the TMDB API.
        Call<MoviesResponse> request;
        if (filter == MoviesFilter.POPULAR) {
            request = apiService.getPopularMovies(params.key);
        } else if (filter == MoviesFilter.TOP_RATED) {
            request = apiService.getTopRatedMovies(params.key);
        } else {
            request = apiService.getNowPlayingMovies(params.key);
        }

        // Note: unlike execute(), enqueue() schedules the request
        // to be executed at some point in the future.
        request.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NotNull Call<MoviesResponse> call,
                                   @NotNull Response<MoviesResponse> response) {
                if (response.isSuccessful()) {
                    // Get the loaded page of data from the response.
                    MoviesResponse data = response.body();
                    List<Movie> movieList = data != null ?
                            data.getResults() : Collections.emptyList();

                    // No need to retry data loading.
                    retryCallback = null;

                    // Pass the loading state to the UI.
                    networkState.postValue(Resource.success(null));

                    // Pass the loaded page of data to the UI.
                    callback.onResult(movieList, params.key + 1);
                } else {
                    // Retry data loading.
                    retryCallback = () -> loadAfter(params, callback);

                    // Publish error.
                    networkState.postValue(Resource.error(
                            "Error code: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NotNull Call<MoviesResponse> call,
                                  @NotNull Throwable throwable) {
                // Retry data loading.
                retryCallback = () -> networkExecutor.execute(() -> loadAfter(params, callback));
                // Publish error.
                networkState.postValue(Resource.error(throwable.getMessage(), null));
            }
        });
    }
}
