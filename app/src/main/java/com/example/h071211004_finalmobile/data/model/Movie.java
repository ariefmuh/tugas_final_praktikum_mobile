package com.example.h071211004_finalmobile.data.model;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import android.os.Parcel;

public class Movie implements Parcelable {

    @SerializedName("genre_id")
    private final int id;

    @SerializedName("overview")
    private final String overview;

    @SerializedName("poster_path")
    private final String posterPath;

    @SerializedName("release_date")
    private final String releaseDate;

    @SerializedName("title")
    private final String title;

    @SerializedName("vote_average")
    private final Double voteAverage;

    @SerializedName("backdrop_path")
    private final String backdropUrl;

    public Movie(int id, String overview, String posterPath, String releaseDate, String title, Double voteAverage, String backdropUrl) {
        this.id = id;
        this.overview = overview;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.title = title;
        this.voteAverage = voteAverage;
        this.backdropUrl = backdropUrl;
    }

    protected Movie(Parcel in) {
        backdropUrl = in.readString();
        id = in.readInt();
        overview = in.readString();
        posterPath = in.readString();
        releaseDate = in.readString();
        title = in.readString();
        voteAverage = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(backdropUrl);
        dest.writeInt(id);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeString(title);
        dest.writeDouble(voteAverage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getBackdropUrl() {
        return backdropUrl;
    }
    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }
}


