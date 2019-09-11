package com.ang.acb.popularmovies.ui.moviedetails;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ang.acb.popularmovies.R;
import com.ang.acb.popularmovies.data.vo.MovieDetails;
import com.ang.acb.popularmovies.databinding.FragmentMovieDetailsBinding;
import com.ang.acb.popularmovies.ui.movielist.MainActivity;
import com.ang.acb.popularmovies.utils.InjectorUtils;
import com.ang.acb.popularmovies.utils.ViewModelFactory;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MovieDetailsFragment extends Fragment {

    private static final String ARG_MOVIE_ID = "ARG_MOVIE_ID";

    private FragmentMovieDetailsBinding binding;
    private DetailsViewModel viewModel;
    private long movieId;

    // Required empty public constructor
    public MovieDetailsFragment() {}

    public static MovieDetailsFragment newInstance(long movieId) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_MOVIE_ID, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieId = getArguments().getLong(ARG_MOVIE_ID);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupToolbar();
        setupViewModel();
        setupTrailersAdapter();
        setupCastAdapter();
        setupReviewsAdapter();
        observeResult();
    }

    private void setupToolbar() {
        getHostActivity().setSupportActionBar(binding.toolbar);
        if (getHostActivity().getSupportActionBar() != null) {
            // Handle Up navigation
            getHostActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setToolbarTitleIfCollapsed();
        }
    }

    private void setToolbarTitleIfCollapsed() {
        // To set the title on the toolbar only when the toolbar is collapsed,
        // we need to add an OnOffsetChangedListener to AppBarLayout to determine
        // when CollapsingToolbarLayout is collapsed or expanded.
        // See: https://stackoverflow.com/questions/31662416/show-collapsingtoolbarlayout-title-only-when-collapsed
        // See: https://medium.com/@nullthemall/the-power-of-appbarlayout-offset-ecbf8eaa6b5f
        binding.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShown = true;
            int totalScrollRange = -1;

            // This listener is triggered when vertical offset is changed,
            // i.e bottom and top are offset. Parameter "verticalOffset" is
            // always between 0 and appBarLayout.getTotalScrollRange(),
            // which is the total height of all children that can scroll.
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (totalScrollRange == -1) totalScrollRange = appBarLayout.getTotalScrollRange();
                // If toolbar is completely collapsed, set the collapsing bar title.
                if (totalScrollRange + verticalOffset == 0) {
                    MovieDetails movieDetails = Objects.requireNonNull(
                            viewModel.getMovieDetailsLiveData().getValue()).getData();
                    binding.collapsingToolbar.setTitle(Objects.requireNonNull(movieDetails)
                            .movie.getTitle());
                    isShown = true;
                } else if (isShown) {
                    // When toolbar is expanded, display an empty string.
                    binding.collapsingToolbar.setTitle(" ");
                    isShown = false;
                }
            }
        });
    }

    private void setupViewModel(){
        ViewModelFactory factory = InjectorUtils.provideViewModelFactory(getHostActivity());
        viewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel.class);
        viewModel.init(movieId);
    }

    private void setupCastAdapter() {
        RecyclerView rvCast = binding.contentPartialDetails.rvCast;
        rvCast.setLayoutManager(new LinearLayoutManager(
                getHostActivity(), RecyclerView.HORIZONTAL, false));
        rvCast.setAdapter(new CastAdapter());
        // Disable nested scrolling for this view.
        ViewCompat.setNestedScrollingEnabled(rvCast, false);
    }

    private void setupTrailersAdapter() {
        RecyclerView rvTrailers = binding.contentPartialDetails.rvTrailers;
        rvTrailers.setLayoutManager(new LinearLayoutManager(
                getHostActivity(), RecyclerView.HORIZONTAL, false));
        rvTrailers.setHasFixedSize(true);
        rvTrailers.setAdapter(new TrailersAdapter());
        // Disable nested scrolling for this view.
        ViewCompat.setNestedScrollingEnabled(rvTrailers, false);
    }

    private void setupReviewsAdapter() {
        RecyclerView listReviews = binding.contentPartialDetails.rvReviews;
        listReviews.setLayoutManager(new LinearLayoutManager(
                getHostActivity(), RecyclerView.VERTICAL, false));
        listReviews.setAdapter(new ReviewsAdapter());
        // Disable nested scrolling for this view.
        ViewCompat.setNestedScrollingEnabled(listReviews, false);
    }

    private void observeResult() {
        viewModel.getMovieDetailsLiveData().observe(getViewLifecycleOwner(), resource -> {
            if (resource.data != null && resource.data.movie != null) {
                // Handle adding/removing movie from favorites.
                viewModel.setFavorite(resource.data.movie.isFavorite());
                getHostActivity().invalidateOptionsMenu();
            }
            binding.setResource(resource);
            binding.setMovieDetails(resource.data);
        });

        // Handle retry event in case of network failure.
        binding.networkState.retryButton.setOnClickListener(view -> viewModel.retry(movieId));

        // Observe the Snackbar messages showed when adding/removing movie from favorites.
        viewModel.getSnackbarMessage().observe(this, (Observer<Integer>) message ->
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show());
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_details, menu);

        MenuItem favoriteItem = menu.findItem(R.id.action_add_remove_favorite);
        if (viewModel.isFavorite()) {
            favoriteItem.setIcon(R.drawable.ic_favorite_black_24dp)
                        .setTitle(R.string.action_remove_from_favorites);
        } else {
            favoriteItem.setIcon(R.drawable.ic_favorite_border_black_24dp)
                        .setTitle(R.string.action_add_to_favorites);
        }
        tintMenuIcon(getHostActivity(), favoriteItem);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void tintMenuIcon(Context context, MenuItem item) {
        // Tints menu item icons
        // See: https://stackoverflow.com/questions/26780046/menuitem-tinting-on-appcompat-toolbar
        Drawable iconWrapper = DrawableCompat.wrap(item.getIcon());
        DrawableCompat.setTint(iconWrapper, context.getResources().getColor(android.R.color.white));
        item.setIcon(iconWrapper);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_remove_favorite) {
            viewModel.onFavoriteClicked();
            getHostActivity().invalidateOptionsMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private MainActivity getHostActivity() {
        return (MainActivity) getActivity();
    }

}
