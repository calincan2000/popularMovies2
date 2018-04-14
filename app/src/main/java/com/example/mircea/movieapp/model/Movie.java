package com.example.mircea.movieapp.model;

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

public class Movie {

    private String originalTitle;
    private String id;

    private String moviePosterImageThumblail;
    private String overview;
    private String vote_average;
    private String releaseDate;

    public Movie(String originalTitle, String moviePosterImageThumblail,
                 String overview, String vote_average, String releaseDate,String id) {
        this.originalTitle = originalTitle;
        this.moviePosterImageThumblail = moviePosterImageThumblail;
        this.overview = overview;
        this.vote_average = vote_average;
        this.releaseDate = releaseDate;
        this.id = id;

    }

    public String getOriginalTitle(){return originalTitle;}
    public String getId(){return id;}

    public String getMoviePosterImageThumblail(){return moviePosterImageThumblail;}
    public String getOverview(){return overview;}
    public String getVote_average(){return vote_average;}
    public String getReleaseDate(){return releaseDate;}


}
