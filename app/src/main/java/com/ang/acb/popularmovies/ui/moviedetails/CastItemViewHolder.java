package com.ang.acb.popularmovies.ui.moviedetails;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.popularmovies.R;
import com.ang.acb.popularmovies.data.vo.Cast;
import com.ang.acb.popularmovies.databinding.ItemCastBinding;
import com.ang.acb.popularmovies.utils.AppConstants;
import com.ang.acb.popularmovies.utils.GlideApp;

/**
 * A ViewHolder that works with DataBinding.
 */
public class CastItemViewHolder extends RecyclerView.ViewHolder {

    private ItemCastBinding binding;

    public CastItemViewHolder(@NonNull ItemCastBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public static CastItemViewHolder create(ViewGroup parent) {
        // Inflate view and obtain an instance of the binding class.
        ItemCastBinding binding = ItemCastBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CastItemViewHolder(binding);
    }

    public void bindTo(final Cast cast) {
        binding.setCast(cast);

        String profileImageUrl = AppConstants.IMAGE_BASE_URL +
                AppConstants.PROFILE_SIZE_W185 +
                cast.getProfileImagePath();

        GlideApp.with(binding.getRoot().getContext())
                .load(profileImageUrl)
                // Display a placeholder until the image is loaded and processed.
                .placeholder(R.drawable.loading_animation)
                // Provide an error placeholder for non-existing-urls.
                .error(R.color.colorImagePlaceholder)
                // Provide a fallback placeholder for when the url is null.
                .fallback(R.color.colorImagePlaceholder)
                .into(binding.castItemProfileImage);

        // Binding must be executed immediately.
        binding.executePendingBindings();
    }
}

