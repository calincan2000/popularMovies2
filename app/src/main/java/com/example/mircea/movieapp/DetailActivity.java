package com.example.mircea.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mircea.movieapp.Adapter.MovieAdapter;
import com.example.mircea.movieapp.Adapter.ReviewsAdapter;
import com.example.mircea.movieapp.Adapter.TrailerAdapter;
import com.example.mircea.movieapp.model.Movie;
import com.example.mircea.movieapp.model.Review;
import com.example.mircea.movieapp.model.Trailers;
import com.example.mircea.movieapp.utils.JsonUtils;
import com.example.mircea.movieapp.utils.OpenMovieJsonUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.mircea.movieapp.data.MovieContract;

public class DetailActivity extends AppCompatActivity
        implements TrailerAdapter.TrailerAdapterOnClickHandler, ReviewsAdapter.ReviewAdapterOnClickHandler {

    @BindView(R.id.image_iv)
    ImageView mImageDetail;
    @BindView(R.id.original_title)
    TextView mOriginalTitle;
    @BindView(R.id.overview)
    TextView mOverview;
    @BindView(R.id.vote_average)
    TextView mVoteAverage;
    @BindView(R.id.release_date)
    TextView mReleaseDate;
    @BindView(R.id.trailers)
    RecyclerView mTrailers;
    @BindView(R.id.reviews)
    TextView mReviews;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private TrailerAdapter mAdapter;
    private static final String LOG = MovieAdapter.class.getSimpleName();
    public List<Movie> movieResultsData = MainActivity.mMovieData;
    public static ArrayList<Trailers> mTrailersData = null;
    public static ArrayList<Review> mReviewsData = null;

    public String textEntered = null;
    private Context mContext;
    String idx = null;
    private int mPriority = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        fab.setImageResource(R.mipmap.ic_my_icon2);
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            textEntered = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);

            mOriginalTitle.setText(movieResultsData.get(Integer.parseInt(textEntered)).getOriginalTitle());
            mOverview.setText(movieResultsData.get(Integer.parseInt(textEntered)).getOverview());
            mReleaseDate.setText(movieResultsData.get(Integer.parseInt(textEntered)).getReleaseDate());
            mVoteAverage.setText(movieResultsData.get(Integer.parseInt(textEntered)).getVote_average());

            Picasso.get()
                    .load(movieResultsData.get(Integer.parseInt(textEntered)).getMoviePosterImageThumblail())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.user_placeholder)
                    .into(mImageDetail, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            // Try again online, if cache loading failed
                            Picasso.get()
                                    .load(movieResultsData.get(Integer.parseInt(textEntered)).getMoviePosterImageThumblail())
                                    .placeholder(R.drawable.user_placeholder)
                                    .error(R.drawable.user_placeholder_error)
                                    .into(mImageDetail);
                        }
                    });
            idx = movieResultsData.get(Integer.parseInt(textEntered)).getId();
            Log.i(LOG, "xxxxxxxxxxxxYYYYZZZZBBB " + idx);

            LinearLayoutManager layoutManager =
                    new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
            mTrailers.setLayoutManager(layoutManager);


            mTrailers.setHasFixedSize(true);
            mAdapter = new TrailerAdapter(DetailActivity.this, new ArrayList<Trailers>(), DetailActivity.this);
            mTrailers.setAdapter(mAdapter);


            getSupportLoaderManager().initLoader(0, null, TrailerLoaderListener);
            getSupportLoaderManager().initLoader(1, null, ReviewsResultLoaderListener);


        }
    }

    private LoaderManager.LoaderCallbacks<String[]> ReviewsResultLoaderListener
            = new LoaderManager.LoaderCallbacks<String[]>() {
        @Override
        public Loader<String[]> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<String[]>(DetailActivity.this) {
                String[] mReviewsData = null;

                @Override
                protected void onStartLoading() {
                    if (mReviewsData != null) {
                        deliverResult(mReviewsData);
                    } else {
                        // mLoadingIndicator.setVisibility(View.VISIBLE);
                        forceLoad();
                    }
                }

                @Override
                public String[] loadInBackground() {
                    URL ReviewsRequestUrl = JsonUtils.createUrl(JsonUtils.buildUrl(idx + "/reviews").toString());
                    String ReviewsSearchResults = null;


                    try {

                        ReviewsSearchResults = JsonUtils
                                .getResponseFromHttpUrl(ReviewsRequestUrl);

                        String[] ReviewsResultData = OpenMovieJsonUtils.getSimpleReviewsStringsFromJson(ReviewsSearchResults);

                        return ReviewsResultData;

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

                public void deliverResult(String[] data) {
                    mReviewsData = data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<String[]> loader, String[] data) {

            for (String datad : data) {
                mReviews.append(datad + "\n\n\n");
            }
        }

        @Override
        public void onLoaderReset(Loader<String[]> loader) {

        }
    };
    private LoaderManager.LoaderCallbacks<ArrayList<Trailers>> TrailerLoaderListener
            = new LoaderManager.LoaderCallbacks<ArrayList<Trailers>>() {

        @Override
        public Loader<ArrayList<Trailers>> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<ArrayList<Trailers>>(DetailActivity.this) {

                @Override
                protected void onStartLoading() {
                 /*   if (mTrailersData != null) {
                        mTrailersData=null;
                    } else {
                        // mLoadingIndicator.setVisibility(View.VISIBLE);*/
                    forceLoad();

                }

                @Override
                public ArrayList<Trailers> loadInBackground() {
                    URL TrailerRequestUrl = JsonUtils.createUrl(JsonUtils.buildUrl(idx + "/videos").toString());
                    String trailerSearchResults = null;
                    ArrayList<Trailers> trailerResultData = new ArrayList<>();

                    try {
                        trailerSearchResults = JsonUtils
                                .getResponseFromHttpUrl(TrailerRequestUrl);
                        trailerResultData.addAll(OpenMovieJsonUtils.parseTrailersJson(trailerSearchResults));

                        return trailerResultData;

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                public void deliverResult(ArrayList<Trailers> data) {
                    mTrailersData = data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Trailers>> loader, ArrayList<Trailers> data) {
            mAdapter.setTrailersData(data);


        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Trailers>> loader) {

        }
    };
    private LoaderManager.LoaderCallbacks<Cursor> QueryLoaderListener
            = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<Cursor>(DetailActivity.this) {
                // Initialize a Cursor, this will hold all the task data
                Cursor mMovieData = null;

                // onStartLoading() is called when a loader first starts loading data
                @Override
                protected void onStartLoading() {
                    if (mMovieData != null) {
                        // Delivers any previously loaded data immediately
                        deliverResult(mMovieData);
                    } else {
                        // Force a new load
                        forceLoad();
                    }
                }
                // loadInBackground() performs asynchronous loading of data
                @Override
                public Cursor loadInBackground() {
                    // Will implement to load data

                    // Query and load all task data in the background; sort by priority
                    // [Hint] use a try/catch block to catch any errors in loading data
                    try {
                        return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                MovieContract.MovieEntry.COLUMN_PRIORITY);
                    } catch (Exception e) {
                        Log.i(LOG, "Failed to asynchronusly load data.");
                        e.printStackTrace();
                        return null;
                    }
                }
                // deliverResult sends the result of the load, a Cursor, to the registered listener
                public void deliverResult(Cursor data) {
                    mMovieData = data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    @Override
    public void onClick(String TrailerItem) {

        Context context = this;


        Log.i(LOG, "xxxxxxxxxxxxb " + mTrailersData.get(Integer.parseInt(TrailerItem)).getTrailer());

        // Convert the String URL into a URI object (to pass into the Intent constructor)
        Uri resultUri = Uri.parse(mTrailersData.get(Integer.parseInt(TrailerItem)).getTrailer());

        // Create a new intent to view the Trailer URI
        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, resultUri);

        // Send the intent to launch a new activity
        startActivity(websiteIntent);


    }

    /**
     * onClickAddTask is called when the "ADD" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    public void onClickAddTask(View view) {

        // Insert new Movie data via a ContentResolver

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUMN_PRIORITY, mPriority);
        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

        // Display the URI that's returned with a Toast
        // [Hint] Don't forget to call finish() to return to MainActivity after this insert is complete
        if (uri != null) {
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }

        // Finish activity (this returns back to MainActivity)
        //finish();
    }


}
