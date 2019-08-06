package com.ang.acb.popularmovies.ui.moviedetails;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.popularmovies.data.vo.Review;
import com.ang.acb.popularmovies.databinding.ItemReviewBinding;

/**
 * A ViewHolder that works with a DataBinding.
 */
public class ReviewItemViewHolder extends RecyclerView.ViewHolder {

    private ItemReviewBinding binding;

    public ReviewItemViewHolder(@NonNull ItemReviewBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public static ReviewItemViewHolder create(ViewGroup parent) {
        // Inflate view and obtain an instance of the binding class.
        ItemReviewBinding binding = ItemReviewBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new ReviewItemViewHolder(binding);
    }

    public void bindTo(final Review review) {
        binding.reviewAuthorName.setText(review.getAuthor());
        binding.reviewTextContent.setText(review.getContent());

        // Note: when a variable or observable object changes, the binding is scheduled
        // to change before the next frame. There are times, however, when binding must
        // be executed immediately. To force execution, use executePendingBindings().
        // https://developer.android.com/topic/libraries/data-binding/generated-binding#immediate_binding
        binding.executePendingBindings();
    }

}

