package com.ang.acb.popularmovies.ui.common;

import androidx.fragment.app.FragmentManager;

import com.ang.acb.popularmovies.R;
import com.ang.acb.popularmovies.ui.movielist.MainActivity;

/**
 * A utility class that handles navigation in {@link MainActivity}.
 */
public class NavigationController {
    private final int containerId;
    private final FragmentManager fragmentManager;

    public NavigationController(MainActivity mainActivity) {
        this.containerId = R.id.container;
        this.fragmentManager = mainActivity.getSupportFragmentManager();
    }
}
