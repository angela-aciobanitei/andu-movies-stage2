# Popular Movies Stage II

Expand on the movies app built in the previous project (Popular Movies Stage I) to create a fully-featured app that will allow users to view and play trailers, read reviews, and mark their favorite movies.

## Project Specs
*   Movies are displayed via a grid of their corresponding movie poster thumbnails.
*   When user taps a movie poster, the movie details are shown: title, release date, poster, vote average, and plot synopsis.
*   The movie details screen also contains a section for displaying trailer videos and user reviews.
*   The user can toggle the sort order of the movies by: most popular, highest rated, and now playing.
*   Users can mark their favorite movies and share the movie trailer’s YouTube URLs.
*   The favorite movies are stored in a local database, and can be displaedy even when offline.

## What Will I Learn?
*   Properly handle network requests with [Retrofit](https://github.com/square/retrofit) and [OkHttp](https://github.com/square/okhttp).
*   Gradually load information on demand from a data source with [Paging Library](https://developer.android.com/topic/libraries/architecture/paging/).
*   Use [Room](https://developer.android.com/topic/libraries/architecture/room)'s capabilities for data persistence.
*   Manage UI-related data in a lifecycle-conscious way with [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) and [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel). 
*   Bind observable data to UI elements with [DataBinding](https://developer.android.com/topic/libraries/data-binding/).

## Libraries
*   [Room](https://developer.android.com/topic/libraries/architecture/room), a SQLite object mapping library, part of the [Android Architecture Components](https://developer.android.com/topic/libraries/architecture).
*   [DataBinding](https://developer.android.com/topic/libraries/data-binding/)
*   [Paging](https://developer.android.com/topic/libraries/architecture/paging/)
*   [Retrofit 2](https://github.com/square/retrofit), a type-safe HTTP client for Android and Java.
*   [OkHttp](https://github.com/square/okhttp), an HTTP client for Android, Kotlin, and Java. 
*   [Gson](https://github.com/google/gson), a Java serialization/deserialization library to convert Java Objects into JSON and back.
*   [Glide](https://github.com/bumptech/glide), an image loading and caching library for Android.
*   [Timber](https://github.com/JakeWharton/timber), a logger that provides utility on top of Android's normal Log class. 
*   [Dagger 2](https://github.com/google/dagger), a fast dependency injector for Android and Java.

## Screenshots
<img src="/screenshots/movie_list_popular.png" width="300"/> <img src="/screenshots/movie_details_lotr1.png" width="300"/> 
<img src="/screenshots/movie_details_lotr2.png" width="300"/> <img src="/screenshots/movie_details_lotr3.png" width="300"/> 
