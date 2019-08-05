package com.ang.acb.popularmovies.ui.moviedetails;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ang.acb.popularmovies.data.repository.MovieRepository;
import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.data.vo.MovieDetails;
import com.ang.acb.popularmovies.data.vo.Resource;

import timber.log.Timber;

/**
 * The ViewModel used in [DetailsActivity].
 *
 * See: https://github.com/googlesamples/android-sunflower/blob/master/app/src/main/java/com/google/samples/apps/sunflower/viewmodels
 */
public class DetailsViewModel extends ViewModel {

    private final MovieRepository repository;
    private LiveData<Resource<MovieDetails>> result;
    private MutableLiveData<Long> movieIdLiveData = new MutableLiveData<>();

    public DetailsViewModel(final MovieRepository repository) {
        this.repository = repository;
    }

    public void init(long movieId) {
        // Load movie details only when the activity is created for the first time.
        if (result != null) return;
        Timber.d("Initializing details view model");

        result = Transformations.switchMap(
                movieIdLiveData,
                new Function<Long, LiveData<Resource<MovieDetails>>>() {
                    @Override
                    public LiveData<Resource<MovieDetails>> apply(Long movieId) {
                        return repository.loadMovie(movieId);
                    }
                });

        // Trigger loading movie
        movieIdLiveData.setValue(movieId);
    }

    public LiveData<Resource<MovieDetails>> getResult() {
        return result;
    }

    public void retry(long movieId) {
        movieIdLiveData.setValue(movieId);
    }

}
