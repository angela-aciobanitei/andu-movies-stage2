package com.ang.acb.popularmovies.ui.moviedetails;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.popularmovies.data.vo.Trailer;

import java.util.List;

public class TrailersAdapter extends RecyclerView.Adapter<TrailerItemViewHolder> {

    private List<Trailer> trailerList;

    @NonNull
    @Override
    public TrailerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return TrailerItemViewHolder.createViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerItemViewHolder holder, int position) {
        holder.bindTo(trailerList.get(position));
    }

    @Override
    public int getItemCount() {
        return trailerList != null ? trailerList.size() : 0;
    }

    public void submitList(List<Trailer> trailers) {
        trailerList = trailers;
        // Notify any registered observers
        // that the data set has changed.
        notifyDataSetChanged();
    }
}
