package com.ang.acb.popularmovies.ui.movielist;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ang.acb.popularmovies.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Show popular movies fragment by default.
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            selectedFragment = TmdbMoviesFragment.newInstance(R.id.action_show_popular);
            transaction.replace(R.id.main_fragment_container, selectedFragment);
            transaction.commit();
        }

        // Setup toolbar.
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        // Setup bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.main_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_show_popular:
                        selectedFragment = TmdbMoviesFragment.newInstance(R.id.action_show_popular);
                        break;

                    case R.id.action_show_top_rated:
                        selectedFragment = TmdbMoviesFragment.newInstance(R.id.action_show_top_rated);
                        break;

                    case R.id.action_show_now_playing:
                        selectedFragment = TmdbMoviesFragment.newInstance(R.id.action_show_now_playing);
                        break;
                    case R.id.action_show_favorites:
                        selectedFragment = FavoriteMoviesFragment.newInstance();
                        break;
                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragment_container, selectedFragment);
                transaction.commit();

                return true;
            }
        });
    }
}
