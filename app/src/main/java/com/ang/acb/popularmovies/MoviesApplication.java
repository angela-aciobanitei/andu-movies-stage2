package com.ang.acb.popularmovies;

import android.app.Application;

import timber.log.Timber;

/**
 * Timber is a logger that provides utility on top of Android's normal Log class.
 * The behavior is added through Tree instances. You can install an instance by
 * calling Timber.plant(). Installation of Trees should be done as early as possible.
 * The onCreate() of your application is the most logical choice.
 *
 * See: https://github.com/JakeWharton/timber
 */
public class MoviesApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
