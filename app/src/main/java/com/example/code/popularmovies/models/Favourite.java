package com.example.code.popularmovies.models;

import com.orm.SugarRecord;

/**
 * Created by code on 2016-03-24.
 */
public class Favourite extends SugarRecord {
    public String id;
    public String title;
    public String posterPath;
    public String releaseDate;
    public Double populairty;
    public Double voteAverage;
    public String plotSynopsis;
    public Boolean favourited;
    public String youtubeKey; //Incase user has already played the trailer.

    public Favourite() {
    }

    public Favourite(String id, String title, String posterPath, String releaseDate, Double populairty,
                     Double voteAverage, String plotSynopsis, String youtubeKey, Boolean favourited) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.populairty = populairty;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
        this.youtubeKey = youtubeKey;
        this.favourited = favourited;
        this.posterPath = posterPath;
    }
}
