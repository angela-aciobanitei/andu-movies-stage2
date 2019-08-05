package com.ang.acb.popularmovies.ui.moviedetails;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.popularmovies.data.vo.Trailer;

import java.util.List;

public class TrailersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Trailer> trailerList;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return TrailerItemViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Trailer trailer = trailerList.get(position);
        ((TrailerItemViewHolder) holder).bindTo(trailer);
    }

    @Override
    public int getItemCount() {
        return trailerList != null ? trailerList.size() : 0;
    }

    public void submitList(List<Trailer> trailers) {
        trailerList = trailers;
        notifyDataSetChanged();
    }
}
