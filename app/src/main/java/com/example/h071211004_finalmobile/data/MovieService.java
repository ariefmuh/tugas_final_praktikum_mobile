package com.example.h071211004_finalmobile.data;

import com.example.h071211004_finalmobile.data.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {
    @GET("movie/now_playing")
    Call<MovieResponse> getNowPlayingMovies(
            @Query("api_key") String apiKey
    );
}
