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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.popularmovies.R;
import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.databinding.FragmentFavoriteMoviesBinding;
import com.ang.acb.popularmovies.utils.GridSpacingItemDecoration;
import com.ang.acb.popularmovies.utils.InjectorUtils;
import com.ang.acb.popularmovies.utils.ViewModelFactory;

import java.util.List;

/**
 * The UI Controller for displaying the list of favorite movies.
 */
public class FavoriteMoviesFragment extends Fragment {

    private FragmentFavoriteMoviesBinding binding;

    public static FavoriteMoviesFragment newInstance() {
        return new FavoriteMoviesFragment();
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

        // Setup action bar title.
        final MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(getString(R.string.action_show_favorites));
        }
        // Setup view model.
        ViewModelFactory factory = InjectorUtils.provideViewModelFactory(activity);
        FavoriteMoviesViewModel viewModel = ViewModelProviders
                .of(activity, factory)
                .get(FavoriteMoviesViewModel.class);

        // Setup recycler view.
        final FavoriteMoviesAdapter adapter = new FavoriteMoviesAdapter();
        RecyclerView recyclerView = binding.favoriteMoviesList.rvMovieList;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(
                activity, getResources().getInteger(R.integer.span_count)));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(
                activity, R.dimen.item_offset));

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

}
