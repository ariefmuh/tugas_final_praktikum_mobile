package com.example.h071211004_finalmobile;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.h071211004_finalmobile.data.MovieService;
import com.example.h071211004_finalmobile.data.model.Favorite;
import com.example.h071211004_finalmobile.data.model.Movie;
import com.example.h071211004_finalmobile.data.model.MovieResponse;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieFragment extends Fragment {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY = "dad1cd55d3f6d09536f1c6bde1fe8d07";

    private ProgressBar progressBar;
    private TextView tvAlert;
    private ImageView btnRefresh;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private Button ClearSort, NameSort, VoteSort;
    private TextInputEditText search;
    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        ClearSort = view.findViewById(R.id.ClearSort);
        NameSort = view.findViewById(R.id.SortByName);
        VoteSort = view.findViewById(R.id.SortByVote);
        progressBar = view.findViewById(R.id.progress_bar);
        tvAlert = view.findViewById(R.id.tv_alert);
        btnRefresh = view.findViewById(R.id.btn_refresh);
        recyclerView = view.findViewById(R.id.recyclerView);
        search = view.findViewById(R.id.et_search);
        setupRecyclerView();
        loadNowPlayingMovies();

        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        movieAdapter = new MovieAdapter();
        recyclerView.setAdapter(movieAdapter);
    }

    private void loadNowPlayingMovies() {
        showLoading();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieService movieService = retrofit.create(MovieService.class);

        Call<MovieResponse> call = movieService.getNowPlayingMovies(API_KEY);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    hideLoading();
                    MovieResponse movieResponse = response.body();
                    List<Movie> movies = movieResponse.getMovies();
                    List<Movie> sortMovies = new ArrayList<>(movies);
                    movieAdapter.setMovies(movies);

                    AtomicBoolean isNameAscending = new AtomicBoolean(true);
                    AtomicBoolean isVoteAscending = new AtomicBoolean(true);

                    VoteSort.setOnClickListener(view1 -> {
                        isNameAscending.set(false);
                        isVoteAscending.set(!isVoteAscending.get());
                        Collections.sort(sortMovies, (movie1, movie2) -> {
                            if (isVoteAscending.get()) {
                                return Double.compare(movie2.getVoteAverage(), movie1.getVoteAverage());
                            } else {
                                return Double.compare(movie1.getVoteAverage(), movie2.getVoteAverage());
                            }
                        });

                        movieAdapter.setMovies(sortMovies);
                    });

                    NameSort.setOnClickListener(view1 -> {
                        isNameAscending.set(!isNameAscending.get());
                        isVoteAscending.set(true);

                        Collections.sort(sortMovies, (movie1, movie2) -> {
                            if (isNameAscending.get()) {
                                return movie1.getTitle().compareToIgnoreCase(movie2.getTitle());
                            } else {
                                return movie2.getTitle().compareToIgnoreCase(movie1.getTitle());
                            }
                        });
                        movieAdapter.setMovies(sortMovies);
                    });

                    ClearSort.setOnClickListener(view1 -> {
                        isNameAscending.set(true);
                        isVoteAscending.set(true);
                        movieAdapter.setMovies(movies);
                    });

                    search.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            // Nothing
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            performSearch(charSequence.toString(), movies);
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            performSearch(editable.toString(), movies);
                        }
                    });


                } else {
                    showAlert();
                    Toast.makeText(getActivity(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performSearch(String querySearch, List<Movie> movies) {
        List<Movie> searchMovie = new ArrayList<>();
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getTitle().contains(querySearch)) {
                searchMovie.add(movies.get(i));
            }
        }
        movieAdapter.setMovies(searchMovie);
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        tvAlert.setVisibility(View.INVISIBLE);
        btnRefresh.setVisibility(View.INVISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        tvAlert.setVisibility(View.INVISIBLE);
        btnRefresh.setVisibility(View.INVISIBLE);
    }

    private void showAlert() {
        tvAlert.setVisibility(View.VISIBLE);
        btnRefresh.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }
}
