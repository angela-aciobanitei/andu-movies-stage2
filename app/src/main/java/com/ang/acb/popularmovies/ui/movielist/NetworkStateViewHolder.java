package com.ang.acb.popularmovies.ui.movielist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.popularmovies.data.vo.Resource;
import com.ang.acb.popularmovies.databinding.ItemNetworkStateBinding;
import com.ang.acb.popularmovies.ui.common.RetryCallback;

/**
 * A View Holder that can display a loading or have click action.
 * It is used to show the network state of paging.
 *
 * See: https://github.com/android/android-architecture-components/tree/PagingWithNetworkSample
 */
/**
 * A View Holder that can display a loading or have click action.
 * It is used to show the network state of paging.
 *
 * See: https://github.com/android/android-architecture-components/tree/PagingWithNetworkSample
 */
public class NetworkStateViewHolder extends RecyclerView.ViewHolder {

    private ItemNetworkStateBinding binding;

    public NetworkStateViewHolder(@NonNull ItemNetworkStateBinding binding,
                                  RetryCallback retryCallback) {
        super(binding.getRoot());
        this.binding = binding;

        // Trigger retry event on click.
        binding.retryButton.setOnClickListener(view -> retryCallback.invoke());
    }

    public static NetworkStateViewHolder create(ViewGroup parent, RetryCallback retryCallback) {
        // Inflate view and obtain an instance of the binding class.
        ItemNetworkStateBinding binding = ItemNetworkStateBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new NetworkStateViewHolder(binding, retryCallback);
    }

    public void bindTo(Resource resource) {
        binding.setResource(resource);

        // Binding must be executed immediately.
        binding.executePendingBindings();
    }
}

