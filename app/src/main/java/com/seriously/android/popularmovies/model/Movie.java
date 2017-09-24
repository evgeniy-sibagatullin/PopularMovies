package com.seriously.android.popularmovies.model;

public class Movie {

    private final String title;
    private final String releaseDate;
    private final String posterPath;
    private final int voteAverage;
    private final String overview;

    public Movie(String title, String releaseDate, String posterPath, int voteAverage, String overview) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.overview = overview;
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

    public int getVoteAverage() {
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

        if (voteAverage != movie.voteAverage) return false;
        if (title != null ? !title.equals(movie.title) : movie.title != null) return false;
        if (releaseDate != null ? !releaseDate.equals(movie.releaseDate) : movie.releaseDate != null)
            return false;
        if (posterPath != null ? !posterPath.equals(movie.posterPath) : movie.posterPath != null)
            return false;
        if (overview != null ? !overview.equals(movie.overview) : movie.overview != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (releaseDate != null ? releaseDate.hashCode() : 0);
        result = 31 * result + (posterPath != null ? posterPath.hashCode() : 0);
        result = 31 * result + voteAverage;
        result = 31 * result + (overview != null ? overview.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + getTitle() + '\'' +
                ", releaseDate='" + getReleaseDate() + '\'' +
                ", posterPath='" + getPosterPath() + '\'' +
                ", voteAverage=" + getVoteAverage() +
                ", overview='" + getOverview() + '\'' +
                '}';
    }
}
