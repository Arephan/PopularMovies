package com.example.code.popularmovies.services;

import com.example.code.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieParseJSON {

    public static final String JSON_ARRAY = "results";
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_PAGE = "page";
    public static final String KEY_POSTER_PATH = "poster_path";
    public static final String KEY_OVERVIEW = "overview";
    public static final String KEY_RELEASE_DATE = "release_date";
    public static final String KEY_VOTE_AVERAGE = "vote_average";
    public static final String KEY_POPULARITY = "popularity";
    public static final Boolean FAVOURITED_DEFAULT = false;
    public ArrayList<Movie> movieList = new ArrayList<Movie>();
    public Integer page;
    private JSONArray results = null;
    private String json;

    //TODO: Make new ParseJSON for reviews
    public MovieParseJSON(String json) {
        this.json = json;
    }

    public void movieParseJSON() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);

            //Check if this is review response


            //Check if JSON response contains youtube key, if it does, then send intent to play
            results = jsonObject.getJSONArray(JSON_ARRAY);
            // youtubeKey = jsonObject.getString(KEY_YOUTUBE);
            //Check if this request of was for trailer

            page = jsonObject.getInt(KEY_PAGE);

            for (int i = 0; i < results.length(); i++) {
                JSONObject jo = results.getJSONObject(i);
                Movie movie = new Movie(
                        jo.getLong(KEY_ID),
                        jo.getString(KEY_TITLE),
                        jo.getString(KEY_POSTER_PATH),
                        jo.getDouble(KEY_VOTE_AVERAGE),
                        jo.getString(KEY_RELEASE_DATE),
                        jo.getString(KEY_OVERVIEW),
                        jo.getDouble(KEY_POPULARITY),
                        FAVOURITED_DEFAULT

                );

                movieList.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
