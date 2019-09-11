package com.ang.acb.popularmovies.di;

import com.ang.acb.popularmovies.ui.movielist.FavoriteMoviesFragment;
import com.ang.acb.popularmovies.ui.movielist.TmdbMoviesFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * See: https://github.com/googlesamples/android-architecture-components/tree/GithubBrowserSample
 */
@Module
public abstract class FragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract TmdbMoviesFragment contributeTmdbMoviesFragment();

    @ContributesAndroidInjector()
    abstract FavoriteMoviesFragment contributeFavoriteMoviesFragment();
 }
