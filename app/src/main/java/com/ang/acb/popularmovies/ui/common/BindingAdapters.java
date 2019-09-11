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
import com.ang.acb.popularmovies.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

/**
 * Binding adapters are responsible for making the appropriate framework calls to set values.
 *
 * See: https://developer.android.com/topic/libraries/data-binding/binding-adapters
 * See: https://github.com/googlesamples/android-sunflower/blob/master/app/src/main/java/com/google/samples/apps/sunflower/adapters
 */
public class BindingAdapters {

    // Used for movie poster or movie backdrop images
    @BindingAdapter({"imageUrl", "isPoster"})
    public static void bindImage(ImageView imageView, String imagePath, boolean isPoster) {
        String baseUrl;
        if (isPoster) baseUrl = Constants.IMAGE_URL;
        else baseUrl = Constants.BACKDROP_URL;

        Glide.with(imageView.getContext())
                .load(baseUrl + imagePath)
                .apply(new RequestOptions().placeholder(R.color.colorImagePlaceholder))
                .into(imageView);
    }

    @BindingAdapter("toggleVisibility")
    public static void toggleVisibility(View view, Boolean isVisible) {
        if (isVisible) view.setVisibility(View.VISIBLE);
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
            chip.setTextColor(context.getResources().getColor(android.R.color.white));
            // Set the chips's stroke width.
            float chipStrokeWidth = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 1,
                    context.getResources().getDisplayMetrics());
            chip.setChipStrokeWidth(chipStrokeWidth);
            // Set the chip's stroke color and background color.
            chip.setChipStrokeColorResource(android.R.color.white);
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

