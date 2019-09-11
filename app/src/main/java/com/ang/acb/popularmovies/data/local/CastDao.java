package com.ang.acb.popularmovies.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.ang.acb.popularmovies.data.vo.Cast;

import java.util.List;

/**
 * Interface for database access on {@link Cast} related operations.
 *
 * See: https://medium.com/androiddevelopers/7-steps-to-room-27a5fe5f99b2
 * See: https://medium.com/androiddevelopers/7-pro-tips-for-room-fbadea4bfbd1
 */
@Dao
public interface CastDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCastList(List<Cast> castList);
}