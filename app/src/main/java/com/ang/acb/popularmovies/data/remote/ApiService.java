package com.ang.acb.popularmovies.data.remote;

import androidx.lifecycle.LiveData;

import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.data.vo.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Defines the REST API access points for Retrofit.
 *
 * See: https://www.androidhive.info/2016/05/android-working-with-retrofit-http-library/
 * See: https://github.com/googlesamples/android-architecture-components/tree/master/GithubBrowserSample
 *
 */
public interface ApiService {

    // The endpoints are defined using special retrofit annotations to
    // encode details about the parameters and request method.
    // Note: every method must have an HTTP annotation that provides the
    // request method and relative URL. There are five built-in annotations:
    // GET, POST, PUT, DELETE, and HEAD. The relative URL of the resource is
    // specified in the annotation.
    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("page") int page);

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("page") int page);

    @GET("movie/now_playing")
    Call<MoviesResponse> getNowPlayingMovies(@Query("page") int page);

    // By using the query parameter "append_to_response" we can make
    // sub requests within the same namespace in a single HTTP request
    // thus eliminating duplicate requests and saving network bandwidth.
    // See: https://developers.themoviedb.org/3/getting-started/append-to-response
    @GET("movie/{id}?append_to_response=videos,credits,reviews")
    LiveData<ApiResponse<Movie>> getAllMovieDetails(@Path("id") long id);
}