package com.seriously.android.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seriously.android.popularmovies.databinding.TrailerItemBinding;
import com.seriously.android.popularmovies.model.Trailer;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    public interface TrailerClickListener {
        void playTrailer(String source);
    }

    private final Context mContext;
    private final TrailerClickListener mListener;
    private final List<Trailer> mTrailers;

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TrailerItemBinding mBinding;

        TrailerViewHolder(TrailerItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(Trailer trailer) {
            mBinding.name.setText(trailer.getName());
            mBinding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListener.playTrailer(mTrailers.get(clickedPosition).getSource());
        }
    }

    public TrailerAdapter(Context context, TrailerClickListener listener, List<Trailer> trailers) {
        mContext = context;
        mListener = listener;
        mTrailers = trailers;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        TrailerItemBinding binding = TrailerItemBinding.inflate(inflater, parent, false);
        return new TrailerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bind(mTrailers.get(position));
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }
}
