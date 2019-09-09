package com.ang.acb.popularmovies.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ang.acb.popularmovies.data.vo.Cast;
import com.ang.acb.popularmovies.data.vo.Movie;
import com.ang.acb.popularmovies.data.vo.Review;
import com.ang.acb.popularmovies.data.vo.Trailer;

/**
 * The Room database for this app.
 *
 * See: https://medium.com/androiddevelopers/7-steps-to-room-27a5fe5f99b2
 * See: https://medium.com/androiddevelopers/7-pro-tips-for-room-fbadea4bfbd1
 */
@Database(
        entities = {Movie.class, Trailer.class, Cast.class, Review.class},
        version = 1,
        exportSchema = false)
@TypeConverters(GenreConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "movies.db";
    private static final Object sLock = new Object();
    private static AppDatabase sInstance;

    public abstract MovieDao movieDao();
    public abstract TrailerDao trailerDao();
    public abstract CastDao castDao();
    public abstract ReviewDao reviewDao();

    private static AppDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                DATABASE_NAME)
                    .build();
    }

    // Returns the single instance of this class, creating it if necessary.
    public static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = buildDatabase(context);
            }
            return sInstance;
        }
    }
}

