package com.ang.acb.popularmovies.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;

import androidx.annotation.ColorRes;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.palette.graphics.Palette;

public class UiUtils {

    public static void tintMenuIcon(Context context, MenuItem item, @ColorRes int color) {
        // See: https://stackoverflow.com/questions/26780046/menuitem-tinting-on-appcompat-toolbar
        Drawable iconWrapper = DrawableCompat.wrap(item.getIcon());
        DrawableCompat.setTint(iconWrapper, context.getResources().getColor(color));
        item.setIcon(iconWrapper);
    }

    public static Palette.Swatch getDominantColor(Palette palette) {
        // To extract prominent colors from an image, we can use the Platte
        // class. When a palette is generated, a number of colors with different
        // profiles are extracted from the image: vibrant, dark vibrant,
        // light vibrant, muted, dark muted, light muted, and dominant.
        // These can be retrieved from the appropriate getter method.
        // See: https://developer.android.com/training/material/palette-colors
        Palette.Swatch result = palette.getDominantSwatch();
        if (palette.getVibrantSwatch() != null) {
            result = palette.getVibrantSwatch();
        } else if (palette.getMutedSwatch() != null) {
            result = palette.getMutedSwatch();
        }
        return result;
    }
}
