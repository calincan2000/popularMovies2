package com.example.mircea.movieapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import static com.example.mircea.movieapp.data.MovieContract.MovieEntry.TABLE_NAME;
/**
 * Created by mircea on 15.04.2018.
 */

public class MovieContentProvider extends ContentProvider {

    // Define final integer constants for the directory of reviews and a single item.
    // It's convention to use 100, 200, 300, etc for directories,
    // and related ints (101, 102, ..) for items in that directory.
    public static final int CODE_MOVIES = 100;
    public static final int CODE_MOVIES_ID = 101;
    //  Declare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Define a static buildUriMatcher method that associates URI's with their int match
    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, CODE_MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/#", CODE_MOVIES_ID);
        return uriMatcher;

    }

    private MovieDbHelper movieDbHelper;

    @Override
    public boolean onCreate() {
        // [Hint] Declare the DbHelper as a global variable
        Context context = getContext();
        movieDbHelper = new MovieDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        //  Get access to underlying database (read-only for query)
        final SQLiteDatabase db = movieDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        // Query for the tasks directory and write a default case
        Cursor retCursor;
        switch (match) {
            case CODE_MOVIES:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_MOVIES_ID:
                //using selection and selectionArgs
                //URI:content://<authority>/task/#
                String id = uri.getPathSegments().get(1);
                //Selection is the _ID column = ? , and the selection args = the row ID from the URI
                String mSelection = "_id=?";
                String[] mSelectionArgs= new String[]{id};


                // Construct a query as you would normally, passing in the selection/args
                retCursor =  db.query(TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        // Get access to the task database (to write new data to)
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);
        // Insert new values into the database
        // Set the value for the returnedUri and write the default case for unknown URI's
        Uri returnUri;
        switch (match) {
            case CODE_MOVIES:
//                Inserting values into task table
                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
//                  success
                    returnUri= ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI,id);
                } else {
                    throw new SQLException("Faild to insert row into" + uri);
                }
                break;
            //Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);

        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI

        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
        
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get access to the database and write URI matching code to recognize a single item
        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = movieDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        int tasksDeleted;
        // Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            // Query for the tasks directory
            case CODE_MOVIES_ID:
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(TABLE_NAME, "_id=?", new String[]{id});
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        //  Notify the resolver of a change and return the number of items deleted
        if (tasksDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return tasksDeleted;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        //Keep track of if an update occurs
        int tasksUpdated;

        // match code
        int match = sUriMatcher.match(uri);

        switch (match) {
            case CODE_MOVIES_ID:
                //update a single task by getting the id
                String id = uri.getPathSegments().get(1);
                //using selections
                tasksUpdated = movieDbHelper.getWritableDatabase().update(TABLE_NAME, values, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksUpdated != 0) {
            //set notifications if a task was updated
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // return number of tasks updated
        return tasksUpdated;
    }
}
