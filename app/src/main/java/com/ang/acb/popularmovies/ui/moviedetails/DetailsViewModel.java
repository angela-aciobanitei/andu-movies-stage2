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
 *
 * See: https://github.com/googlesamples/android-sunflower/blob/master/app/src/main/java/com/google/samples/apps/sunflower/viewmodels
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
        MovieDetails movieDetails = Objects.requireNonNull(movieDetailsLiveData.getValue()).data;
        assert movieDetails != null;
        if (!isFavorite) {
            movieRepository.markAsFavorite(movieDetails.movie);
            snackbarMessage.setValue(R.string.movie_added_to_favorites);
            isFavorite = true;
        } else {
            movieRepository.markAsNotFavorite(movieDetails.movie);
            snackbarMessage.setValue(R.string.movie_removed_from_favorites);
            isFavorite = false;
        }
    }
}
