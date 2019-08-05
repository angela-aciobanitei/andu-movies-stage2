package com.ang.acb.popularmovies.ui.movielist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ang.acb.popularmovies.data.repository.MovieRepository;
import com.ang.acb.popularmovies.data.vo.Movie;

import java.util.List;

public class FavoriteMoviesViewModel extends ViewModel {

    private LiveData<List<Movie>> favoriteListLiveData;

    public FavoriteMoviesViewModel(MovieRepository repository) {
        favoriteListLiveData = repository.getAllFavoriteMovies();
    }

    public LiveData<List<Movie>> getFavoriteListLiveData() {
        return favoriteListLiveData;
    }
}

