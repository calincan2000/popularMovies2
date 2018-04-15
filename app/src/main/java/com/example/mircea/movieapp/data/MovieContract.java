package com.example.mircea.movieapp.data;

import android.provider.BaseColumns;

/**
 * Created by mircea on 15.04.2018.
 */

public class MovieContract {
      /* MovieEntry is an inner class that defines the contents of the Movie table */
      public static final class MovieEntry implements BaseColumns{
          // Task table and column names
          public static final String TABLE_NAME = "reviews";

          // Since TaskEntry implements the interface "BaseColumns", it has an automatically produced
          // "_ID" column in addition to the two below
          public static final String COLUMN_DESCRIPTION = "description";
          public static final String COLUMN_PRIORITY = "priority";


        /*
        The above table structure looks something like the sample table below.
        With the name of the table and columns on top, and potential contents in rows

        Note: Because this implements BaseColumns, the _id column is generated automatically

        tasks
         - - - - - - - - - - - - - - - - - - - - - -
        | _id  |    description     |    priority   |
         - - - - - - - - - - - - - - - - - - - - - -
        |  1   |  Complete lesson   |       1       |
         - - - - - - - - - - - - - - - - - - - - - -
        |  2   |    Go shopping     |       3       |
         - - - - - - - - - - - - - - - - - - - - - -
        .
        .
        .
         - - - - - - - - - - - - - - - - - - - - - -
        | 43   |   Learn guitar     |       2       |
         - - - - - - - - - - - - - - - - - - - - - -

         */

      }

}
