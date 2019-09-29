package com.ang.acb.popularmovies.ui.moviedetails;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.popularmovies.data.vo.Review;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewItemViewHolder> {

    private List<Review> reviewList;

    @NonNull
    @Override
    public ReviewItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ReviewItemViewHolder.createViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewItemViewHolder holder, int position) {
        holder.bindTo(reviewList.get(position));
    }

    @Override
    public int getItemCount() {
        return reviewList != null ? reviewList.size() : 0;
    }

    public void submitList(List<Review> reviews) {
        reviewList = reviews;
        // Notify any registered observers
        // that the data set has changed.
        notifyDataSetChanged();
    }
}