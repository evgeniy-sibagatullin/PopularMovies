package com.seriously.android.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seriously.android.popularmovies.databinding.ReviewItemBinding;
import com.seriously.android.popularmovies.model.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private static final int REVIEW_CONTENT_LENGTH = 300;

    public interface ReviewClickListener {
        void openReview(String url);
    }

    private final Context mContext;
    private final ReviewClickListener mListener;
    private final List<Review> mReviews;

    class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ReviewItemBinding mBinding;

        ReviewViewHolder(ReviewItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(Review review) {
            mBinding.author.setText(review.getAuthor());
            mBinding.content.setText(prepareContent(review.getContent()));
            mBinding.getRoot().setOnClickListener(this);
        }

        private String prepareContent(String content) {
            return content.length() > REVIEW_CONTENT_LENGTH ?
                    content.substring(0, REVIEW_CONTENT_LENGTH) + "..." : content;
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListener.openReview(mReviews.get(clickedPosition).getUrl());
        }
    }

    public ReviewAdapter(Context context, ReviewClickListener listener, List<Review> reviews) {
        mContext = context;
        mListener = listener;
        mReviews = reviews;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ReviewItemBinding binding = ReviewItemBinding.inflate(inflater, parent, false);
        return new ReviewViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bind(mReviews.get(position));
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }
}
