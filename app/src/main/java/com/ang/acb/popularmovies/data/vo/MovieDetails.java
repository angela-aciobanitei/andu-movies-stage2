package com.ang.acb.popularmovies.data.vo;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

/**
 * A simple POJO containing a movie's details, trailers, cast and reviews.
 *
 * See: https://medium.com/@magdamiu/android-room-persistence-library-relations-75bbe02e8522
 */
public class MovieDetails {

    @Embedded
    public Movie movie = null;

    @Relation(parentColumn = "id", entityColumn = "movie_id")
    public List<Trailer> trailers = new ArrayList<>();

    @Relation(parentColumn = "id", entityColumn = "movie_id")
    public List<Cast> castList = new ArrayList<>();

    @Relation(parentColumn = "id", entityColumn = "movie_id")
    public List<Review> reviews = new ArrayList<>();
}
