package com.example.mircea.movieapp.model;

/**
 * Created by mircea on 11.04.2018.
 */

public class Review {


        private String Review;
        private String mUrl;


        public Review(String review, String url) {
            this.Review = review;
            this.mUrl = url;

        }

        public String getReview(){return Review;}
        public String getUrl(){return mUrl;}




}
