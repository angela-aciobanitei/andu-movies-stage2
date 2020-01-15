package com.ang.acb.popularmovies.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.ang.acb.popularmovies.data.vo.Review;

import java.util.List;

/**
 * Interface for database access on {@link Review} related operations.
 */
@Dao
public interface ReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReviews(List<Review> reviews);
}
