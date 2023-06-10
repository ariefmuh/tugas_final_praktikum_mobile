package com.example.h071211004_finalmobile.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Tv implements Parcelable {
    @SerializedName("backdrop_path")
    private final String backdropUrl;

    @SerializedName("first_air_date")
    private final String firstAirDate;

    @SerializedName("id")
    private final int id;

    @SerializedName("name")
    private final String name;

    @SerializedName("overview")
    private final String overview;

    @SerializedName("poster_path")
    private final String posterUrl;

    @SerializedName("vote_average")
    private final Double voteAverage;

    public Tv(String backdropUrl, String firstAirDate, List<Integer> genreIds, int id, String name, String overview,
              String posterUrl, Double voteAverage) {
        this.backdropUrl = backdropUrl;
        this.firstAirDate = firstAirDate;
        this.id = id;
        this.name = name;
        this.overview = overview;
        this.posterUrl = posterUrl;
        this.voteAverage = voteAverage;
    }

    protected Tv(Parcel in) {
        backdropUrl = in.readString();
        firstAirDate = in.readString();
        id = in.readInt();
        name = in.readString();
        overview = in.readString();
        posterUrl = in.readString();
        voteAverage = in.readDouble();
    }

    public static final Creator<Tv> CREATOR = new Creator<Tv>() {
        @Override
        public Tv createFromParcel(Parcel in) {
            return new Tv(in);
        }

        @Override
        public Tv[] newArray(int size) {
            return new Tv[size];
        }
    };

    public String getBackdropUrl() {
        return backdropUrl;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(backdropUrl);
        dest.writeString(firstAirDate);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(overview);
        dest.writeString(posterUrl);
        dest.writeDouble(voteAverage);
    }
}
