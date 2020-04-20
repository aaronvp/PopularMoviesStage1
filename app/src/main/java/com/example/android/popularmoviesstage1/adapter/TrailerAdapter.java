package com.example.android.popularmoviesstage1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.popularmoviesstage1.R;
import com.example.android.popularmoviesstage1.model.Trailer;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private List<Trailer> trailers;

    private final ListItemClickListener listItemClickListener;

    public TrailerAdapter(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.trailer_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder trailerViewHolder, int position) {
        Trailer trailer = trailers.get(position);
        trailerViewHolder.bind(trailer);
    }

    @Override
    public int getItemCount() {
        return (trailers != null) ? trailers.size() : 0;
    }

    /**
     * Caching of the children views for a Movie item
     */
    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView textViewTrailer;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTrailer = itemView.findViewById(R.id.textViewTrailer);
            itemView.setOnClickListener(this);
        }

        void bind(Trailer trailer) {
            textViewTrailer.setText(trailer.getName());
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            listItemClickListener.onListItemClick(clickedPosition);
        }
    }

    public void setTrailerData(List<Trailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}
