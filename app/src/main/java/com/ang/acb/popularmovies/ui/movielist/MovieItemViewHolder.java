package com.ang.acb.popularmovies.ui.movielist;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.databinding.ItemMovieBinding;
import com.ang.acb.popularmovies.ui.moviedetails.DetailsActivity;

public class MovieItemViewHolder extends RecyclerView.ViewHolder {

    private final ItemMovieBinding binding;

    public MovieItemViewHolder(@NonNull ItemMovieBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public static MovieItemViewHolder create(ViewGroup parent) {
        // Inflate view and obtain an instance of the binding class.
        ItemMovieBinding binding = ItemMovieBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new MovieItemViewHolder(binding);
    }

    public void bindTo(final Movie movie) {
        binding.setMovie(movie);
        // Handle movie click events.
        binding.getRoot().setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), DetailsActivity.class);
            intent.putExtra(DetailsActivity.EXTRA_MOVIE_ID, movie.getId());
            view.getContext().startActivity(intent);
        });

        // Note: when a variable or observable object changes, the binding is scheduled
        // to change before the next frame. There are times, however, when binding must
        // be executed immediately. To force execution, use executePendingBindings().
        // https://developer.android.com/topic/libraries/data-binding/generated-binding#immediate_binding
        binding.executePendingBindings();
    }

}

