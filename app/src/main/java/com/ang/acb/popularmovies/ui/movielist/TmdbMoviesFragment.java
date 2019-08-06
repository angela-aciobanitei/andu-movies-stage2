package com.ang.acb.popularmovies.ui.movielist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.popularmovies.R;
import com.ang.acb.popularmovies.utils.GridSpacingItemDecoration;
import com.ang.acb.popularmovies.utils.InjectorUtils;
import com.ang.acb.popularmovies.utils.ViewModelFactory;

/**
 * The UI Controller for displaying a list of movies loaded from tmdb.org.
 */
public class TmdbMoviesFragment extends Fragment {

    private static final String EXTRA_ACTION_ID = "EXTRA_ACTION_ID" ;

    public static TmdbMoviesFragment newInstance(int actionId) {
        TmdbMoviesFragment fragment = new TmdbMoviesFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_ACTION_ID, actionId);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tmdb_movies, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Setup view model.
        final MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        ViewModelFactory factory = InjectorUtils.provideViewModelFactory(activity);
        TmdbMoviesViewModel viewModel = ViewModelProviders
                .of(activity, factory)
                .get(TmdbMoviesViewModel.class);

        // Update current movies filter.
        Bundle bundle = getArguments();
        if (bundle != null) {
            viewModel.updateCurrentFilter(bundle.getInt(EXTRA_ACTION_ID));
        }

        // Setup the grid layout manager.
        final TmdbMoviesAdapter adapter =  new TmdbMoviesAdapter(viewModel);
        final GridLayoutManager layoutManager = new GridLayoutManager(
                activity, getResources().getInteger(R.integer.span_count));

        // Create a custom GridLayoutManager that allows different span counts
        // for different rows. This allows displaying the network status and
        // errors messages on a whole row (3 spans).
        // See: https://stackoverflow.com/questions/31112291/recyclerview-layoutmanager-different-span-counts-on-different-rows
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case R.layout.item_network_state:
                        return layoutManager.getSpanCount();
                    default:
                        return 1;
                }
            }
        });

        // Setup recycler view.
        RecyclerView recyclerView = activity.findViewById(R.id.rv_movie_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(activity, R.dimen.item_offset));

        // Observe paged list data.
        viewModel.getPagedData().observe(getViewLifecycleOwner(), adapter::submitList);

        // Observe network state.
        viewModel.getNetworkState().observe(getViewLifecycleOwner(), adapter::setNetworkState);

        // Observe current toolbar title.
        viewModel.getCurrentTitle().observe(getViewLifecycleOwner(), activity.getSupportActionBar()::setTitle);
    }
}
