package com.example.android.popularmoviesstage1.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class TrailerList {

    @SerializedName("id")
    public int id;
    @SerializedName("results")
    public List<Trailer> trailers;

}
