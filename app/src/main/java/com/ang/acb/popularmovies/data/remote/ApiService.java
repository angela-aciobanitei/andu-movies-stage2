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

    @GET("movie/{id}")
    LiveData<ApiResponse<Movie>> getMovieDetails(@Path("id") long id);
}
