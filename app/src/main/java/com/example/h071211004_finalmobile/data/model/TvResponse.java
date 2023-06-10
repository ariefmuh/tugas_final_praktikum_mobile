package com.example.h071211004_finalmobile.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TvResponse {
    @SerializedName("results")
    private List<Tv> tvShows;

    public List<Tv> getTvShows() {
        return tvShows;
    }
}

