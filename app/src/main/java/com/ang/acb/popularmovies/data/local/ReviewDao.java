package com.ang.acb.popularmovies.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.ang.acb.popularmovies.data.vo.Review;

import java.util.List;

/**
 * Interface for database access on Reviews related operations.
 *
 * See: https://medium.com/androiddevelopers/7-steps-to-room-27a5fe5f99b2
 * See: https://medium.com/androiddevelopers/7-pro-tips-for-room-fbadea4bfbd1
 */
@Dao
public interface ReviewDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertReviews(List<Review> reviews);

}
