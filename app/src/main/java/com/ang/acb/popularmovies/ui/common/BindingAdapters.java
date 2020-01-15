package com.ang.acb.popularmovies.ui.common;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.popularmovies.R;
import com.ang.acb.popularmovies.data.vo.Cast;
import com.ang.acb.popularmovies.data.vo.Genre;
import com.ang.acb.popularmovies.data.vo.Review;
import com.ang.acb.popularmovies.data.vo.Trailer;
import com.ang.acb.popularmovies.ui.moviedetails.CastAdapter;
import com.ang.acb.popularmovies.ui.moviedetails.ReviewsAdapter;
import com.ang.acb.popularmovies.ui.moviedetails.TrailersAdapter;
import com.ang.acb.popularmovies.utils.AppConstants;
import com.ang.acb.popularmovies.utils.GlideApp;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

/**
 * Binding adapters are responsible for making the appropriate framework calls to set values.
 *
 * See: https://developer.android.com/topic/libraries/data-binding/binding-adapters
 * See: https://github.com/android/android-sunflower/blob/master/app/src/main/java/com/google/samples/apps/sunflower/adapters
 */
public class BindingAdapters {

    /**
     * Uses the Glide library to load an image by URL into an ImageView.
     */
    @BindingAdapter("posterPath")
    public static void bindPosterImage(ImageView imageView, String posterPath) {
        GlideApp.with(imageView.getContext())
                .load(AppConstants.IMAGE_URL + posterPath)
                // Display a placeholder until the image is loaded and processed.
                .placeholder(R.drawable.loading_animation)
                // Provide an error placeholder for non-existing-urls.
                .error(R.color.colorImagePlaceholder)
                // Provide a fallback placeholder for when the url is null.
                .fallback(R.color.colorImagePlaceholder)
                .into(imageView);
    }


    @BindingAdapter("backdropPath")
    public static void bindBackdropImage(ImageView imageView, String backdropPath) {
        GlideApp.with(imageView.getContext())
                .load(AppConstants.BACKDROP_URL + backdropPath)
                // Display a placeholder until the image is loaded and processed.
                .placeholder(R.color.colorPrimary)
                // Provide an error placeholder for non-existing-urls.
                .error(R.color.colorPrimary)
                // Provide a fallback placeholder for when the url is null.
                .fallback(R.color.colorPrimary)
                .into(imageView);
    }

    @BindingAdapter("toggleVisibility")
    public static void toggleVisibility(View view, Boolean show) {
        if (show) view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.GONE);
    }

    @BindingAdapter("chipGroupItems")
    public static void setItems(ChipGroup view, List<Genre> genres) {
        // Because we are using live data, any changes that will trigger the
        // observer will also trigger data re-binding, which can lead to duplicates.
        if (genres == null || view.getChildCount() > 0) return;

        // Dynamically create genre chips.
        // https://stackoverflow.com/questions/55350567/dynamically-create-choice-chip-in-android
        Context context = view.getContext();
        for (Genre genre : genres) {
            // Initialize a new chip instance.
            Chip chip = new Chip(context);
            // Set the chip text.
            chip.setText(genre.getName());
            // Set the chips's stroke width.
            float chipStrokeWidth = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 1,
                    context.getResources().getDisplayMetrics());
            chip.setChipStrokeWidth(chipStrokeWidth);
            // Set the chip's stroke color and background color.
            chip.setChipStrokeColorResource(android.R.color.black);
            chip.setChipBackgroundColorResource(android.R.color.transparent);
            // Finally, add the chip to the chip group.
            view.addView(chip);
        }
    }

    @BindingAdapter("castItems")
    public static void setCastItems(RecyclerView recyclerView, List<Cast> items) {
        CastAdapter adapter = (CastAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.submitList(items);
        }
    }

    @BindingAdapter("trailersItems")
    public static void setTrailersItems(RecyclerView recyclerView, List<Trailer> items) {
        TrailersAdapter adapter = (TrailersAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.submitList(items);
        }
    }

    @BindingAdapter("reviewsItems")
    public static void setReviewsItems(RecyclerView recyclerView, List<Review> items) {
        ReviewsAdapter adapter = (ReviewsAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.submitList(items);
        }
    }
}

