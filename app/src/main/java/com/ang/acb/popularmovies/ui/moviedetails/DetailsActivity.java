package com.ang.acb.popularmovies.ui.moviedetails;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ang.acb.popularmovies.R;
import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.data.vo.Resource;
import com.ang.acb.popularmovies.databinding.ActivityDetailsBinding;
import com.ang.acb.popularmovies.utils.InjectorUtils;
import com.ang.acb.popularmovies.utils.ViewModelFactory;
import com.google.android.material.appbar.AppBarLayout;

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

        // Observe result.
        viewModel.getResult().observe(this, new Observer<Resource<Movie>>() {
            @Override
            public void onChanged(Resource<Movie> resource) {
                binding.setResource(resource);
                binding.setMovie(resource.data);
            }
        });

        // Handle retry event in case of network failure.
        binding.networkState.retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.retry(movieId);
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
        // TODO Sets the title on the toolbar only if the toolbar is collapsed.
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
                    binding.collapsingToolbar.setTitle(viewModel.getResult().getValue().data.getTitle());
                    isShow = true;
                } else if (isShow) {
                    // Display an empty string when toolbar is expanded.
                    binding.collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void closeOnError() {
        throw new IllegalArgumentException("Access denied.");
    }
}
