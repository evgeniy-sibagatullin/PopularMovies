package com.seriously.android.popularmovies.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Review {

    private static final String JSON_AUTHOR = "author";
    private static final String JSON_CONTENT = "content";
    private static final String JSON_URL = "url";

    private final String author;
    private final String content;
    private final String url;

    private Review(String author, String content, String url) {
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public static Review getInstance(JSONObject jsonObject) throws JSONException {
        String author = jsonObject.getString(JSON_AUTHOR);
        String content = jsonObject.getString(JSON_CONTENT);
        String url = jsonObject.getString(JSON_URL);
        return new Review(author, content, url);
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;

        if (!author.equals(review.author)) return false;
        if (!content.equals(review.content)) return false;
        if (!url.equals(review.url)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = author.hashCode();
        result = 31 * result + content.hashCode();
        result = 31 * result + url.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Review{" +
                "author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
