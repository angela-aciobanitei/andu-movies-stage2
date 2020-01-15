package com.ang.acb.popularmovies.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.ang.acb.popularmovies.data.vo.Trailer;

import java.util.List;

/**
 * Interface for database access on {@link Trailer} related operations.
 */
@Dao
public interface TrailerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTrailers(List<Trailer> trailers);
}
