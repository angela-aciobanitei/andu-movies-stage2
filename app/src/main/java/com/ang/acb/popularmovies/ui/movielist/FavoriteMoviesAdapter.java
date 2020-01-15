package com.ang.acb.popularmovies.ui.movielist;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.ang.acb.popularmovies.data.vo.Movie;

public class FavoriteMoviesAdapter extends ListAdapter<Movie, MovieViewHolder> {

    // Required default constructor matching super.
    protected FavoriteMoviesAdapter() {
        super(MOVIE_COMPARATOR);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MovieViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }

    private static DiffUtil.ItemCallback<Movie> MOVIE_COMPARATOR =
            new DiffUtil.ItemCallback<Movie>() {
                @Override
                public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
                    // The ID property identifies when items are the same.
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
                    // Note: Don't use the "==" operator here. Either implement and
                    // use .equals(), or write custom data comparison logic here.
                    return oldItem.equals(newItem);
                }
            };
}

