package com.ang.acb.popularmovies.data.vo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static androidx.room.ForeignKey.CASCADE;

/**
 * Immutable model class for a Cast.
 *
 * See: https://developer.android.com/training/data-storage/room/relationships#one-to-many
 * See: https://android.jlelse.eu/android-architecture-components-room-relationships-bf473510c14a
 */
@Entity(tableName = "cast",
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
public class Cast {

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    private long id;

    @ColumnInfo(name = "movie_id")
    private long movieId;

    @SerializedName("cast_id")
    @ColumnInfo(name = "cast_id")
    private long castId;

    @SerializedName("credit_id")
    @ColumnInfo(name = "credit_id")
    private String creditId;

    @SerializedName("character")
    @ColumnInfo(name = "character_name")
    private String characterName;

    @SerializedName("gender")
    private int gender;

    @SerializedName("order")
    private int order;

    @SerializedName("name")
    @ColumnInfo(name = "actor_name")
    private String actorName;

    @SerializedName("profile_path")
    @ColumnInfo(name = "profile_image_path")
    private String profileImagePath;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public long getCastId() {
        return castId;
    }

    public void setCastId(long castId) {
        this.castId = castId;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Cast cast = (Cast) object;
        return id == cast.id &&
                movieId == cast.movieId &&
                castId == cast.castId &&
                Objects.equals(creditId, cast.creditId) &&
                Objects.equals(characterName, cast.characterName) &&
                gender == cast.gender &&
                order == cast.order &&
                Objects.equals(actorName, cast.actorName) &&
                Objects.equals(profileImagePath, cast.profileImagePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
                movieId,
                castId,
                creditId,
                characterName,
                gender,
                order,
                actorName,
                profileImagePath);
    }
}
