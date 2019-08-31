# Popular Movies Stage II

Expand on the movies app built in the previous project (Popular Movies Stage I) to create a fully-featured app that will allow users to view and play trailers, read reviews, and mark their favorite movies.

## Project Specs
*   Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.
*   UI contains a spinner or settings menu to toggle the sort order of the movies by: most popular, highest rated.
*   In a background thread, app queries [The Movie DB API](https://www.themoviedb.org/documentation/api?language=en-US) for the sort criteria specified in the settings menu.
*   When a user changes the sort criteria (most popular, highest rated, and favorites) the main view gets updated correctly.
*   When a movie poster thumbnail is selected, the movie details screen is launched.
*   Movie Details layout contains title, release date, movie poster, vote average, and plot synopsis.
*   Movie Details layout contains a section for displaying trailer videos and user reviews.
*   App requests for related videos for a selected movie via the /movie/{id}/videos endpoint in a background thread and displays those details when the user selects a movie.
*   App requests for user reviews for a selected movie via the /movie/{id}/reviews endpoint in a background thread and displays those details when the user selects a movie.
*   When a trailer is selected, app uses an Intent to launch the trailer.
*   In the movies detail screen, a user can tap a button (for example, a star) to mark it as a Favorite. 
*   Tapping on a favorite movie will unfavorite it.
*   The titles and IDs of the user’s favorite movies are stored using Room.
*   Data is updated whenever the user favorites or unfavorites a movie. 
*   When the "favorites" setting option is selected, the main view displays the entire favorites collection based on movie ids stored in the database.
*   LiveData is used to observe changes in the database and update the UI accordingly.
*   Database is not re-queried unnecessarily after rotation. Cached LiveData from ViewModel is used instead.

## What Will I Learn?
*   Properly handle network requests with [Retrofit](https://github.com/square/retrofit) and [OkHttp](https://github.com/square/okhttp).
*   Gradually load information on demand from a data source with [Paging Library](https://developer.android.com/topic/libraries/architecture/paging/).
*   Use [Room](https://developer.android.com/topic/libraries/architecture/room)'s capabilities for data persistence.
*   Manage UI-related data in a lifecycle-conscious way with [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) and [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel). 
*   Bind observable data to UI elements with [DataBinding](https://developer.android.com/topic/libraries/data-binding/).

## Libraries
*   [AndroidX](https://developer.android.com/jetpack/androidx/), a major improvement to the original [Android Support Library](https://developer.android.com/topic/libraries/support-library/index).
*   [Room](https://developer.android.com/topic/libraries/architecture/room), a SQLite object mapping library, part of the [Android Architecture Components](https://developer.android.com/topic/libraries/architecture).
*   [DataBinding](https://developer.android.com/topic/libraries/data-binding/)
*   [Paging](https://developer.android.com/topic/libraries/architecture/paging/)
*   [Retrofit 2](https://github.com/square/retrofit), a type-safe HTTP client for Android and Java.
*   [OkHttp](https://github.com/square/okhttp), an HTTP client for Android, Kotlin, and Java. 
*   [Gson](https://github.com/google/gson), a Java serialization/deserialization library to convert Java Objects into JSON and back.
*   [Glide](https://github.com/bumptech/glide), an image loading and caching library for Android.
*   [Timber](https://github.com/JakeWharton/timber), a logger that provides utility on top of Android's normal Log class. 
