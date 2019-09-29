package com.ang.acb.popularmovies.ui.movielist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.ang.acb.popularmovies.R;
import com.ang.acb.popularmovies.data.remote.PagedMoviesResult;
import com.ang.acb.popularmovies.data.repository.MovieRepository;
import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.data.vo.MoviesFilter;
import com.ang.acb.popularmovies.data.vo.Resource;

import javax.inject.Inject;

/**
 * The ViewModel used in {@link TmdbMoviesFragment}.
 * Stores and manages UI-related data in a lifecycle conscious way.
 *
 * See: https://medium.com/androiddevelopers/viewmodels-and-livedata-patterns-antipatterns-21efaef74a54
 * See: https://medium.com/androiddevelopers/livedata-beyond-the-viewmodel-reactive-patterns-using-transformations-and-mediatorlivedata-fda520ba00b7
 * See: https://github.com/googlesamples/android-architecture-components/tree/GithubBrowserSample/app/src/main/java/com/android/example/github/ui
 */
public class TmdbMoviesViewModel extends ViewModel {

    private LiveData<PagedMoviesResult> pagedResult;
    private LiveData<PagedList<Movie>> pagedData;
    private LiveData<Resource> networkState;
    private MutableLiveData<MoviesFilter> currentFilter = new MutableLiveData<>();
    private MutableLiveData<Integer> currentTitle = new MutableLiveData<>();

    @Inject
    public TmdbMoviesViewModel(final MovieRepository movieRepository) {
        // By default show popular movies.
        currentFilter.setValue(MoviesFilter.POPULAR);
        currentTitle.setValue(R.string.action_show_popular);

        pagedResult = Transformations.map(currentFilter, movieRepository::loadMoviesFilteredBy);
        pagedData = Transformations.switchMap(pagedResult,PagedMoviesResult::getPagedData);
        networkState = Transformations.switchMap(pagedResult, PagedMoviesResult::getNetworkState);
    }

    public LiveData<PagedList<Movie>> getPagedData() {
        return pagedData;
    }

    public LiveData<Resource> getNetworkState() {
        return networkState;
    }

    public LiveData<Integer> getCurrentTitle() {
        return currentTitle;
    }

    public MoviesFilter getCurrentFilter() {
        return currentFilter.getValue();
    }

    public void updateCurrentFilter(int actionId) {
        MoviesFilter filterType;
        int title;
        switch (actionId) {
            case R.id.action_show_popular: {
                // If already selected no need to request the API.
                if (getCurrentFilter() == MoviesFilter.POPULAR) return;
                filterType = MoviesFilter.POPULAR;
                title = R.string.action_show_popular;
                break;
            }
            case R.id.action_show_top_rated: {
                if (getCurrentFilter() == MoviesFilter.TOP_RATED) return;
                filterType = MoviesFilter.TOP_RATED;
                title = R.string.action_show_top_rated;
                break;
            }
            case R.id.action_show_now_playing: {
                if (getCurrentFilter() == MoviesFilter.NOW_PLAYING) return;
                filterType = MoviesFilter.NOW_PLAYING;
                title = R.string.action_show_now_playing;
                break;
            }

            default:
                throw new IllegalArgumentException("Unknown action id");
        }
        currentFilter.setValue(filterType);
        currentTitle.setValue(title);
    }

    // Retry any failed requests.
    public void retry() {
        pagedResult.getValue()
                .getPagedDataSource().getValue()
                .getRetryCallback().retry();
    }
}
