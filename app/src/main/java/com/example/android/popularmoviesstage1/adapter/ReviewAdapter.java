package com.example.android.popularmoviesstage1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.popularmoviesstage1.R;
import com.example.android.popularmoviesstage1.model.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviews;

    public ReviewAdapter() {
        // Empty constructor
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int position) {
        Review review = reviews.get(position);
        reviewViewHolder.bind(review);
    }

    @Override
    public int getItemCount() {
        return (reviews != null) ? reviews.size() : 0;
    }

    /**
     * Caching of the children views for a Movie item
     */
    static class ReviewViewHolder extends RecyclerView.ViewHolder {

        final TextView textViewReviewAuthor;
        final TextView textViewReviewContent;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewReviewAuthor = itemView.findViewById(R.id.tv_review_author);
            textViewReviewContent = itemView.findViewById(R.id.tv_review_content);
        }

        void bind(Review review) {
            textViewReviewAuthor.setText(review.getAuthor());
            textViewReviewContent.setText(review.getContent());
        }

    }

    public void setReviewData(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

}
