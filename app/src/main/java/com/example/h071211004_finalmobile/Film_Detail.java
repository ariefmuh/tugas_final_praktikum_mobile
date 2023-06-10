package com.example.h071211004_finalmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.h071211004_finalmobile.data.model.Favorite;
import com.example.h071211004_finalmobile.data.model.Movie;
import com.example.h071211004_finalmobile.data.model.Tv;
import com.example.h071211004_finalmobile.database.DatabaseHelper;

public class Film_Detail extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private ImageView backdropImageView, backButton, favoriteButton, posterImageView, type;
    private TextView titleTextView, ratingTextView, synopsisTextView;
    boolean favorite = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);

        type = findViewById(R.id.iv_type);
        backdropImageView = findViewById(R.id.iv_backdrop);
        backButton = findViewById(R.id.btn_back);
        favoriteButton = findViewById(R.id.btn_favorite);
        posterImageView = findViewById(R.id.iv_poster);
        titleTextView = findViewById(R.id.tv_title);
        ratingTextView = findViewById(R.id.tv_rating);
        synopsisTextView = findViewById(R.id.tv_synopsis);
        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        if (intent.getParcelableExtra("movie") != null) {
            Movie movie = intent.getParcelableExtra("movie");
            type.setImageResource(R.drawable.ic_movie);
            handleFilmDetails(movie.getTitle(), movie.getVoteAverage().toString(), movie.getOverview(), movie.getPosterPath(), movie.getBackdropUrl(), movie.getId(), movie.getReleaseDate());
        } else if (intent.getParcelableExtra("show") != null) {
            Tv show = intent.getParcelableExtra("show");
            type.setImageResource(R.drawable.ic_tv);
            handleFilmDetails(show.getName(), show.getVoteAverage().toString(), show.getOverview(), show.getPosterUrl(), show.getBackdropUrl(), show.getId(), show.getName());
        } else if (intent.getParcelableExtra("favorite") != null) {
            Favorite favorite = intent.getParcelableExtra("favorite");
            handleFilmDetails(favorite.getTitle(), favorite.getVoteAverage().toString(), favorite.getOverview(), favorite.getPosterPath(), favorite.getBackdropUrl(), favorite.getId(), favorite.getTitle());
        }

        backButton.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void addMovieToFavorites(int id, String overview, String posterUrl, String releaseDate, String title, double voteAverage, String backdropUrl) {
        Movie movie = new Movie(id, overview, posterUrl, releaseDate, title, voteAverage, backdropUrl);
        long result = dbHelper.insertMovie(movie);
        if (result != -1) {
            Toast.makeText(this, "Success added to favorites", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add to favorites", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteMovieFromFavorites(String nama) {
        long result = dbHelper.deleteMovie(nama);
        if (result != -1) {
            Toast.makeText(this, "Success deleted from favorites", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to delete from favorites", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleFilmDetails(String title, String voteAverage, String overview, String posterPath, String backdropPath, int id, String releaseDate) {
        String posterUrl = "https://image.tmdb.org/t/p/w300_and_h450_bestv2/" + posterPath;
        String backdropUrl = "https://image.tmdb.org/t/p/w300_and_h450_bestv2/" + backdropPath;
        titleTextView.setText(title);
        ratingTextView.setText(voteAverage);
        Glide.with(this).load(posterUrl).into(posterImageView);
        Glide.with(this).load(backdropUrl).into(backdropImageView);
        synopsisTextView.setText(overview);

        if (dbHelper.isMovieInFavorites(title)) {
            favorite = !favorite;
        }

        int favoriteButtonImageResource = favorite ? R.drawable.ic_favorite_border_full : R.drawable.ic_favorite_border;
        favoriteButton.setImageResource(favoriteButtonImageResource);

        favoriteButton.setOnClickListener(view -> {
            if (!favorite) {
                favoriteButton.setImageResource(R.drawable.ic_favorite_border_full);
                favorite = true;
                addMovieToFavorites(id, overview, posterUrl, releaseDate, title, Double.parseDouble(voteAverage), backdropUrl);
            } else {
                favoriteButton.setImageResource(R.drawable.ic_favorite_border);
                favorite = false;
                deleteMovieFromFavorites(title);
            }
        });

    }
}
