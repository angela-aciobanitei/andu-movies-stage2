package com.ang.acb.popularmovies.data.repository;

import androidx.lifecycle.LiveData;

import com.ang.acb.popularmovies.data.remote.PagedMoviesResult;
import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.data.vo.MovieDetails;
import com.ang.acb.popularmovies.data.vo.Resource;
import com.ang.acb.popularmovies.ui.movielist.MoviesFilter;

import java.util.List;

/**
 * Main entry point for accessing movies data.
 *
 * See: https://github.com/googlesamples/android-architecture/tree/todo-mvp/todoapp
 */
public interface MovieDataSource {

    LiveData<Resource<MovieDetails>> loadMovie(long movieId);

    PagedMoviesResult loadMoviesFilteredBy(MoviesFilter sortBy);

    LiveData<List<Movie>> getAllFavoriteMovies();

    void markAsFavorite(Movie movie);

    void markAsNotFavorite(Movie movie);
}

