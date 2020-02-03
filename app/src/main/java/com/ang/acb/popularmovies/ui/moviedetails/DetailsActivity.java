package com.ang.acb.popularmovies.ui.moviedetails;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.popularmovies.R;
import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.databinding.ActivityDetailsBinding;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static com.ang.acb.popularmovies.utils.UiUtils.tintMenuIcon;

/**
 * The UI Controller for displaying the details of a movie loaded from The Movie DB.
 */
public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_ID = "extra_movie_id";
    private static final int INVALID_MOVIE_ID = -1;

    private ActivityDetailsBinding binding;
    private DetailsViewModel viewModel;
    private long movieId;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Note: when using Dagger for injecting Activity
        // objects, inject as early as possible.
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        initBinding();
        setupToolbar();
        initMovieId();
        setupViewModel();
        setupTrailersAdapter();
        setupCastAdapter();
        setupReviewsAdapter();
        observeResult();
    }

    private void initBinding(){
        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);
    }

    private void initMovieId(){
        movieId = getIntent().getLongExtra(EXTRA_MOVIE_ID, INVALID_MOVIE_ID);
        if (movieId == INVALID_MOVIE_ID) {
            closeOnError();
        }
    }

    private void setupToolbar() {
        setSupportActionBar(binding.detailsToolbar);
        if (getSupportActionBar() != null) {
            // Handle Up navigation
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void setupViewModel(){
        viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(DetailsViewModel.class);
        viewModel.init(movieId);
    }

    private void setupCastAdapter() {
        RecyclerView rvCast = binding.contentDetails.rvCast;
        rvCast.setLayoutManager(new LinearLayoutManager(
                this, RecyclerView.HORIZONTAL, false));
        rvCast.setAdapter(new CastAdapter());
    }

    private void setupTrailersAdapter() {
        RecyclerView rvTrailers = binding.contentDetails.rvTrailers;
        rvTrailers.setLayoutManager(new LinearLayoutManager(
                this, RecyclerView.HORIZONTAL, false));
        rvTrailers.setAdapter(new TrailersAdapter());
    }

    private void setupReviewsAdapter() {
        RecyclerView listReviews = binding.contentDetails.rvReviews;
        listReviews.setLayoutManager(new LinearLayoutManager(
                this, RecyclerView.VERTICAL, false));
        listReviews.setAdapter(new ReviewsAdapter());
    }

    private void observeResult() {
        viewModel.getLiveMovieDetails().observe(this, resource -> {
            if (resource.data != null && resource.data.movie != null) {
                // Handle toolbar title when collapsed.
                if (getSupportActionBar() != null) {
                    setToolbarTitleIfCollapsed(resource.data.movie);
                }

                // Handle adding/removing movie from favorites.
                viewModel.setFavorite(resource.data.movie.isFavorite());

                // Note: when movie details change, contents of menu also change,
                // so menu should be redrawn.
                invalidateOptionsMenu();

                // Bind movie data
                binding.setMovieDetails(resource.data);
            }
            binding.setResource(resource);

        });

        // Handle retry event in case of network failure.
        binding.networkState.retryButton.setOnClickListener(view -> viewModel.retry(movieId));

        // Observe the Snackbar messages showed when adding/removing movie from favorites.
        viewModel.getSnackbarMessage().observe(this, (Observer<Integer>) message ->
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show());
    }


    private void setToolbarTitleIfCollapsed(Movie movie) {
        // To set the title on the toolbar only when the toolbar is collapsed,
        // we need to add an OnOffsetChangedListener to AppBarLayout to determine
        // when CollapsingToolbarLayout is collapsed or expanded.
        // https://stackoverflow.com/questions/31662416/show-collapsingtoolbarlayout-title-only-when-collapsed
        // https://medium.com/@nullthemall/the-power-of-appbarlayout-offset-ecbf8eaa6b5f
        binding.detailsAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
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
                    binding.detailsCollapsingToolbar.setTitle(movie.getTitle());
                    isShown = true;
                } else if (isShown) {
                    // When toolbar is expanded, display an empty string.
                    binding.detailsCollapsingToolbar.setTitle(" ");
                    isShown = false;
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_details, menu);

        MenuItem favoriteItem = menu.findItem(R.id.action_add_remove_favorite);
        if (viewModel.isFavorite()) {
            favoriteItem.setIcon(R.drawable.ic_favorite_black_24dp)
                        .setTitle(R.string.movie_added_to_favorites);
        } else {
            favoriteItem.setIcon(R.drawable.ic_favorite_border_black_24dp)
                        .setTitle(R.string.movie_removed_from_favorites);
        }
        tintMenuIcon(this, favoriteItem, android.R.color.white);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.action_add_remove_favorite:
                viewModel.onFavoriteClicked();
                invalidateOptionsMenu();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}
