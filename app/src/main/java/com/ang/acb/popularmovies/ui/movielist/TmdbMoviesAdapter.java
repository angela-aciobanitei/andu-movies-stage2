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
 * A custom PagedListAdapter for the movie list items. A PagedListAdapter is an
 * implementation of RecyclerView.Adapter that presents data from a PagedList.
 * It uses DiffUtil as a parameter to calculate data differences and do all
 * the updates for you. The PagedListAdapter handles page load events using a
 * PagedList.Callback object. As the user scrolls, the PagedListAdapter calls
 * PagedList.loadAround() to provide hints to the underlying PagedList as to
 * which items it should fetch from the DataSource.
 *
 * See: https://developer.android.com/topic/libraries/architecture/paging/ui#implement-diffing-callback
 * See: https://github.com/googlesamples/android-architecture-components/tree/master/PagingWithNetworkSample
 */
public class TmdbMoviesAdapter extends PagedListAdapter<Movie, RecyclerView.ViewHolder> {

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
                return MovieItemViewHolder.create(parent);
            case R.layout.item_network_state:
                return NetworkStateItemViewHolder.create(parent, viewModel);
            default:
                throw new IllegalArgumentException("unknown view type " + viewType);
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
                throw new IllegalArgumentException("unknown view type");
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return R.layout.item_network_state;
        } else {
            return R.layout.item_movie;
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (hasExtraRow() ? 1 : 0);
    }

    private boolean hasExtraRow() {
        return resource != null && resource.status != Resource.Status.SUCCESS;
    }

    public void setNetworkState(Resource resource) {
        Resource previousState = this.resource;
        boolean hadExtraRow = hasExtraRow();
        this.resource = resource;
        boolean hasExtraRow = hasExtraRow();
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount());
            } else {
                notifyItemInserted(super.getItemCount());
            }
        } else if (hasExtraRow && !previousState.equals(resource)) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    private static DiffUtil.ItemCallback<Movie> MOVIE_COMPARATOR = new DiffUtil.ItemCallback<Movie>() {
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
}

