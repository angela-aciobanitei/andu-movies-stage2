package com.ang.acb.popularmovies.ui.moviedetails;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.data.vo.MovieDetails;
import com.ang.acb.popularmovies.data.vo.Resource;
import com.ang.acb.popularmovies.databinding.ActivityDetailsBinding;
import com.ang.acb.popularmovies.utils.Constants;
import com.ang.acb.popularmovies.utils.InjectorUtils;
import com.ang.acb.popularmovies.utils.ViewModelFactory;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;

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
        viewModel.getResult().observe(this, new Observer<Resource<MovieDetails>>() {
            @Override
            public void onChanged(Resource<MovieDetails> resource) {
                if (resource.data != null && resource.data.movie != null) {
                    viewModel.setFavorite(resource.data.movie.isFavorite());
                    invalidateOptionsMenu();
                }
                binding.setResource(resource);
                binding.setMovieDetails(resource.data);
            }
        });

        // Handle retry event in case of network failure.
        binding.networkState.retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.retry(movieId);
            }
        });

        // Observe Snackbar messages.
        viewModel.getSnackbarMessage().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer message) {
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            handleCollapsedToolbarTitle();
        }
    }

    private void handleCollapsedToolbarTitle() {
        // Sets the title on the toolbar only if the toolbar is collapsed.
        binding.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                // Verify if the toolbar is completely collapsed
                // and set the movie name as the title.
                if (scrollRange + verticalOffset == 0) {
                    binding.collapsingToolbar.setTitle(
                            viewModel.getResult().getValue().data.movie.getTitle());
                    isShow = true;
                } else if (isShow) {
                    // Display an empty string when toolbar is expanded.
                    binding.collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void setupCastAdapter() {
        RecyclerView rvCast = binding.contentPartialDetails.rvCast;
        rvCast.setLayoutManager(new LinearLayoutManager(
                this, RecyclerView.HORIZONTAL, false));
        rvCast.setAdapter(new CastAdapter());
        ViewCompat.setNestedScrollingEnabled(rvCast, false);
    }

    private void setupTrailersAdapter() {
        RecyclerView rvTrailers = binding.contentPartialDetails.rvTrailers;
        rvTrailers.setLayoutManager(new LinearLayoutManager(
                this, RecyclerView.HORIZONTAL, false));
        rvTrailers.setHasFixedSize(true);
        rvTrailers.setAdapter(new TrailersAdapter());
        ViewCompat.setNestedScrollingEnabled(rvTrailers, false);
    }

    private void setupReviewsAdapter() {
        RecyclerView listReviews = binding.contentPartialDetails.rvReviews;
        listReviews.setLayoutManager(new LinearLayoutManager(
                this, RecyclerView.VERTICAL, false));
        listReviews.setAdapter(new ReviewsAdapter());
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

        MenuItem favoriteItem = menu.findItem(R.id.action_favorite);
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
            case R.id.action_favorite: {
                viewModel.onFavoriteClicked();
                invalidateOptionsMenu();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareTrailer() {
        MovieDetails movieDetails = viewModel.getResult().getValue().data;
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
