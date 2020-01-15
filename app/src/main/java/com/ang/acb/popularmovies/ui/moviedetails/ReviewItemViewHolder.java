package com.ang.acb.popularmovies.ui.moviedetails;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.popularmovies.data.vo.Review;
import com.ang.acb.popularmovies.databinding.ItemReviewBinding;

/**
 * A ViewHolder that works with DataBinding.
 */
public class ReviewItemViewHolder extends RecyclerView.ViewHolder {

    private ItemReviewBinding binding;

    public ReviewItemViewHolder(@NonNull ItemReviewBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public static ReviewItemViewHolder createViewHolder(ViewGroup parent) {
        // Inflate view and obtain an instance of the binding class.
        ItemReviewBinding binding = ItemReviewBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new ReviewItemViewHolder(binding);
    }

    public void bindTo(final Review review) {
        binding.setReview(review);

        // Binding must be executed immediately.
        binding.executePendingBindings();
    }

}

