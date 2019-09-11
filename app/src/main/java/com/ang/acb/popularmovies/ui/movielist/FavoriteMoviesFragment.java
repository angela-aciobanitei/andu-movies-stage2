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
import com.ang.acb.popularmovies.databinding.FragmentFavoriteMoviesBinding;
import com.ang.acb.popularmovies.utils.ItemOffsetDecoration;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * The UI Controller for displaying the list of favorite movies.
 */
public class FavoriteMoviesFragment extends Fragment {

    private FragmentFavoriteMoviesBinding binding;
    private FavoriteMoviesViewModel viewModel;
    private FavoriteMoviesAdapter adapter;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    // Required empty public constructor
    public FavoriteMoviesFragment() {}

    public static FavoriteMoviesFragment newInstance() {
        return new FavoriteMoviesFragment();
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
        binding = FragmentFavoriteMoviesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupToolbarTitle();
        setupViewModel();
        setupRecyclerView();
        populateUi();
    }

    private void setupToolbarTitle() {
        if (getHostActivity().getSupportActionBar() != null) {
            getHostActivity().getSupportActionBar()
                    .setTitle(getString(R.string.action_show_favorites));
        }
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders
                .of(getHostActivity(), viewModelFactory)
                .get(FavoriteMoviesViewModel.class);
    }

    private void setupRecyclerView() {
        adapter = new FavoriteMoviesAdapter();
        RecyclerView recyclerView = binding.favoriteMoviesList.rvMovieList;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(
                getHostActivity(), getResources().getInteger(R.integer.span_count)));
        recyclerView.addItemDecoration(new ItemOffsetDecoration(
                getHostActivity(), R.dimen.item_offset));
    }

    private void populateUi() {
        // Observe favorite movies list.
        viewModel.getFavoriteListLiveData().observe(getViewLifecycleOwner(), movieList -> {
            if (movieList.isEmpty()) {
                // No favorites, show empty state.
                binding.favoriteMoviesList.rvMovieList.setVisibility(View.GONE);
                binding.favoriteEmptyState.setVisibility(View.VISIBLE);
            } else {
                binding.favoriteMoviesList.rvMovieList.setVisibility(View.VISIBLE);
                binding.favoriteEmptyState.setVisibility(View.GONE);
                adapter.submitList(movieList);
            }
        });
    }

    private MainActivity getHostActivity() {
        return (MainActivity)getActivity();
    }
}
