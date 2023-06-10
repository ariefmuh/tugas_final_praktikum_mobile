package com.example.h071211004_finalmobile;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.h071211004_finalmobile.data.TvShowService;
import com.example.h071211004_finalmobile.data.model.Tv;
import com.example.h071211004_finalmobile.data.model.Tv;
import com.example.h071211004_finalmobile.data.model.TvResponse;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TvShowFragment extends Fragment {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY = "dad1cd55d3f6d09536f1c6bde1fe8d07";
    private ProgressBar progressBar;
    private TextView tvAlert;
    private ImageView btnRefresh;
    private RecyclerView recyclerView;
    private TvShowAdapter tvAdapter;
    private TextInputEditText search;
    private Button ClearSort, NameSort, VoteSort;


    public TvShowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv_shows, container, false);
        initializeViews(view);
        showLoading();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TvShowService tvShowService = retrofit.create(TvShowService.class);

        Call<TvResponse> call = tvShowService.getAiringTodayTV(API_KEY);
        call.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                if (response.isSuccessful()) {
                    hideLoading();
                    TvResponse tvResponse = response.body();
                    List<Tv> tvShows = tvResponse.getTvShows();
                    List<Tv> sortTvs = tvShows;
                    setTvShows(tvShows);

                    NameSort.setOnClickListener(view1 -> {
                        Collections.sort(sortTvs, new Comparator<Tv>() {
                            @Override
                            public int compare(Tv favorite1, Tv favorite2) {
                                return favorite1.getName().compareToIgnoreCase(favorite2.getName());
                            }
                        });
                        setTvShows(sortTvs);
                    });

                    VoteSort.setOnClickListener(view1 -> {
                        Collections.sort(sortTvs, new Comparator<Tv>() {
                            @Override
                            public int compare(Tv favorite1, Tv favorite2) {
                                return Double.compare(favorite2.getVoteAverage(), favorite1.getVoteAverage());
                            }
                        });
                        setTvShows(sortTvs);
                    });

                    ClearSort.setOnClickListener(view1 -> {
                        setTvShows(tvShows);
                    });

                    search.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            // Nothing
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            performSearch(charSequence.toString(), tvShows);
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            performSearch(editable.toString(), tvShows);
                        }
                    });
                } else {
                    showAlert();
                    Toast.makeText(getActivity(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    private void performSearch(String querySearch, List<Tv> tvShow) {
        List<Tv> searchTv = new ArrayList<>();
        for (int i = 0; i < tvShow.size(); i++) {
            if (tvShow.get(i).getName().contains(querySearch)) {
                searchTv.add(tvShow.get(i));
            }
        }
        setTvShows(searchTv);
    }

    private void initializeViews(View view) {
        ClearSort = view.findViewById(R.id.ClearSort);
        NameSort = view.findViewById(R.id.SortByName);
        VoteSort = view.findViewById(R.id.SortByVote);
        search = view.findViewById(R.id.et_search);
        progressBar = view.findViewById(R.id.progress_bar);
        tvAlert = view.findViewById(R.id.tv_alert);
        btnRefresh = view.findViewById(R.id.btn_refresh);
        recyclerView = view.findViewById(R.id.rv_tv_shows);
    }

    private void setTvShows(List<Tv> tvShows) {
        tvAdapter = new TvShowAdapter(tvShows);
        recyclerView.setAdapter(tvAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
