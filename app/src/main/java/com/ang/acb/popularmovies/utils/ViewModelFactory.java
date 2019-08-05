package com.ang.acb.popularmovies.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ang.acb.popularmovies.data.repository.MovieRepository;
import com.ang.acb.popularmovies.ui.moviedetails.DetailsViewModel;
import com.ang.acb.popularmovies.ui.movielist.TmdbMoviesViewModel;

/**
 * A factory class for creating ViewModels with a constructor that takes a [MovieRepository].
 *
 * See: https://github.com/googlesamples/android-sunflower/blob/master/app/src/main/java/com/google/samples/apps/sunflower/viewmodels
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    // Each view model needs access to the repository that handles the local and remote data.
    private final MovieRepository repository;

    public static ViewModelFactory getInstance(MovieRepository repository) {
        return new ViewModelFactory(repository);
    }

    private ViewModelFactory(MovieRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TmdbMoviesViewModel.class)) {
            // noinspection unchecked
            return (T) new TmdbMoviesViewModel(repository);
        } else if (modelClass.isAssignableFrom(DetailsViewModel.class)) {
            // noinspection unchecked
            return (T) new DetailsViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
