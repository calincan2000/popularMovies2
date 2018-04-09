package com.example.mircea.movieapp.utils;

import android.net.Uri;
import android.util.Log;

import com.example.mircea.movieapp.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by mircea on 11.03.2018.
 * Your app will:
 * +Upon launch, present the user with an grid arrangement of movie posters.
 * <p>
 * <p>
 * +Allow your user to change sort order via a setting. The sort order can be by:
 * a)most popular,
 * b)or by top rated
 * <p>
 * +Allow the user to tap on a movie poster and transition to a details screen with additional information such as:
 * 1)original title
 * 2)movie poster image thumbnail
 * 3)A plot synopsis (called overview in the api)
 * 4)user rating (called vote_average in the api)
 * 5)release date
 */

public class JsonUtils {
    private static final String LOG = JsonUtils.class.getSimpleName();

    /**
     * @param popularSearchQuery The keyword that will be queried for.
     * @return The URL to use to query the weather server.
     */

    final static String themoviedb_BASE_URL = "https://api.themoviedb.org/3/movie";
    final static String api = "api_key";
    final static String apiVal = "b2e0baf71e0fed285d22bfc99af07c68";
final static String trailer_BASE_URL ="238/videos?api_key=b2e0baf71e0fed285d22bfc99af07c68";
    final static String review_BASE_URL ="238/reviews?api_key=b2e0baf71e0fed285d22bfc99af07c68";

    public static URL buildUrl(String PopularMovie) {
          Uri builtUri = Uri.parse(themoviedb_BASE_URL).buildUpon()
                .appendEncodedPath(PopularMovie)
                .appendQueryParameter(api, apiVal)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.i(LOG, "Built URI zzzzzzzzz " + url);

        return url;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
