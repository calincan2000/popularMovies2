package com.example.mircea.movieapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mircea.movieapp.Adapter.MovieAdapter;
import com.example.mircea.movieapp.data.MovieContract;
import com.example.mircea.movieapp.model.Movie;
import com.example.mircea.movieapp.utils.JsonUtils;
import com.example.mircea.movieapp.utils.OpenMovieJsonUtils;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        LoaderCallbacks<Cursor> {
    /*
     * References to RecyclerView and Adapter to reset the list to its
     * "pretty" state when the reset menu item is clicked.
     */


    @BindView(R.id.rv_movies)
    RecyclerView mMoviesList;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    private MovieAdapter mAdapter;
    private static final String LOG = MainActivity.class.getSimpleName();
    private Toast mToast;
    public static ArrayList<Movie> mMovieData = null;
    private static final int FORECAST_LOADER_ID = 0;
    private int mPosition = RecyclerView.NO_POSITION;
    String TopRated = "top_rated";
    String MostPopular = "popular";
    String searchUrl = JsonUtils.buildUrl(TopRated).toString();

    // Create a String array containing the names of the desired data columns from our ContentProvider
    /*
     * The columns of data that we are interested in displaying within our MainActivity's list of
     * weather data.
     */


    public static final String[] MAIN_FORECAST_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_PRIORITY,
    };

    //  Create constant int values representing each column name's position above
    /*
     * We store the indices of the values in the array of Strings above to more quickly be able to
     * access the data from our query. If the order of the Strings above changes, these indices
     * must be adjusted to match the order of the Strings.
     */
    public static final int INDEX_TITLE = 0;
    public static final int INDEX_POSTER_PATH = 1;
    public static final int INDEX_OVERVIEW = 2;
    public static final int INDEX_VOTE_AVERAGE = 3;
    public static final int INDEX_RELEASE_DATE = 4;
    public static final int INDEX_MOVIE_ID = 5;
    public static final int INDEX_PRIORITY = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mMoviesList.setLayoutManager(layoutManager);
        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mMoviesList.setHasFixedSize(true);
        mAdapter = new MovieAdapter(this, new ArrayList<Movie>(), this);
        mMoviesList.setAdapter(mAdapter);
      /*
         * This ID will uniquely identify the Loader. We can use it, for example, to get a handle
         * on our Loader at a later point in time through the support LoaderManager.
         */
        int loaderId = FORECAST_LOADER_ID;
   /*
         * From MainActivity, we have implemented the LoaderCallbacks interface with the type of
         * String array. (implements LoaderCallbacks<String[]>) The variable callback is passed
         * to the call to initLoader below. This means that whenever the loaderManager has
         * something to notify us of, it will do so through this callback.
         */
        //LoaderCallbacks<ArrayList<Movie>> callback = MainActivity.this;
           /*
         * The second parameter of the initLoader method below is a Bundle. Optionally, you can
         * pass a Bundle to initLoader that you can then access from within the onCreateLoader
         * callback. In our case, we don't actually use the Bundle, but it's here in case we wanted
         * to.
         */
        Bundle bundleForLoader = null;

//      Call the showLoading method
        showLoading();

        /*
         * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
         * created and (if the activity/fragment is currently started) starts the loader. Otherwise
         * the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, MainActivity.this);


    }


    //Override onCreateOptionsMenu
    //Use getMenuInflater().inflate to inflate the menu
    //Return true to display this menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public void showJsonData() {
       // mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        //what you want to show
        mMoviesList.setVisibility(View.VISIBLE);
    }



    // Create a method called showLoading that shows the loading indicator and hides the data

    /**
     * This method will make the loading indicator visible and hide the weather View and error
     * message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't need to check whether
     * each view is currently visible or invisible.
     */
    private void showLoading() {
        /* Then, hide the weather data */
        mMoviesList.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(String movieItem) {
        Context context = this;
        //  Toast.makeText(this, movieItem, Toast.LENGTH_LONG).show();
        Class destinationActivity = DetailActivity.class;
        Intent intent = new Intent(context, destinationActivity);
        intent.putExtra(Intent.EXTRA_TEXT, movieItem);
        //intent.putExtra(DetailActivity.EXTRA_POSITION,movieItem);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {


        switch (loaderId) {
//          If the loader requested is our forecast loader, return the appropriate CursorLoader
            case FORECAST_LOADER_ID:
           /* URI for all rows of weather data in our weather table */
                Uri forecastQueryUri = MovieContract.MovieEntry.CONTENT_URI;
                /* Sort order: Ascending by date */
                String sortOrder = MovieContract.MovieEntry.COLUMN_MOVIE_ID + " ASC";


                return new CursorLoader(this,
                        forecastQueryUri,
                        MAIN_FORECAST_PROJECTION,
                        null,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
//      Call mForecastAdapter's swapCursor method and pass in the new Cursor
        mAdapter.swapCursor(data);
//      If mPosition equals RecyclerView.NO_POSITION, set it to 0
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
//      Smooth scroll the RecyclerView to mPosition
        mMoviesList.smoothScrollToPosition(mPosition);

//      If the Cursor's size is not equal to 0, call showWeatherDataView
        if (data.getCount() != 0) showJsonData();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }



    // Override onOptionsItemSelected
    // Within this method, get the ID from the MenuItem
    // If the ID equals R.id.action_refresh, create and set a new adapter on the RecyclerView and return true
    // For now, for all other IDs, return super.onOptionsItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();


        switch (itemId) {
            /*
             * When you click the reset menu item, we want to start all over
             * and display the pretty gradient again. There are a few similar
             * ways of doing this, with this one being the simplest of those
             * ways. (in our humble opinion)
             */

            case R.id.top_rated:
                mMovieData = null;
                searchUrl = JsonUtils.buildUrl(TopRated).toString();
                Log.i(LOG, "xxxxxxxxxxxxb " + searchUrl);

                getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, null, this);
                return true;

            case R.id.most_popular:
                mMovieData = null;
                searchUrl = JsonUtils.buildUrl(MostPopular).toString();
                getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, null, this);
                return true;

        }


        return super.onOptionsItemSelected(item);
    }


}
