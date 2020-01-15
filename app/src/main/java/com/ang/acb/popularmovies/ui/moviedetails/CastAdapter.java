package com.ang.acb.popularmovies.ui.moviedetails;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.ang.acb.popularmovies.data.vo.Cast;


public class CastAdapter extends ListAdapter<Cast, CastViewHolder> {

    protected CastAdapter() {
        super(CAST_COMPARATOR);
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return CastViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }

    private static DiffUtil.ItemCallback<Cast> CAST_COMPARATOR =
            new DiffUtil.ItemCallback<Cast>() {
                @Override
                public boolean areItemsTheSame(@NonNull Cast oldItem, @NonNull Cast newItem) {
                    // The ID property identifies when items are the same.
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Cast oldItem, @NonNull Cast newItem) {
                    // Note: Don't use the "==" operator here. Either implement and
                    // use .equals(), or write custom data comparison logic here.
                    return oldItem.equals(newItem);
                }
            };
}


