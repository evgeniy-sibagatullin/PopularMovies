package com.seriously.android.popularmovies.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.seriously.android.popularmovies.loader.MovieDbLoader;

import org.json.JSONException;
import org.json.JSONObject;

import static com.seriously.android.popularmovies.data.FavoritesContract.FavoriteEntry.COLUMN_FAVORITE_ID;
import static com.seriously.android.popularmovies.data.FavoritesContract.FavoriteEntry.COLUMN_FAVORITE_OVERVIEW;
import static com.seriously.android.popularmovies.data.FavoritesContract.FavoriteEntry.COLUMN_FAVORITE_POSTER_PATH;
import static com.seriously.android.popularmovies.data.FavoritesContract.FavoriteEntry.COLUMN_FAVORITE_RELEASE_DATE;
import static com.seriously.android.popularmovies.data.FavoritesContract.FavoriteEntry.COLUMN_FAVORITE_TITLE;
import static com.seriously.android.popularmovies.data.FavoritesContract.FavoriteEntry.COLUMN_FAVORITE_VOTE_AVERAGE;

public class Movie implements Parcelable {

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_RELEASE_DATE = "release_date";
    private static final String JSON_POSTER_PATH = "poster_path";
    private static final String JSON_VOTE_AVERAGE = "vote_average";
    private static final String JSON_OVERVIEW = "overview";

    private final String id;
    private final String title;
    private final String releaseDate;
    private final String posterPath;
    private final String voteAverage;
    private final String overview;

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    protected Movie(Parcel in) {
        id = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        voteAverage = in.readString();
        overview = in.readString();
    }

    private Movie(String id, String title, String releaseDate, String posterPath, String voteAverage, String overview) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.overview = overview;
    }

    public static Movie getInstance(JSONObject jsonObject) throws JSONException {
        String id = jsonObject.getString(JSON_ID);
        String title = jsonObject.getString(JSON_TITLE);
        String releaseDate = jsonObject.getString(JSON_RELEASE_DATE);
        String posterPath = jsonObject.getString(JSON_POSTER_PATH);
        String voteAverage = jsonObject.getString(JSON_VOTE_AVERAGE);
        String overview = jsonObject.getString(JSON_OVERVIEW);
        return new Movie(id, title, releaseDate, posterPath, voteAverage, overview);
    }

    public static Movie getInstance(Cursor cursor) {
        int idColumnIndex = cursor.getColumnIndex(COLUMN_FAVORITE_ID);
        int titleColumnIndex = cursor.getColumnIndex(COLUMN_FAVORITE_TITLE);
        int releaseDateColumnIndex = cursor.getColumnIndex(COLUMN_FAVORITE_RELEASE_DATE);
        int posterPathColumnIndex = cursor.getColumnIndex(COLUMN_FAVORITE_POSTER_PATH);
        int voteAverageColumnIndex = cursor.getColumnIndex(COLUMN_FAVORITE_VOTE_AVERAGE);
        int overviewColumnIndex = cursor.getColumnIndex(COLUMN_FAVORITE_OVERVIEW);

        String id = cursor.getString(idColumnIndex);
        String title = cursor.getString(titleColumnIndex);
        String releaseDate = cursor.getString(releaseDateColumnIndex);
        String posterPath = cursor.getString(posterPathColumnIndex);
        String voteAverage = cursor.getString(voteAverageColumnIndex);
        String overview = cursor.getString(overviewColumnIndex);

        return new Movie(id, title, releaseDate, posterPath, voteAverage, overview);
    }

    public boolean isFavorite() {
        return MovieDbLoader.isMovieFavoriteById(id);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (id != null ? !id.equals(movie.id) : movie.id != null) return false;
        if (title != null ? !title.equals(movie.title) : movie.title != null) return false;
        if (releaseDate != null ? !releaseDate.equals(movie.releaseDate) : movie.releaseDate != null)
            return false;
        if (posterPath != null ? !posterPath.equals(movie.posterPath) : movie.posterPath != null)
            return false;
        if (voteAverage != null ? !voteAverage.equals(movie.voteAverage) : movie.voteAverage != null)
            return false;
        if (overview != null ? !overview.equals(movie.overview) : movie.overview != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (releaseDate != null ? releaseDate.hashCode() : 0);
        result = 31 * result + (posterPath != null ? posterPath.hashCode() : 0);
        result = 31 * result + (voteAverage != null ? voteAverage.hashCode() : 0);
        result = 31 * result + (overview != null ? overview.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + getId() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", releaseDate='" + getReleaseDate() + '\'' +
                ", posterPath='" + getPosterPath() + '\'' +
                ", voteAverage=" + getVoteAverage() +
                ", overview='" + getOverview() + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
        parcel.writeString(voteAverage);
        parcel.writeString(overview);
    }
}
