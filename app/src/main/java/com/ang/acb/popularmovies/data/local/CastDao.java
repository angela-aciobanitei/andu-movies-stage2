package com.ang.acb.popularmovies.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.ang.acb.popularmovies.data.vo.Cast;

import java.util.List;

/**
 * Interface for database access on {@link Cast} related operations.
 */
@Dao
public interface CastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCastList(List<Cast> castList);
}