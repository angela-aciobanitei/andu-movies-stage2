package com.ang.acb.popularmovies.ui.movielist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.popularmovies.R;
import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.data.vo.Resource;
import com.ang.acb.popularmovies.utils.GridSpacingItemDecoration;
import com.ang.acb.popularmovies.utils.InjectorUtils;
import com.ang.acb.popularmovies.utils.ViewModelFactory;

public class TmdbMoviesFragment extends Fragment {

    private static final String ACTION_ID_ARG = "ACTION_ID_ARG" ;

    public static TmdbMoviesFragment newInstance(int actionId) {
        TmdbMoviesFragment fragment = new TmdbMoviesFragment();
        Bundle args = new Bundle();
        args.putInt(ACTION_ID_ARG, actionId);
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
        ViewModelFactory factory = InjectorUtils.provideViewModelFactory(activity);
        TmdbMoviesViewModel viewModel = ViewModelProviders.of(activity, factory).get(TmdbMoviesViewModel.class);

        // Get sort by action sent from the hosting activity (MainActivity).
        Bundle bundle = getArguments();
        if (bundle != null) viewModel.setSortMoviesBy(bundle.getInt(ACTION_ID_ARG));

        // Setup the grid layout manager.
        final TmdbMoviesAdapter tmdbMoviesAdapter =  new TmdbMoviesAdapter(viewModel);
        final GridLayoutManager layoutManager = new GridLayoutManager(
                activity, getResources().getInteger(R.integer.span_count));

        // Create a custom GridLayoutManager that allows different span counts
        // for different rows. This allows displaying the network status and
        // errors messages on a whole row (3 spans).
        // See: https://stackoverflow.com/questions/31112291/recyclerview-layoutmanager-different-span-counts-on-different-rows
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (tmdbMoviesAdapter.getItemViewType(position)) {
                    case R.layout.item_network_state:
                        return layoutManager.getSpanCount();
                    default:
                        return 1;
                }
            }
        });

        // Setup recycler view.
        RecyclerView recyclerView = activity.findViewById(R.id.rv_movie_list);
        recyclerView.setAdapter(tmdbMoviesAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(activity, R.dimen.item_offset));

        // Observe paged list.
        viewModel.getPagedList().observe(getViewLifecycleOwner(), new Observer<PagedList<Movie>>() {
            @Override
            public void onChanged(PagedList<Movie> movies) {
                tmdbMoviesAdapter.submitList(movies);
            }
        });

        // Observe network state.
        viewModel.getNetworkState().observe(getViewLifecycleOwner(), new Observer<Resource>() {
            @Override
            public void onChanged(Resource resource) {
                tmdbMoviesAdapter.setNetworkState(resource);
            }
        });

        // Observe current toolbar title.
        viewModel.getCurrentTitle().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer title) {
                activity.getSupportActionBar().setTitle(title);
            }
        });
    }
}
