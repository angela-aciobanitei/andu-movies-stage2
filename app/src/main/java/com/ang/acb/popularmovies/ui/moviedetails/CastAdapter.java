package com.ang.acb.popularmovies.ui.moviedetails;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.popularmovies.data.vo.Cast;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastItemViewHolder> {

    private List<Cast> castList;

    @NonNull
    @Override
    public CastItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return CastItemViewHolder.createViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull CastItemViewHolder holder, int position) {
        holder.bindTo(castList.get(position));
    }

    @Override
    public int getItemCount() {
        return castList != null ? castList.size() : 0;
    }

    public void submitList(List<Cast> casts) {
        castList = casts;
        // Notify any registered observers
        // that the data set has changed.
        notifyDataSetChanged();
    }
}
