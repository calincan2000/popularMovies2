package com.example.mircea.movieapp.utils;

import android.util.Log;

import com.example.mircea.movieapp.model.Movie;
import com.example.mircea.movieapp.model.Review;
import com.example.mircea.movieapp.model.Trailers;

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
    private static final String KEY = "key";
    private static final String URL = "url";
    private static final String CONTENT = "content";


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

            movie = new Movie(original_title, base + poster_path, overview, vote_average, release_date, id);
            movies.add(movie);



        }

        return movies;

    }

    public static List<Trailers> parseTrailersJson(String json) throws JSONException {
        Trailers movie = null;
        List<Trailers> movies = null;
        String base = "http://image.tmdb.org/t/p/w185/";
        movies = new ArrayList<>();

        JSONObject forecastJson = new JSONObject(json);
        JSONArray movieArray = forecastJson.getJSONArray(OWN_LIST);
        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject currentMovie = movieArray.getJSONObject(i);

            String key = currentMovie.optString(KEY);
            String trailers = "https://www.youtube.com/watch?v=" + key;
            String poster_path = "http://img.youtube.com/vi/" + key + "/sddefault.jpg";


            movie = new Trailers(trailers, poster_path);
            movies.add(movie);

        }

        return movies;

    }

    public static String[] getSimpleTrailersStringsFromJson(String forecastJsonStr)
            throws JSONException {


        /* String array to hold each day's weather String */

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray movieArray = forecastJson.getJSONArray(OWN_LIST);
        String[] parsedTrailersData = new String[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject currentMovie = movieArray.getJSONObject(i);
            String key = currentMovie.optString(KEY);
            parsedTrailersData[i] = "https://www.youtube.com/watch?v=" + key;

        }
        return parsedTrailersData;

    }

    public static List<Review> parseReviewsJson(String json)
            throws JSONException {
        Review review = null;
        List<Review> reviews = null;
        String base = "http://image.tmdb.org/t/p/w185/";
        reviews = new ArrayList<>();

        JSONObject forecastJson = new JSONObject(json);
        JSONArray movieArray = forecastJson.getJSONArray(OWN_LIST);
        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject currentMovie = movieArray.getJSONObject(i);
            String content= currentMovie.optString(CONTENT);
            String url = currentMovie.optString(URL);


            review = new Review(content, url);
            reviews.add(review);

        }

        return reviews;
    }

    public static String[] getSimpleReviewsStringsFromJson(String forecastJsonStr)
            throws JSONException {


        /* String array to hold each day's weather String */

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray movieArray = null;
        String[] parsedTrailersData = null;

        if (forecastJson.getJSONArray(OWN_LIST).length() != 0) {
            movieArray = forecastJson.getJSONArray(OWN_LIST);

            parsedTrailersData = new String[movieArray.length()];
            for (int i = 0; i < movieArray.length(); i++) {

                JSONObject currentMovie = movieArray.getJSONObject(i);
                String url = currentMovie.optString(CONTENT);
                parsedTrailersData[i] = url;
                Log.i(LOG, "xxxxxxxxxxxxb1 " + currentMovie.optString(CONTENT));



            }

            return parsedTrailersData;
        } else {
            parsedTrailersData = new String[1];

            parsedTrailersData[0] = " No reviews to show ";
            return parsedTrailersData;

        }
    }

}
