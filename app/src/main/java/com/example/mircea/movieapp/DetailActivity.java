package com.example.mircea.movieapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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

    private TrailerAdapter mAdapter;
    private static final String LOG = MovieAdapter.class.getSimpleName();
    public List<Movie> movieResultsData = MainActivity.mMovieData;
    public static ArrayList<Trailers> mTrailersData = null;
    public static ArrayList<Review> mReviewsData = null;

    public String textEntered = null;
    private Context mContext;
    String idx = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
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


}
