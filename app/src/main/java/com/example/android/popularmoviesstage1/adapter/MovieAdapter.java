package com.example.android.popularmoviesstage1.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.android.popularmoviesstage1.util.NetworkUtils;
import com.example.android.popularmoviesstage1.R;
import com.example.android.popularmoviesstage1.model.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    Context context;
    private List<Movie> movies;

    private final ListItemClickListener listItemClickListener;

    public MovieAdapter(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int position) {
        Movie movie = movies.get(position);
        movieViewHolder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return (movies != null) ? movies.size() : 0;
    }

    /**
     * Caching of the children views for a Movie item
     */
    @SuppressWarnings("deprecation")
    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView movieThumbnail;
        final TextView missingPoster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movieThumbnail = itemView.findViewById(R.id.iv_movie_thumbnail);
            missingPoster = itemView.findViewById(R.id.tv_no_poster);
            itemView.setOnClickListener(this);
        }

        void bind(Movie movie) {
            if (!TextUtils.isEmpty(movie.getPosterPath())) {
                Glide.with(this.itemView)
                        .load(NetworkUtils.buildImageUrl(movie.getPosterPath()))
                        .placeholder(R.drawable.large_movie_poster)
                        .into(movieThumbnail);
            } else {
                movieThumbnail.setVisibility(View.GONE);
                missingPoster.setVisibility(View.VISIBLE);
                missingPoster.setText(movie.getTitle());
            }
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            listItemClickListener.onListItemClick(clickedPosition);
        }
    }

    public void setMovieData(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

}
