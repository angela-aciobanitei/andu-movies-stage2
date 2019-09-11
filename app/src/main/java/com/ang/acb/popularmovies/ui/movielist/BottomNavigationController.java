package com.ang.acb.popularmovies.ui.movielist;

import androidx.fragment.app.FragmentManager;

import com.ang.acb.popularmovies.R;

import javax.inject.Inject;

/**
 * A utility class that handles navigation in {@link MainActivity}.
 */
public class BottomNavigationController {

    private final FragmentManager fragmentManager;
    private final int containerId;

    @Inject
    public BottomNavigationController(MainActivity mainActivity) {
        this.fragmentManager = mainActivity.getSupportFragmentManager();
        this.containerId = R.id.main_fragment_container;
    }

    public void navigateToPopularMovies() {
        fragmentManager.beginTransaction()
                .replace(containerId,
                        TmdbMoviesFragment.newInstance(R.id.action_show_popular))
                .commit();

    }

    public void navigateToTopRatedMovies() {
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container,
                        TmdbMoviesFragment.newInstance(R.id.action_show_top_rated))
                .commit();
    }

    public void navigateToNowPlayingMovies(){
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container,
                        TmdbMoviesFragment.newInstance(R.id.action_show_now_playing))
                .commit();
    }

    public void navigateToFavorites() {
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container,
                        FavoriteMoviesFragment.newInstance())
                .commit();
    }


}
