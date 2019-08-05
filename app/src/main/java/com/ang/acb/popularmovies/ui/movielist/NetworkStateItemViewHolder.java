package com.ang.acb.popularmovies.ui.movielist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.popularmovies.data.vo.Resource;
import com.ang.acb.popularmovies.databinding.ItemNetworkStateBinding;

/**
 * A View Holder that can display a loading or have click action.
 * It is used to show the network state of paging.
 *
 * See: https://github.com/googlesamples/android-architecture-components/tree/PagingWithNetworkSample
 */
public class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {

    private ItemNetworkStateBinding binding;

    public NetworkStateItemViewHolder(@NonNull ItemNetworkStateBinding binding,
                                      final TmdbMoviesViewModel viewModel) {
        super(binding.getRoot());
        this.binding = binding;

        // Trigger retry event on click.
        binding.retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.retry();
            }
        });
    }

    public static NetworkStateItemViewHolder create(ViewGroup parent, TmdbMoviesViewModel viewModel) {
        // Inflate view and obtain an instance of the binding class.
        ItemNetworkStateBinding binding = ItemNetworkStateBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new NetworkStateItemViewHolder(binding, viewModel);
    }

    public void bindTo(Resource resource) {
        binding.setResource(resource);

        // Note: when a variable or observable object changes, the binding is scheduled
        // to change before the next frame. There are times, however, when binding must
        // be executed immediately. To force execution, use executePendingBindings().
        // https://developer.android.com/topic/libraries/data-binding/generated-binding#immediate_binding
        binding.executePendingBindings();
    }
}

