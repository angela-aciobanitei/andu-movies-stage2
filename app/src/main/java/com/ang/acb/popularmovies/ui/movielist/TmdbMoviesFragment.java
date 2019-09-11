package com.ang.acb.popularmovies.ui.movielist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.popularmovies.R;
import com.ang.acb.popularmovies.utils.ItemOffsetDecoration;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * The UI Controller for displaying a list of movies loaded from The Movie DB.
 */
public class TmdbMoviesFragment extends Fragment {

    private static final String EXTRA_ACTION_ID = "EXTRA_ACTION_ID" ;

    private TmdbMoviesViewModel viewModel;
    private TmdbMoviesAdapter adapter;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    // Required empty public constructor
    public TmdbMoviesFragment() {}

    public static TmdbMoviesFragment newInstance(int actionId) {
        TmdbMoviesFragment fragment = new TmdbMoviesFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_ACTION_ID, actionId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(@NotNull Context context) {
        // Note: when using Dagger for injecting Fragment objects, inject as early as possible.
        // For this reason, call AndroidInjection.inject() in onAttach(). This also prevents
        // inconsistencies if the Fragment is reattached.
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
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

        initViewModel();
        updateMoviesFilter();
        setupRecyclerView();
        populateUi();
    }

    private void initViewModel() {
        viewModel = ViewModelProviders
                .of(getHostActivity(), viewModelFactory)
                .get(TmdbMoviesViewModel.class);
    }

    private void updateMoviesFilter() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            viewModel.updateCurrentFilter(bundle.getInt(EXTRA_ACTION_ID));
        }
    }

    private void setupRecyclerView() {
        // Setup the grid layout manager.
        adapter =  new TmdbMoviesAdapter(viewModel);
        final GridLayoutManager layoutManager = new GridLayoutManager(
                getHostActivity(), getResources().getInteger(R.integer.span_count));

        // Create a custom GridLayoutManager that allows different span counts
        // for different rows. This allows displaying the network status and
        // errors messages on a whole row (3 spans).
        // See: https://stackoverflow.com/questions/31112291/
        // recyclerview-layoutmanager-different-span-counts-on-different-rows
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
        RecyclerView recyclerView = getHostActivity().findViewById(R.id.rv_movie_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new ItemOffsetDecoration(
                getHostActivity(), R.dimen.item_offset));
    }

    private void populateUi() {
        // Observe paged list data.
        viewModel.getPagedData().observe(getViewLifecycleOwner(), adapter::submitList);

        // Observe network state.
        viewModel.getNetworkState().observe(getViewLifecycleOwner(), adapter::setNetworkState);

        // Observe current toolbar title.
        viewModel.getCurrentTitle().observe(getViewLifecycleOwner(),
                getHostActivity().getSupportActionBar()::setTitle);
    }

    private MainActivity getHostActivity() {
        return (MainActivity)getActivity();
    }
}
