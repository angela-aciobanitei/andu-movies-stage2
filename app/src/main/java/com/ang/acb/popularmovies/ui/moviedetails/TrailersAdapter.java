package com.ang.acb.popularmovies.ui.moviedetails;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.ang.acb.popularmovies.data.vo.Trailer;

public class TrailersAdapter extends ListAdapter<Trailer, TrailerViewHolder> {

    protected TrailersAdapter() {
        super(TRAILER_COMPARATOR);
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return TrailerViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }

    private static DiffUtil.ItemCallback<Trailer> TRAILER_COMPARATOR =
            new DiffUtil.ItemCallback<Trailer>() {
                @Override
                public boolean areItemsTheSame(@NonNull Trailer oldItem, @NonNull Trailer newItem) {
                    // The ID property identifies when items are the same.
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Trailer oldItem, @NonNull Trailer newItem) {
                    // Note: Don't use the "==" operator here. Either implement and
                    // use .equals(), or write custom data comparison logic here.
                    return oldItem.equals(newItem);
                }
            };
}
