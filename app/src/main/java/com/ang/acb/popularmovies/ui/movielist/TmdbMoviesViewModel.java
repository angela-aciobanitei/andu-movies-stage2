package com.ang.acb.popularmovies.ui.movielist;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.ang.acb.popularmovies.R;
import com.ang.acb.popularmovies.data.remote.PagedMoviesResult;
import com.ang.acb.popularmovies.data.repository.MovieRepository;
import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.data.vo.Resource;

/**
 * The ViewModel used in [TmdbMoviesFragment].
 *
 * See: https://github.com/googlesamples/android-sunflower/blob/master/app/src/main/java/com/google/samples/apps/sunflower/viewmodels
 */
public class TmdbMoviesViewModel extends ViewModel {

    private LiveData<PagedMoviesResult> pagedMoviesResult;
    private LiveData<PagedList<Movie>> pagedListData;
    private LiveData<Resource> networkState;
    private MutableLiveData<Integer> currentTitle = new MutableLiveData<>();
    private MutableLiveData<MoviesFilter> sortBy = new MutableLiveData<>();

    public TmdbMoviesViewModel(final MovieRepository movieRepository) {
        // By default show popular movies.
        sortBy.setValue(MoviesFilter.POPULAR);
        currentTitle.setValue(R.string.action_popular);

        pagedMoviesResult = Transformations.map(
                sortBy,
                new Function<MoviesFilter, PagedMoviesResult>() {
                    @Override
                    public PagedMoviesResult apply(MoviesFilter sort) {
                        return movieRepository.loadMoviesFilteredBy(sort);
                    }
                });

        pagedListData = Transformations.switchMap(
                pagedMoviesResult,
                new Function<PagedMoviesResult, LiveData<PagedList<Movie>>>() {
                    @Override
                    public LiveData<PagedList<Movie>> apply(PagedMoviesResult result) {
                        return result.getData();
                    }
                });

        networkState = Transformations.switchMap(
                pagedMoviesResult,
                new Function<PagedMoviesResult, LiveData<Resource>>() {
                    @Override
                    public LiveData<Resource> apply(PagedMoviesResult result) {
                        return result.getResource();
                    }
                });
    }

    public LiveData<PagedList<Movie>> getPagedListData() {
        return pagedListData;
    }

    public LiveData<Resource> getNetworkState() {
        return networkState;
    }

    public MoviesFilter getCurrentSorting() {
        return sortBy.getValue();
    }

    public LiveData<Integer> getCurrentTitle() {
        return currentTitle;
    }

    public void setSortMoviesBy(int id) {
        MoviesFilter filterType = null;
        Integer title = null;
        switch (id) {
            case R.id.action_popular: {
                // If already selected no need to request the API.
                if (sortBy.getValue() == MoviesFilter.POPULAR) return;
                filterType = MoviesFilter.POPULAR;
                title = R.string.action_popular;
                break;
            }
            case R.id.action_top_rated: {
                if (sortBy.getValue() == MoviesFilter.TOP_RATED) return;
                filterType = MoviesFilter.TOP_RATED;
                title = R.string.action_top_rated;
                break;
            }
            case R.id.action_now_playing: {
                if (sortBy.getValue() == MoviesFilter.NOW_PLAYING) return;
                filterType = MoviesFilter.NOW_PLAYING;
                title = R.string.action_now_playing;
                break;
            }

            default:
                throw new IllegalArgumentException("unknown sorting id");
        }
        sortBy.setValue(filterType);
        currentTitle.setValue(title);
    }

    // Retry any failed requests.
    public void retry() {
        pagedMoviesResult.getValue()
                .getSourceLiveData().getValue()
                .getRetryCallback().invoke();
    }
}
