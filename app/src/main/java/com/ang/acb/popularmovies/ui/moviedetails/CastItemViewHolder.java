package com.ang.acb.popularmovies.ui.moviedetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.popularmovies.R;
import com.ang.acb.popularmovies.data.vo.Cast;
import com.ang.acb.popularmovies.databinding.ItemCastBinding;
import com.ang.acb.popularmovies.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * A ViewHolder that works with a DataBinding.
 */
public class CastItemViewHolder extends RecyclerView.ViewHolder {

    private ItemCastBinding binding;
    private Context context;

    public CastItemViewHolder(@NonNull ItemCastBinding binding, Context context) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
    }

    public static CastItemViewHolder create(ViewGroup parent) {
        // Inflate view and obtain an instance of the binding class.
        ItemCastBinding binding = ItemCastBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CastItemViewHolder(binding, parent.getContext());
    }

    public void bindTo(final Cast cast) {
        String profileImage = Constants.IMAGE_BASE_URL +
                Constants.PROFILE_SIZE_W185 +
                cast.getProfileImagePath();
        Glide.with(context)
                .load(profileImage)
                .apply(new RequestOptions().placeholder(R.color.colorImagePlaceholder))
                .into(binding.castItemProfileImage);

        binding.castItemName.setText(cast.getActorName());

        // Note: when a variable or observable object changes, the binding is scheduled
        // to change before the next frame. There are times, however, when binding must
        // be executed immediately. To force execution, use executePendingBindings().
        // https://developer.android.com/topic/libraries/data-binding/generated-binding#immediate_binding
        binding.executePendingBindings();
    }
}

