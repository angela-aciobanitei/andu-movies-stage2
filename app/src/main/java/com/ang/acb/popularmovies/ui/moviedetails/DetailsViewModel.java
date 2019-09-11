package com.ang.acb.popularmovies.ui.moviedetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ang.acb.popularmovies.R;
import com.ang.acb.popularmovies.data.repository.MovieRepository;
import com.ang.acb.popularmovies.data.vo.MovieDetails;
import com.ang.acb.popularmovies.data.vo.Resource;
import com.ang.acb.popularmovies.utils.SnackbarMessage;

import java.util.Objects;

import timber.log.Timber;

/**
 * The ViewModel used in [DetailsActivity].
 * Stores and manages UI-related data in a lifecycle conscious way.
 *
 * See: https://medium.com/androiddevelopers/viewmodels-and-livedata-patterns-antipatterns-21efaef74a54
 * See: https://medium.com/androiddevelopers/livedata-beyond-the-viewmodel-reactive-patterns-using-transformations-and-mediatorlivedata-fda520ba00b7
 * See: https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150
 */
public class DetailsViewModel extends ViewModel {

    private final MovieRepository movieRepository;
    private LiveData<Resource<MovieDetails>> movieDetailsLiveData;
    private MutableLiveData<Long> movieIdLiveData = new MutableLiveData<>();

    private final SnackbarMessage snackbarMessage = new SnackbarMessage();
    private boolean isFavorite;

    public DetailsViewModel(final MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void init(long movieId) {
        // Load movie details only when the activity is created for the first time.
        if (movieDetailsLiveData != null) return;

        Timber.d("Initializing the movie details view model");
        movieDetailsLiveData = Transformations.switchMap(
                movieIdLiveData, movieRepository::loadAllMovieDetails);

        // Trigger movie loading.
        movieIdLiveData.setValue(movieId);
    }

    public void retry(long movieId) {
        movieIdLiveData.setValue(movieId);
    }

    public LiveData<Resource<MovieDetails>> getMovieDetailsLiveData() {
        return movieDetailsLiveData;
    }

    public SnackbarMessage getSnackbarMessage() {
        return snackbarMessage;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void onFavoriteClicked() {
        MovieDetails movieDetails = Objects.requireNonNull(
                movieDetailsLiveData.getValue()).data;
        if (!isFavorite) {
            movieRepository.markAsFavorite(Objects.requireNonNull(movieDetails).movie);
            snackbarMessage.setValue(R.string.movie_added_to_favorites);
            isFavorite = true;
        } else {
            movieRepository.markAsNotFavorite(Objects.requireNonNull(movieDetails).movie);
            snackbarMessage.setValue(R.string.movie_removed_from_favorites);
            isFavorite = false;
        }
    }
}
