package com.ang.acb.popularmovies.data.vo;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static androidx.room.ForeignKey.CASCADE;

/**
 * Immutable model class for a Trailer.
 *
 * See: https://developer.android.com/training/data-storage/room/relationships#one-to-many
 * See: https://android.jlelse.eu/android-architecture-components-room-relationships-bf473510c14a
 */
@Entity(tableName = "trailer",
        foreignKeys = @ForeignKey(
                entity = Movie.class,
                parentColumns = "id",
                childColumns = "movie_id",
                onDelete = CASCADE,
                onUpdate = CASCADE
        ),
        indices = {
                @Index(value = {"movie_id"})
        }
)
public class Trailer {

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    private String id;

    @ColumnInfo(name = "movie_id")
    private long movieId;

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String title;

    @SerializedName("site")
    private String site;

    @SerializedName("size")
    private int size;

    @SerializedName("type")
    private String type;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Trailer trailer = (Trailer) object;
        return Objects.equals(id, trailer.id) &&
                movieId == trailer.movieId &&
                Objects.equals(key, trailer.key) &&
                Objects.equals(title, trailer.title) &&
                Objects.equals(site, trailer.site) &&
                size == trailer.size &&
                Objects.equals(type, trailer.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
                movieId,
                key,
                title,
                site,
                size,
                type);
    }
}

