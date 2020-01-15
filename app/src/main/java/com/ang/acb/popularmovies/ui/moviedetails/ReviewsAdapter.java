package com.ang.acb.popularmovies.ui.moviedetails;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.ang.acb.popularmovies.data.vo.Review;

public class ReviewsAdapter extends ListAdapter<Review, ReviewViewHolder> {

    protected ReviewsAdapter() {
        super(REVIEW_COMPARATOR);
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ReviewViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }

    private static DiffUtil.ItemCallback<Review> REVIEW_COMPARATOR =
            new DiffUtil.ItemCallback<Review>() {
                @Override
                public boolean areItemsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
                    // The ID property identifies when items are the same.
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
                    // Note: Don't use the "==" operator here. Either implement and
                    // use .equals(), or write custom data comparison logic here.
                    return oldItem.equals(newItem);
                }
            };
}