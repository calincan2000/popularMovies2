package com.example.mircea.movieapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mircea on 15.04.2018.
 */

public class MovieContract {

    /*  Add content provider constants to the Contract
     Clients need to know how to access the task data, and it's your job to provide
     these content URI's for the path to that data:
        1) Content authority,
        2) Base content URI,
        3) Path(s) to the tasks directory
        4) Content URI for data in the MovieEntry class
      */

    /*
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website. A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * Play Store.
     */
    public static final String AUTHORITY = "com.example.mircea.movieapp";
    /*
     * Use AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    /*
    * Possible paths that can be appended to BASE_CONTENT_URI to form valid URI's
    * can handle. For instance,
    *
    *     content://com.example.android.sunshine/weather/
    *     [           BASE_CONTENT_URI         ][ PATH_WEATHER ]
    *
    * is a valid path for looking at weather data.
    *
    *      content://com.example.android.sunshine/givemeroot/
    *
    * will fail, as the ContentProvider hasn't been given any information on what to do with
    * "givemeroot". At least, let's hope not. Don't be that dev, reader. Don't be that dev.
    */
    public static final String PATH_MOVIES = "reviews";


    /* MovieEntry is an inner class that defines the contents of the Movie table */
    public static final class MovieEntry implements BaseColumns {
        /* The base CONTENT_URI used to query the reviews table from the content provider */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon()
                        .appendPath(PATH_MOVIES)
                        .build();
        // Task table and column names
        /* Used internally as the name of our reviews table. */
        public static final String TABLE_NAME = "reviews";

        // Since TaskEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
  /* public Movie(String originalTitle, String moviePosterImageThumblail,
                 String overview, String vote_average, String releaseDate,String id) {*/
        public static final String COLUMN_TITLE = "originalTitle";                          // Type: TEXT
        public static final String COLUMN_POSTER_PATH = "moviePosterImageThumblail";        // Type: TEXT
        public static final String COLUMN_OVERVIEW = "overview";                            // Type: TEXT
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";                    // Type: TEXT
        public static final String COLUMN_RELEASE_DATE = "releaseDate";                     // Type: TEXT
        public static final String COLUMN_MOVIE_ID = "id";                     // Type: TEXT
        public static final String COLUMN_PRIORITY = "fav";                                 // Type: TEXT


        /*
        The above table structure looks something like the sample table below.
        With the name of the table and columns on top, and potential contents in rows

        Note: Because this implements BaseColumns, the _id column is generated automatically

        reviews
         - - - - - - - - - - - - - - - - - - - - - -
        | _id  |    key     |    fav   |
         - - - - - - - - - - - - - - - - - - - - - -
        |  1   |   389      |      1   |
         - - - - - - - - - - - - - - - - - - - - - -
        |  2   |   539      |      1   |
         - - - - - - - - - - - - - - - - - - - - - -
        .
        .
        .
         - - - - - - - - - - - - - - - - - - - - - -
        | 43   |   13       |      1   |
         - - - - - - - - - - - - - - - - - - - - - -

         */

    }

}
