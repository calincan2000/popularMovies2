package com.example.mircea.movieapp.model;

/**
 * Created by mircea on 11.04.2018.
 */

public class Trailers {


        private String Trailer;
        private String moviePosterImageThumblail;


        public Trailers(String trailer, String moviePosterImageThumblail) {
            this.Trailer = trailer;
            this.moviePosterImageThumblail = moviePosterImageThumblail;

        }

        public String getTrailer(){return Trailer;}
        public String getMoviePosterImageThumblail(){return moviePosterImageThumblail;}




}
