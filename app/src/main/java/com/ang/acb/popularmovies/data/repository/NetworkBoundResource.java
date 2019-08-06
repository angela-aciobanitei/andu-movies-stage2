package com.ang.acb.popularmovies.data.repository;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.ang.acb.popularmovies.data.remote.ApiResponse;
import com.ang.acb.popularmovies.data.vo.Resource;
import com.ang.acb.popularmovies.utils.AppExecutors;


/**
 * A generic class that can provide a resource backed by both the SQLite database and
 * the network. It defines two type parameters, ResultType and RequestType, because the
 * data type returned from the API might not match the data type used locally.
 *
 * See: https://developer.android.com/jetpack/docs/guide#addendum.
 * See: https://github.com/googlesamples/android-architecture-components/tree/master/GithubBrowserSample
 *
 * @param <ResultType> Type for the Resource data.
 * @param <RequestType> Type for the API response.
 */
public abstract class NetworkBoundResource<ResultType, RequestType> {

    // The final result LiveData
    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();
    private AppExecutors appExecutors;

    @MainThread
    public NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        // Send loading state to the UI.
        result.setValue(Resource.<ResultType>loading(null));
        // Get the cached data from the database.
        final LiveData<ResultType> dbSource = loadFromDb();
        // Start observing the database for the resource.
        result.addSource(dbSource, new Observer<ResultType>() {
            @Override
            public void onChanged(ResultType data) {
                result.removeSource(dbSource);
                // Decide whether to fetch potentially updated data from the network.
                // Note: only fetch fresh data if it doesn't exist in database.
                if (shouldFetch(data)) {
                    fetchFromNetwork(dbSource);
                } else {
                    result.addSource(dbSource, new Observer<ResultType>() {
                        @Override
                        public void onChanged(ResultType newData) {
                            setValue(Resource.success(newData));
                        }
                    });
                }
            }
        });
    }

    @MainThread
    private void setValue(Resource<ResultType> newValue) {
        if (result.getValue() != newValue) {
            result.setValue(newValue);
        }
    }

    /**
     * Fetch the data from network and persist into DB and then send it back to UI.
     */
    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {
        // Create the API call to load data from themoviedb.org.
        // Note the use of the ApiResponse class - a simple wrapper around the
        // Retrofit2.Call class that converts responses to instances of LiveData.
        final LiveData<ApiResponse<RequestType>> apiResponse = createCall();

        // Re-attach dbSource as a new source, it will dispatch its latest value quickly.
        result.addSource(dbSource, new Observer<ResultType>() {
            @Override
            public void onChanged(ResultType newData) {
                setValue(Resource.loading(newData));
            }
        });

        // Start observing the API response.
        result.addSource(apiResponse, new Observer<ApiResponse<RequestType>>() {
            @Override
            public void onChanged(final ApiResponse<RequestType> response) {
                result.removeSource(apiResponse);
                result.removeSource(dbSource);
                // If the network call completes successfully, save the response
                // into the database and re-initialize the stream.
                if (response.isSuccessful() && response.body != null) {
                    appExecutors.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            saveCallResult(response.body);
                            appExecutors.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    // We specially request new live data, otherwise we will
                                    // get the immediately last cached value, which may not
                                    // be updated with latest results received from network.
                                    result.addSource(loadFromDb(), new Observer<ResultType>() {
                                        @Override
                                        public void onChanged(ResultType newData) {
                                            setValue(Resource.success(newData));
                                        }
                                    });
                                }
                            });
                        }
                    });
                // If the response is empty, reload from disk whatever we had.
                } else if(response.isSuccessful() && response.body == null) {
                    appExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            result.addSource(loadFromDb(), new Observer<ResultType>() {
                                @Override
                                public void onChanged(ResultType newData) {
                                    setValue(Resource.success(newData));
                                }
                            });
                        }
                    });
                // If network request fails, dispatch a failure directly.
                } else {
                    onFetchFailed();
                    result.addSource(dbSource, new Observer<ResultType>() {
                        @Override
                        public void onChanged(ResultType newData) {
                            setValue(Resource.error(response.getError().getMessage(), newData));
                        }
                    });
                }
            }
        });
    }

    // Called to create the API call.
    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> createCall();

    // Called to save the result of the API response into the database.
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);

    // Called when the fetch fails. The child class may want to reset
    // components like rate limiter.
    @NonNull
    @MainThread
    protected abstract void onFetchFailed();

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);

    // Called to get the cached data from the database.
    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    // Returns a LiveData object that represents the resource that's
    // implemented in the base class.
    public final LiveData<Resource<ResultType>> getAsLiveData() {
        return result;
    }
}

