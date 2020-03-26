package com.example.android.popularmoviesstage1.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class MovieList {

    @SerializedName("page")
    public int page;
    @SerializedName("results")
    public List<Movie> movies;
    @SerializedName("total_pages")
    public int totalPages;
    @SerializedName("total_results")
    public int totalResults;

}
