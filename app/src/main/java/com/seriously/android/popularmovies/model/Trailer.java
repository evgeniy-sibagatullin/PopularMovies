package com.seriously.android.popularmovies.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Trailer {

    private static final String JSON_NAME = "name";
    private static final String JSON_SOURCE = "source";

    private final String name;
    private final String source;

    private Trailer(String name, String source) {
        this.name = name;
        this.source = source;
    }

    public static Trailer getInstance(JSONObject jsonObject) throws JSONException {
        String name = jsonObject.getString(JSON_NAME);
        String source = jsonObject.getString(JSON_SOURCE);
        return new Trailer(name, source);
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trailer review = (Trailer) o;

        if (!name.equals(review.name)) return false;
        if (!source.equals(review.source)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + source.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Review{" +
                "name='" + name + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}
