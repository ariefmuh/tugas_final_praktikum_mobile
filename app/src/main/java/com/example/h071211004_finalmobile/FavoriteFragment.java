package com.example.h071211004_finalmobile;


import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.h071211004_finalmobile.data.model.Favorite;
import com.example.h071211004_finalmobile.data.model.Movie;
import com.example.h071211004_finalmobile.database.DatabaseContract;
import com.example.h071211004_finalmobile.database.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;


public class FavoriteFragment extends Fragment {
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextInputEditText search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progress_bar);
        hideLoading();
        search = view.findViewById(R.id.et_search);
        List<Favorite> favoriteList = getAllMoviesFromDatabase();
        recyclerView = view.findViewById(R.id.rv_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FavoriteAdapter favoriteAdapter = new FavoriteAdapter(favoriteList);
        recyclerView.setAdapter(favoriteAdapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                performSearch(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                performSearch(editable.toString());
            }
        });
    }

    private void performSearch(String searchQuery) {
        List<Favorite> favoriteList = getAllMoviesFromDatabase();
        List<Favorite> searchFavorite = new ArrayList<>();
        for (int i = 0; i < favoriteList.size(); i++) {
            if (favoriteList.get(i).getTitle().contains(searchQuery)) {
                searchFavorite.add(favoriteList.get(i));
            }
        }
        FavoriteAdapter favoriteAdapter = new FavoriteAdapter(searchFavorite);
        recyclerView.setAdapter(favoriteAdapter);
    }
    private List<Favorite> getAllMoviesFromDatabase() {
        List<Favorite> favoriteList = new ArrayList<>();
        DatabaseHelper movieHelper = new DatabaseHelper(getActivity());
        Cursor cursor = movieHelper.getAllMovies();

        if (cursor != null && cursor.moveToFirst()) {

            int idColumnIndex = cursor.getColumnIndex(DatabaseContract.DatabaseEntry._ID);
            int titleColumnIndex = cursor.getColumnIndex(DatabaseContract.DatabaseEntry.COLUMN_TITLE);
            int releaseDateColumnIndex = cursor.getColumnIndex(DatabaseContract.DatabaseEntry.COLUMN_RELEASE_DATE);
            int overviewColumnIndex = cursor.getColumnIndex(DatabaseContract.DatabaseEntry.COLUMN_OVERVIEW);
            int posterUrlColumnIndex = cursor.getColumnIndex(DatabaseContract.DatabaseEntry.COLUMN_POSTER_URL);
            int backdropUrlColumnIndex = cursor.getColumnIndex(DatabaseContract.DatabaseEntry.COLUMN_BACKDROP_URL);
            int voteAverageColumnIndex = cursor.getColumnIndex(DatabaseContract.DatabaseEntry.COLUMN_VOTE_AVERAGE);

            do {
                int id = (idColumnIndex != -1) ? cursor.getInt(idColumnIndex) : -1;
                String title = (titleColumnIndex != -1) ? cursor.getString(titleColumnIndex) : null;
                String releaseDate = (releaseDateColumnIndex != -1) ? cursor.getString(releaseDateColumnIndex) : null;
                String overview = (overviewColumnIndex != -1) ? cursor.getString(overviewColumnIndex) : null;
                String posterUrl = (posterUrlColumnIndex != -1) ? cursor.getString(posterUrlColumnIndex) : null;
                String backdropUrl = (backdropUrlColumnIndex != -1) ? cursor.getString(backdropUrlColumnIndex) : null;
                double voteAverage = (voteAverageColumnIndex != -1) ? cursor.getDouble(voteAverageColumnIndex) : 0.0;

                Favorite favorite = new Favorite(id, overview, posterUrl, releaseDate, title, voteAverage, backdropUrl);
                favoriteList.add(favorite);
            } while (cursor.moveToNext());

        }

        if (cursor != null) {
            cursor.close();
        }

        return favoriteList;
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }
    private void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }
}
