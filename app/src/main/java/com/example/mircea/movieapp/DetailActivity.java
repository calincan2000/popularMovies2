package com.example.mircea.movieapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mircea.movieapp.model.Movie;
import com.example.mircea.movieapp.utils.JsonUtils;
import com.example.mircea.movieapp.utils.OpenMovieJsonUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity{

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

    private static final String LOG = MovieAdapter.class.getSimpleName();
    public List<Movie> movieResultsData = MainActivity.mMovieData;
    public String textEntered = null;
    private Context mContext;
    String idx = null;
    /*   @BindView(R.id.pb_loading_indicator)
       ProgressBar mLoadingIndicator;*/
    String searchUrl;

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
            Picasso.with(mContext).load(movieResultsData.get(Integer.parseInt(textEntered)).getMoviePosterImageThumblail())
                    .placeholder(R.drawable.user_placeholder)
                    .error(R.drawable.user_placeholder_error)
                    .into(mImageDetail);
            idx = movieResultsData.get(Integer.parseInt(textEntered)).getId();
            Log.i(LOG, "xxxxxxxxxxxxbid " + idx);

            getSupportLoaderManager().initLoader(0,null,TrailerResultLoaderListener);
            getSupportLoaderManager().initLoader(1,null,ReviewsResultLoaderListener);


        }
    }



    private LoaderManager.LoaderCallbacks<String[]> TrailerResultLoaderListener
            = new LoaderManager.LoaderCallbacks<String[]>() {

        @Override
        public Loader<String[]> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<String[]>(DetailActivity.this) {
                String[] mTrailerData = null;

                @Override
                protected void onStartLoading() {
                    if (mTrailerData != null) {
                        deliverResult(mTrailerData);
                    } else {
                        // mLoadingIndicator.setVisibility(View.VISIBLE);
                        forceLoad();
                    }
                }

                @Override
                public String[] loadInBackground() {
                    URL TrailerRequestUrl = JsonUtils.createUrl(JsonUtils.buildUrl(idx+"/videos").toString());
                    String TrailerSearchResults = null;


                    try {

                        TrailerSearchResults = JsonUtils
                                .getResponseFromHttpUrl(TrailerRequestUrl);

                        String[] TrailerResultData = OpenMovieJsonUtils.getSimpleTrailersStringsFromJson(TrailerSearchResults);

                        return TrailerResultData;

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
                    mTrailerData = data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<String[]> loader, String[] data) {
            Log.i(LOG, "xxxxxxxxxxxxb2 " + data[0].toString());
        }

        @Override
        public void onLoaderReset(Loader<String[]> loader) {

        }
    };
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
                    URL ReviewsRequestUrl = JsonUtils.createUrl(JsonUtils.buildUrl(idx+"/reviews").toString());
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
            Log.i(LOG, "xxxxxxxxxxxxb1 " + data[0].toString());
        }

        @Override
        public void onLoaderReset(Loader<String[]> loader) {

        }
    };


}
