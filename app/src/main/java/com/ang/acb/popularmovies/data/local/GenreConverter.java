package com.ang.acb.popularmovies.data.local;

import androidx.room.TypeConverter;


import com.ang.acb.popularmovies.data.vo.Genre;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Type converters to allow Room to reference complex data types.
 */
public class GenreConverter {

    // Use Gson, a Java serialization/deserialization library, to convert Java Objects into JSON and back.
    // See: https://stackoverflow.com/questions/44986626/android-room-database-how-to-handle-arraylist-in-an-entity
    private static Gson gson = new Gson();

    @TypeConverter
    public static String genresToJson(List<Genre> genres) {
        // Serialization
        return gson.toJson(genres);
    }


    @TypeConverter
    public static List<Genre> jsonToGenres(String genres) {
        if (genres == null) return Collections.emptyList();

        // Get the type of the collection.
        Type listType = new TypeToken<List<Genre>>() {}.getType();

        // De-serialization
        return gson.fromJson(genres, listType);
    }
}

