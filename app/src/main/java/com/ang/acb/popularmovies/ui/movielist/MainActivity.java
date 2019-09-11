package com.ang.acb.popularmovies.ui.movielist;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.ang.acb.popularmovies.databinding.ActivityMainBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ang.acb.popularmovies.R;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private BottomNavigationController navigationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);

        // Init nav controller
        navigationController = new BottomNavigationController(this);

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
