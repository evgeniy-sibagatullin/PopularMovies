package com.seriously.android.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seriously.android.popularmovies.databinding.MovieSelectionGridItemBinding;
import com.seriously.android.popularmovies.model.Movie;
import com.seriously.android.popularmovies.utilities.ImageLoader;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    public interface MovieClickListener {
        void openDetails(Movie movie);
    }

    private final Context mContext;
    private final MovieClickListener mListener;
    private final List<Movie> mMovies;

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MovieSelectionGridItemBinding mBinding;

        MovieViewHolder(MovieSelectionGridItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(Movie movie) {
            mBinding.title.setText(movie.getTitle());
            ImageLoader.loadGridPosterImage(mContext, mBinding.poster, movie.getPosterPath());
            mBinding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListener.openDetails(mMovies.get(clickedPosition));
        }
    }

    public MovieAdapter(Context context, MovieClickListener listener, List<Movie> movies) {
        mContext = context;
        mListener = listener;
        mMovies = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        MovieSelectionGridItemBinding binding = MovieSelectionGridItemBinding
                .inflate(inflater, parent, false);
        return new MovieViewHolder(binding);
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
