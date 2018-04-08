package com.example.mircea.movieapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mircea.movieapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
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
    List<Movie> movieResultsData = MainActivity.mMovieData;
    String textEntered=null;
    private Context mContext;

 /*   Allow the user to tap on a movie poster and transition to a details screen with additional information such as:

    original title
    movie poster image thumbnail
    A plot synopsis (called overview in the api)
    user rating (called vote_average in the api)
    release date*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent intentThatStartedThisAcrivity= getIntent();
        if(intentThatStartedThisAcrivity.hasExtra(Intent.EXTRA_TEXT))
        {
           textEntered = intentThatStartedThisAcrivity.getStringExtra(Intent.EXTRA_TEXT);
           mOriginalTitle.setText(movieResultsData.get(Integer.parseInt(textEntered)).getOriginalTitle());
           mOverview.setText(movieResultsData.get(Integer.parseInt(textEntered)).getOverview());
           mReleaseDate.setText(movieResultsData.get(Integer.parseInt(textEntered)).getReleaseDate());
           mVoteAverage.setText(movieResultsData.get(Integer.parseInt(textEntered)).getVote_average());
            Picasso.with(mContext).load(movieResultsData.get(Integer.parseInt(textEntered)).getMoviePosterImageThumblail())
                    .placeholder(R.drawable.user_placeholder)
                    .error(R.drawable.user_placeholder_error)
                    .into(mImageDetail);


        }
    }
}
