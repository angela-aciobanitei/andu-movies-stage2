package com.ang.acb.popularmovies.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ang.acb.popularmovies.ui.moviedetails.DetailsViewModel;
import com.ang.acb.popularmovies.ui.movielist.FavoriteMoviesViewModel;
import com.ang.acb.popularmovies.ui.movielist.TmdbMoviesViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(TmdbMoviesViewModel.class)
    abstract ViewModel bindTmdbMoviesViewModel(TmdbMoviesViewModel tmdbMoviesViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteMoviesViewModel.class)
    abstract ViewModel bindFavoriteMoviesViewModel(FavoriteMoviesViewModel favoriteMoviesViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel.class)
    abstract ViewModel bindRecipeDetailsViewModel(DetailsViewModel detailsViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}

