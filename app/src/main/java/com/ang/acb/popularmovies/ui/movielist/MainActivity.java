package com.ang.acb.popularmovies.ui.movielist;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.ang.acb.popularmovies.databinding.ActivityMainBinding;

import androidx.fragment.app.Fragment;

import com.ang.acb.popularmovies.R;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;


public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    private ActivityMainBinding binding;

    @Inject
    BottomNavigationController navigationController;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        // Note: a DispatchingAndroidInjector<T> performs members-injection
        // on instances of core Android types (e.g. Activity, Fragment) that
        // are constructed by the Android framework and not by Dagger.
        return dispatchingAndroidInjector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Note: when using Dagger for injecting Activity
        // objects, inject as early as possible.
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);

        // Show popular movies fragment by default.
        if (savedInstanceState == null) {
            navigationController.navigateToPopularMovies();
        }

        // Setup toolbar
        setSupportActionBar(binding.mainToolbar);

        setupBottomNavigationView();
    }

    private void setupBottomNavigationView() {
        binding.mainBottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_show_popular:
                    navigationController.navigateToPopularMovies();
                    return true;

                case R.id.action_show_top_rated:
                    navigationController.navigateToTopRatedMovies();
                    return true;

                case R.id.action_show_now_playing:
                    navigationController.navigateToNowPlayingMovies();
                    return true;

                case R.id.action_show_favorites:
                    navigationController.navigateToFavorites();
                    return true;
            }

            return false;
        });
    }
}
