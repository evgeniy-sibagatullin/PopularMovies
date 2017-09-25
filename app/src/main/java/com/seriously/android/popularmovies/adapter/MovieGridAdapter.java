package com.seriously.android.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seriously.android.popularmovies.R;
import com.seriously.android.popularmovies.model.Movie;

import java.util.List;

public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.MovieViewHolder> {

    private static List<Movie> mMovies;

    class MovieViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView releaseDate;

        MovieViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            releaseDate = (TextView) itemView.findViewById(R.id.release_date);
        }

        void bind(Movie movie) {
            title.setText(movie.getTitle());
            releaseDate.setText(movie.getReleaseDate());
        }
    }

    public MovieGridAdapter(List<Movie> movies) {
        mMovies = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_grid_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(mMovies.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }
}
