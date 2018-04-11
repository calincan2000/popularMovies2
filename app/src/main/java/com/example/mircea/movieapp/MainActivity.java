package com.example.mircea.movieapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.AsyncTaskLoader;
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
import com.example.mircea.movieapp.model.Movie;
import com.example.mircea.movieapp.utils.JsonUtils;
import com.example.mircea.movieapp.utils.OpenMovieJsonUtils;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        LoaderCallbacks<ArrayList<Movie>> {
    /*
     * References to RecyclerView and Adapter to reset the list to its
     * "pretty" state when the reset menu item is clicked.
     */
    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageDisplay;

    @BindView(R.id.rv_movies)
    RecyclerView mMoviesList;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    private MovieAdapter mAdapter;
    private static final String LOG = MainActivity.class.getSimpleName();
    private Toast mToast;
    public static ArrayList<Movie> mMovieData = null;
    private static final int FORECAST_LOADER_ID = 0;

    String TopRated = "top_rated";
    String MostPopular = "popular";
    String searchUrl = JsonUtils.buildUrl(TopRated).toString();

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
        LoaderCallbacks<ArrayList<Movie>> callback = MainActivity.this;
           /*
         * The second parameter of the initLoader method below is a Bundle. Optionally, you can
         * pass a Bundle to initLoader that you can then access from within the onCreateLoader
         * callback. In our case, we don't actually use the Bundle, but it's here in case we wanted
         * to.
         */
        Bundle bundleForLoader = null;

        /*
         * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
         * created and (if the activity/fragment is currently started) starts the loader. Otherwise
         * the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);


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
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        //what you want to show
        mMoviesList.setVisibility(View.VISIBLE);
    }

    public void showErrorMessage() {
        // Then, show the error
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mMoviesList.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(String movieItem) {
        Context context = this;
        //  Toast.makeText(this, movieItem, Toast.LENGTH_LONG).show();

        Log.i(LOG, "xxxxxxxxxxxxb " + movieItem);

        Class destinationActivity = DetailActivity.class;
        Intent intent = new Intent(context, destinationActivity);
        intent.putExtra(Intent.EXTRA_TEXT, movieItem);
        //intent.putExtra(DetailActivity.EXTRA_POSITION,movieItem);
        startActivity(intent);
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Movie>>(this) {

            /* This array will hold and help cache our movie data */
            //ArrayList<Movie> mMovieData = null;

            /**
             * Subclasses of AsyncTaskLoader must implement this to take care of loading their data.
             */

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }

            }

            /**
             * This is the method of the AsyncTaskLoader that will load and parse the JSON data
             *  in the background.
             *
             * @return Movie
             *         null if an error occurs
             */

            @Override
            public ArrayList<Movie> loadInBackground() {


                URL movieRequestUrl = JsonUtils.createUrl(searchUrl);
                String movieSearchResults = null;
                ArrayList<Movie> movieResultData = new ArrayList<>();

                try {
                    movieSearchResults = JsonUtils
                            .getResponseFromHttpUrl(movieRequestUrl);
                    movieResultData.addAll(OpenMovieJsonUtils.parseMovieJson(movieSearchResults));

                    return movieResultData;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            /**
             * Sends the result of the load to the registered listener.
             *
             * @param data The result of the load
             */
            public void deliverResult(ArrayList<Movie> data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mAdapter.setMovieData(data);


        if (data != null) {
            showJsonData();
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
   /*
         * We aren't using this method in our example application, but we are required to Override
         * it to implement the LoaderCallbacks<ArrayList<Movie>> interface
         */
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
           /* case R.id.action_refresh:
                mMoviesList.setAdapter(mAdapter);
                return true;*/
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
