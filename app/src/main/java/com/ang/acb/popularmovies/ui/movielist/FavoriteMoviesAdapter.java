package com.ang.acb.popularmovies.ui.movielist;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.popularmovies.data.vo.Movie;

import java.util.List;

public class FavoriteMoviesAdapter extends RecyclerView.Adapter<MovieItemViewHolder> {

    private List<Movie> movieList;

    @NonNull
    @Override
    public MovieItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MovieItemViewHolder.createViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieItemViewHolder holder, int position) {
        holder.bindTo(movieList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieList != null ? movieList.size() : 0;
    }

    public void submitList(List<Movie> movies) {
        movieList = movies;
        // Notify any registered observers
        // that the data set has changed.
        notifyDataSetChanged();
    }
}

