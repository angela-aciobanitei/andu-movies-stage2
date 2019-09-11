package com.ang.acb.popularmovies.di;

import com.ang.acb.popularmovies.ui.moviedetails.DetailsActivity;
import com.ang.acb.popularmovies.ui.movielist.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BindingModule {

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector()
    abstract DetailsActivity contributeDetailsActivity();
}
