package com.example.mircea.movieapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.mircea.movieapp.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mircea on 17.03.2018.
 */


public final class OpenMovieJsonUtils {
    private static final String LOG = OpenMovieJsonUtils.class.getSimpleName();
    private static final String OWN_LIST = "results";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String POSTER_PATH = "poster_path";
    private static final String OVERVIEW = "overview";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String RELEASE_DATE = "release_date";
    private static final String ID = "id";

    public static List<Movie> parseMovieJson(String json) throws JSONException {
        Movie movie = null;
        List<Movie> movies = null;
        String base = "http://image.tmdb.org/t/p/w185/";
        movies = new ArrayList<>();

        JSONObject forecastJson = new JSONObject(json);
        JSONArray movieArray = forecastJson.getJSONArray(OWN_LIST);
        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject currentMovie = movieArray.getJSONObject(i);

            String original_title = currentMovie.optString(ORIGINAL_TITLE);
            String poster_path = currentMovie.optString(POSTER_PATH);
            String overview = currentMovie.optString(OVERVIEW);
            String vote_average = currentMovie.optString(VOTE_AVERAGE);
            String release_date = currentMovie.optString(RELEASE_DATE);
            String id = currentMovie.optString(ID);

            movie = new Movie(original_title, base + poster_path, overview, vote_average, release_date,id);
            movies.add(movie);

        }

        return movies;

    }
}
