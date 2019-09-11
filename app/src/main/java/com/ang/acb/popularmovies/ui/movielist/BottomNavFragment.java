package com.ang.acb.popularmovies.ui.movielist;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ang.acb.popularmovies.R;
import com.ang.acb.popularmovies.databinding.FragmentBottomNavBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class BottomNavFragment extends Fragment {

    private FragmentBottomNavBinding binding;
    private Fragment selectedFragment;

    // Required empty public constructor
    public BottomNavFragment() {}


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBottomNavBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Show popular movies fragment by default.
        if (savedInstanceState == null) {
            selectedFragment = TmdbMoviesFragment.newInstance(R.id.action_show_popular);
            getHostActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment_container, selectedFragment)
                    .commit();
        }

        // Setup toolbar.
        getHostActivity().setSupportActionBar(binding.mainToolbar);

        // Setup bottom navigation bar.
        binding.mainBottomNavigation.setOnNavigationItemSelectedListener(item -> {
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

            getHostActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment_container, selectedFragment)
                    .commit();

            return true;
        });
    }

    private MainActivity getHostActivity() {
        return (MainActivity)getActivity();
    }
}
