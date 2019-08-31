package com.ang.acb.popularmovies.ui.moviedetails;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.popularmovies.R;
import com.ang.acb.popularmovies.data.vo.Trailer;
import com.ang.acb.popularmovies.databinding.ItemTrailerBinding;
import com.ang.acb.popularmovies.utils.GlideApp;

import static com.ang.acb.popularmovies.utils.Constants.YOUTUBE_APP_BASE_URL;
import static com.ang.acb.popularmovies.utils.Constants.YOUTUBE_TRAILER_THUMBNAIL_BASE_URL;
import static com.ang.acb.popularmovies.utils.Constants.YOUTUBE_TRAILER_THUMBNAIL_HQ;
import static com.ang.acb.popularmovies.utils.Constants.YOUTUBE_WEB_BASE_URL;

/**
 * A ViewHolder that works with a DataBinding.
 */
public class TrailerItemViewHolder extends RecyclerView.ViewHolder {

    private ItemTrailerBinding binding;
    private Context context;

    public TrailerItemViewHolder(@NonNull ItemTrailerBinding binding, Context context) {
        super(binding.getRoot());

        this.binding = binding;
        this.context = context;
    }

    public static TrailerItemViewHolder create(ViewGroup parent) {
        // Inflate view and obtain an instance of the binding class.
        ItemTrailerBinding binding = ItemTrailerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new TrailerItemViewHolder(binding, parent.getContext());
    }

    public void bindTo(final Trailer trailer) {
        // See: https://stackoverflow.com/questions/2068344/how-do-i-get-a-youtube-video-thumbnail-from-the-youtube-api
        String thumbnail = YOUTUBE_TRAILER_THUMBNAIL_BASE_URL +
                trailer.getKey() + YOUTUBE_TRAILER_THUMBNAIL_HQ;
        GlideApp.with(context)
                .load(thumbnail)
                .placeholder(R.color.colorImagePlaceholder)
                .into(binding.trailerThumbnail);

        binding.trailerName.setText(trailer.getTitle());

        binding.trailerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // See: https://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
                Intent appIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(YOUTUBE_APP_BASE_URL + trailer.getKey()));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(YOUTUBE_WEB_BASE_URL + trailer.getKey()));
                if (appIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(appIntent);
                } else {
                    context.startActivity(webIntent);
                }
            }
        });

        // Note: when a variable or observable object changes, the binding is scheduled
        // to change before the next frame. There are times, however, when binding must
        // be executed immediately. To force execution, use executePendingBindings().
        // https://developer.android.com/topic/libraries/data-binding/generated-binding#immediate_binding
        binding.executePendingBindings();
    }

}
