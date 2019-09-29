package com.ang.acb.popularmovies.ui.movielist;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.popularmovies.R;
import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.data.vo.Resource;

/**
 * A custom PagedListAdapter for the movie list items. A PagedListAdapter is an implementation
 * of RecyclerView.Adapter that presents data from a PagedList. PagedListAdapter listens to
 * PagedList loading callbacks as pages are loaded, and uses DiffUtil on a background thread
 * to compute fine grained updates as new PagedLists are received. Handles both the internal
 * paging of the list as more data is loaded, and updates in the form of new PagedLists.
 *
 * See: https://developer.android.com/topic/libraries/architecture/paging/ui#implement-diffing-callback
 * See: https://github.com/googlesamples/android-architecture-components/tree/master/PagingWithNetworkSample
 */
public class TmdbMoviesAdapter extends PagedListAdapter<Movie, RecyclerView.ViewHolder> {

    private static DiffUtil.ItemCallback<Movie> MOVIE_COMPARATOR =
        new DiffUtil.ItemCallback<Movie>() {
            @Override
            public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
                // The ID property identifies when items are the same.
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
                // Note: Don't use the "==" operator here. Either implement and
                // use .equals(), or write custom data comparison logic here.
                return oldItem.equals(newItem);
            }
    };

    private TmdbMoviesViewModel viewModel;
    private Resource resource = null;

    TmdbMoviesAdapter(TmdbMoviesViewModel viewModel) {
        super(MOVIE_COMPARATOR);
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case R.layout.item_movie:
                return MovieItemViewHolder.createViewHolder(parent);
            case R.layout.item_network_state:
                return NetworkStateItemViewHolder.createViewHolder(parent, viewModel);
            default:
                throw new IllegalArgumentException("Unknown view type " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case R.layout.item_movie:
                ((MovieItemViewHolder) holder).bindTo(getItem(position));
                break;
            case R.layout.item_network_state:
                ((NetworkStateItemViewHolder) holder).bindTo(resource);
                break;
            default:
                throw new IllegalArgumentException("Unknown view type " + getItemViewType(position));
        }
    }
    private boolean hasExtraRow() {
        return resource != null && resource.status != Resource.Status.SUCCESS;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (hasExtraRow() ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return R.layout.item_network_state;
        } else {
            return R.layout.item_movie;
        }
    }

    public void setNetworkState(Resource networkState) {
        Resource previousState = resource;
        boolean hadExtraRow = hasExtraRow();
        resource = networkState;
        boolean hasExtraRow = hasExtraRow();
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount());
            } else {
                notifyItemInserted(super.getItemCount());
            }
        } else if (hasExtraRow && !previousState.equals(networkState)) {
            notifyItemChanged(getItemCount() - 1);
        }
    }
}

