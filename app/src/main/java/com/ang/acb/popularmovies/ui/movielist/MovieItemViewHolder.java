package com.ang.acb.popularmovies.ui.movielist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.popularmovies.R;
import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.databinding.ItemMovieBinding;
import com.ang.acb.popularmovies.ui.moviedetails.DetailsActivity;
import com.ang.acb.popularmovies.utils.Constants;
import com.ang.acb.popularmovies.utils.GlideApp;
import com.ang.acb.popularmovies.utils.UiUtils;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import timber.log.Timber;

public class MovieItemViewHolder extends RecyclerView.ViewHolder {

    private final ItemMovieBinding binding;

    public MovieItemViewHolder(@NonNull ItemMovieBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public static MovieItemViewHolder createViewHolder(ViewGroup parent) {
        // Inflate view and obtain an instance of the binding class.
        ItemMovieBinding binding = ItemMovieBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new MovieItemViewHolder(binding);
    }

    public void bindTo(final Movie movie) {
        binding.setMovie(movie);

        GlideApp.with(binding.getRoot().getContext())
                // Calling Glide.with() returns a RequestBuilder.
                // By default you get a RequestBuilder<Drawable>, but
                // you can change the requested type using as... methods.
                // For example, asBitmap() returns a RequestListener<Bitmap>.
                .asBitmap()
                .load(Constants.IMAGE_URL + movie.getPosterPath())
                // Display a placeholder until the image is loaded and processed.
                .placeholder(R.color.colorImagePlaceholder)
                // Keep track of errors and successful image loading.
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException exception, Object model,
                                                Target<Bitmap> target, boolean isFirstResource) {
                        Timber.d("Image loading failed: %s", exception.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target,
                                                   DataSource dataSource, boolean isFirstResource) {
                        generatePaletteAsync(resource);
                        return false;
                    }
                })
                .into(binding.movieItemPoster);

        // Handle movie click events.
        binding.getRoot().setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), DetailsActivity.class);
            intent.putExtra(DetailsActivity.EXTRA_MOVIE_ID, movie.getId());
            view.getContext().startActivity(intent);
        });

        // Binding must be executed immediately.
        binding.executePendingBindings();
    }


    private void generatePaletteAsync(Bitmap bitmap) {
        // To extract prominent colors from an image, we can use the Platte
        // class. By passing in a PaletteAsyncListener to generate() method,
        // we can generate the palette asynchronously using an AsyncTask
        // to gather the Palette swatch information from the bitmap.
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = UiUtils.getDominantColor(palette);
                if (swatch != null) {
                    binding.movieItemCardView.setCardBackgroundColor(swatch.getRgb());
                    binding.movieItemCardView.setStrokeColor(swatch.getRgb());
                }
            }
        });
    }

}

