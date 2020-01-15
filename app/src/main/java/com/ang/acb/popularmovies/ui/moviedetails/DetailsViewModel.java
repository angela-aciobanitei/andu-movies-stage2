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

import javax.inject.Inject;

/**
 * The ViewModel used in {@link DetailsActivity}.
 * Stores and manages UI-related data in a lifecycle conscious way.
 *
 * See: https://medium.com/androiddevelopers/viewmodels-and-livedata-patterns-antipatterns-21efaef74a54
 * See: https://medium.com/androiddevelopers/livedata-beyond-the-viewmodel-reactive-patterns-using-transformations-and-mediatorlivedata-fda520ba00b7
 * See: https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150
 */
public class DetailsViewModel extends ViewModel {

    private final MovieRepository movieRepository;
    private LiveData<Resource<MovieDetails>> liveMovieDetails;
    private MutableLiveData<Long> liveMovieId = new MutableLiveData<>();
    private final SnackbarMessage snackbarMessage = new SnackbarMessage();
    private boolean isFavorite;

    @Inject
    DetailsViewModel(final MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void init(long movieId) {
        liveMovieId.setValue(movieId);
    }

    public void retry(long movieId) {
        liveMovieId.setValue(movieId);
    }

    public LiveData<Resource<MovieDetails>> getLiveMovieDetails() {
        if (liveMovieDetails == null) {
            liveMovieDetails = Transformations.switchMap(
                    liveMovieId, movieRepository::loadAllMovieDetails);
        }
        return liveMovieDetails;
    }

    public SnackbarMessage getSnackbarMessage() {
        return snackbarMessage;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public void onFavoriteClicked() {
        MovieDetails movieDetails = Objects.requireNonNull(liveMovieDetails.getValue()).data;
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
