package com.ang.acb.popularmovies.ui.moviedetails;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.popularmovies.R;
import com.ang.acb.popularmovies.data.vo.MovieDetails;
import com.ang.acb.popularmovies.databinding.ActivityDetailsBinding;
import com.ang.acb.popularmovies.utils.Constants;
import com.ang.acb.popularmovies.utils.InjectorUtils;
import com.ang.acb.popularmovies.utils.ViewModelFactory;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_ID = "extra_movie_id";
    private static final int INVALID_MOVIE_ID = -1;

    private ActivityDetailsBinding binding;
    private DetailsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final long movieId = getIntent().getLongExtra(EXTRA_MOVIE_ID, INVALID_MOVIE_ID);
        if (movieId == INVALID_MOVIE_ID) {
            closeOnError();
            return;
        }

        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);

        // Setup view model.
        ViewModelFactory factory = InjectorUtils.provideViewModelFactory(this);
        viewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel.class);
        viewModel.init(movieId);

        setupToolbar();

        setupTrailersAdapter();
        setupCastAdapter();
        setupReviewsAdapter();

        // Observe result.
        viewModel.getMovieDetailsLiveData().observe(this, resource -> {
            if (resource.data != null && resource.data.movie != null) {
                // Handle adding/removing movie from favorites.
                viewModel.setFavorite(resource.data.movie.isFavorite());
                invalidateOptionsMenu();
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

    private void setupToolbar() {
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            // Handle Up navigation
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setToolbarTitleIfCollapsed();
        }
    }

    // Sets the title on the toolbar only when the toolbar is collapsed.
    private void setToolbarTitleIfCollapsed() {
        // Add an OnOffsetChangedListener to AppBarLayout to determine
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
                // If toolbar is completely collapsed, set the movie name as the title.
                if (totalScrollRange + verticalOffset == 0) {
                    MovieDetails movieDetails = Objects.requireNonNull(
                            viewModel.getMovieDetailsLiveData().getValue()).getData();
                    binding.collapsingToolbar.setTitle(movieDetails.movie.getTitle());
                    isShown = true;
                } else if (isShown) {
                    // When toolbar is expanded, display an empty string.
                    binding.collapsingToolbar.setTitle(" ");
                    isShown = false;
                }
            }
        });
    }

    private void setupCastAdapter() {
        RecyclerView rvCast = binding.contentPartialDetails.rvCast;
        rvCast.setLayoutManager(new LinearLayoutManager(
                this, RecyclerView.HORIZONTAL, false));
        rvCast.setAdapter(new CastAdapter());
        // Note: remember to enable nested scrolling for this view.
        ViewCompat.setNestedScrollingEnabled(rvCast, false);
    }

    private void setupTrailersAdapter() {
        RecyclerView rvTrailers = binding.contentPartialDetails.rvTrailers;
        rvTrailers.setLayoutManager(new LinearLayoutManager(
                this, RecyclerView.HORIZONTAL, false));
        rvTrailers.setHasFixedSize(true);
        rvTrailers.setAdapter(new TrailersAdapter());
        // Note: remember to enable nested scrolling for this view.
        ViewCompat.setNestedScrollingEnabled(rvTrailers, false);
    }

    private void setupReviewsAdapter() {
        RecyclerView listReviews = binding.contentPartialDetails.rvReviews;
        listReviews.setLayoutManager(new LinearLayoutManager(
                this, RecyclerView.VERTICAL, false));
        listReviews.setAdapter(new ReviewsAdapter());
        // Note: remember to enable nested scrolling for this view.
        ViewCompat.setNestedScrollingEnabled(listReviews, false);
    }

    // Tints menu item icons
    // See: https://stackoverflow.com/questions/26780046/menuitem-tinting-on-appcompat-toolbar
    public void tintMenuIcon(Context context, MenuItem item, @ColorRes int color) {
        Drawable iconWrapper = DrawableCompat.wrap(item.getIcon());
        DrawableCompat.setTint(iconWrapper, context.getResources().getColor(color));
        item.setIcon(iconWrapper);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_details, menu);
        tintMenuIcon(this, menu.findItem(R.id.action_share), android.R.color.white);

        MenuItem favoriteItem = menu.findItem(R.id.action_add_remove_favorite);
        if (viewModel.isFavorite()) {
            favoriteItem.setIcon(R.drawable.ic_favorite_black_24dp)
                    .setTitle(R.string.action_remove_from_favorites);
        } else {
            favoriteItem.setIcon(R.drawable.ic_favorite_border_black_24dp)
                    .setTitle(R.string.action_add_to_favorites);
        }
        tintMenuIcon(this, favoriteItem, android.R.color.white);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share: {
                shareTrailer();
                return true;
            }
            case R.id.action_add_remove_favorite: {
                viewModel.onFavoriteClicked();
                invalidateOptionsMenu();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareTrailer() {
        MovieDetails movieDetails = Objects.requireNonNull(
                viewModel.getMovieDetailsLiveData().getValue()).getData();
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setSubject(movieDetails.movie.getTitle() + " movie trailer")
                .setText("Check out " + movieDetails.movie.getTitle() + " movie trailer at " +
                        Uri.parse(Constants.YOUTUBE_WEB_BASE_URL + movieDetails.trailers.get(0).getKey()))
                .createChooserIntent();

        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;

        shareIntent.addFlags(flags);
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(shareIntent);
        }
    }

    private void closeOnError() {
        throw new IllegalArgumentException("Access denied.");
    }

}
