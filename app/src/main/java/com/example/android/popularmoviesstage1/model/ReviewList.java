package com.example.android.popularmoviesstage1.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class ReviewList {

    @SerializedName("id")
    public int id;
    @SerializedName("page")
    public int page;
    @SerializedName("results")
    public List<Review> reviews;

}
