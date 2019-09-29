package com.ang.acb.popularmovies.ui.movielist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ang.acb.popularmovies.data.repository.MovieRepository;
import com.ang.acb.popularmovies.data.vo.Movie;

import java.util.List;

import javax.inject.Inject;

/**
 * The ViewModel used in {@link FavoriteMoviesFragment}.
 * Stores and manages UI-related data in a lifecycle conscious way.
 *
 * See: https://github.com/googlesamples/android-sunflower
 * See: https://medium.com/androiddevelopers/viewmodels-and-livedata-patterns-antipatterns-21efaef74a54
 */
public class FavoriteMoviesViewModel extends ViewModel {

    private MovieRepository repository;
    private LiveData<List<Movie>> favoriteListLiveData;

    @Inject
    FavoriteMoviesViewModel(MovieRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Movie>> getFavoriteListLiveData() {
        if (favoriteListLiveData == null) {
            favoriteListLiveData = repository.getAllFavoriteMovies();
        }
        return favoriteListLiveData;
    }
}

