package com.ang.acb.popularmovies.utils;

/**
 * AppConstants used throughout the app.
 *
 * See: https://github.com/android/android-sunflower
 */
public final class AppConstants {

    public static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";

    public static final String YOUTUBE_WEB_BASE_URL = "https://www.youtube.com/watch?v=";
    public static final String YOUTUBE_APP_BASE_URL = "vnd.youtube:";
    public static final String YOUTUBE_TRAILER_THUMBNAIL_BASE_URL = "https://img.youtube.com/vi/";
    public static final String YOUTUBE_TRAILER_THUMBNAIL_HQ = "/hqdefault.jpg";

    public static final String IMAGE_SIZE_W185 = "w185";
    public static final String PROFILE_SIZE_W185 = "w185";
    public static final String BACKDROP_SIZE_W780 = "w780";

    public static final String IMAGE_URL = IMAGE_BASE_URL + IMAGE_SIZE_W185;
    public static final String BACKDROP_URL = IMAGE_BASE_URL + BACKDROP_SIZE_W780;

    // Prevent direct instantiation.
    private AppConstants() {}
}
