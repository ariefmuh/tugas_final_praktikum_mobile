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
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.h071211004_finalmobile.data.model.Favorite;
import com.example.h071211004_finalmobile.database.DatabaseContract;
import com.example.h071211004_finalmobile.database.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class FavoriteFragment extends Fragment {
    RecyclerView recyclerView;
    Button ClearSort, NameSort, VoteSort;
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

        ClearSort = view.findViewById(R.id.ClearSort);
        NameSort = view.findViewById(R.id.SortByName);
        VoteSort = view.findViewById(R.id.SortByVote);
        List<Favorite> favoriteList = getAllMoviesFromDatabase();


        recyclerView = view.findViewById(R.id.rv_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FavoriteAdapter favoriteAdapter = new FavoriteAdapter(favoriteList);
        recyclerView.setAdapter(favoriteAdapter);

        // WTF IS THIS IN CHATGPT ???
        // Using it because Apparently can't use boolean in here so using atomicBoolean solve it
        // Really though i just want to add this for ascending and descending reason if its just ascending it was okay
        AtomicBoolean isNameAscending = new AtomicBoolean(true);
        AtomicBoolean isVoteAscending = new AtomicBoolean(true);

        VoteSort.setOnClickListener(view1 -> {
            isNameAscending.set(!isNameAscending.get()); // THE PROBLEM WHY USING ATOMIC BOOLEAN
            isVoteAscending.set(true);

            Collections.sort(favoriteList, new Comparator<Favorite>() {
                @Override
                public int compare(Favorite favorite1, Favorite favorite2) {
                    if (isNameAscending.get()) {
                        return Double.compare(favorite1.getVoteAverage(), favorite2.getVoteAverage());
                    } else {
                        return Double.compare(favorite2.getVoteAverage(), favorite1.getVoteAverage());
                    }
                }
            });

            FavoriteAdapter adapter = new FavoriteAdapter(favoriteList);
            recyclerView.setAdapter(adapter);
        });

        NameSort.setOnClickListener(view1 -> {
            isNameAscending.set(!isNameAscending.get());
            isVoteAscending.set(true);
            Collections.sort(favoriteList, (favorite1, favorite2) -> {
                if (isNameAscending.get()) {
                    return favorite1.getTitle().compareToIgnoreCase(favorite2.getTitle());
                } else {
                    return favorite2.getTitle().compareToIgnoreCase(favorite1.getTitle());
                }
            });

            FavoriteAdapter adapter = new FavoriteAdapter(favoriteList);
            recyclerView.setAdapter(adapter);
        });

        ClearSort.setOnClickListener(view1 -> {
            List<Favorite> testList = getAllMoviesFromDatabase();
            FavoriteAdapter adapter = new FavoriteAdapter(testList);
            recyclerView.setAdapter(adapter);
        });

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

    private void sortFavoritesByName(List<Favorite> favorites) {
        Collections.sort(favorites, new Comparator<Favorite>() {
            @Override
            public int compare(Favorite favorite1, Favorite favorite2) {
                return favorite1.getTitle().compareToIgnoreCase(favorite2.getTitle());
            }
        });
    }

    private void sortFavoritesByVoteAverage(List<Favorite> favorites) {
        Collections.sort(favorites, new Comparator<Favorite>() {
            @Override
            public int compare(Favorite favorite1, Favorite favorite2) {
                return Double.compare(favorite2.getVoteAverage(), favorite1.getVoteAverage());
            }
        });
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }
    private void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }
}
