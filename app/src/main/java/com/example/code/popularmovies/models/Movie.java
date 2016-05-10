package com.example.code.popularmovies.models;

import com.orm.SugarRecord;

/**
 * Created by code on 2016-02-21.
 */
public class Movie extends SugarRecord {
    public Long id;
    public String title;
    public String posterPath;
    public Double voteAverage;
    public String releaseDate;
    public String plotSynopsis;
    public Double popularity;
    public Boolean favourited;

    public Movie() {
    }


    public Movie(Long id, String title, String posterPath, Double voteAverage, String releaseDate, String plotSynopsis, Double popularity, Boolean favourited) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.plotSynopsis = plotSynopsis;
        this.popularity = popularity;
        this.favourited = favourited;
    }

}
