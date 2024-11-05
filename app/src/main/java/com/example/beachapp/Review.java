package com.example.beachapp;

import java.util.List;

public class Review {
    private String reviewID;
    private String userID;
    private String beachID;
    private int rating;
    private String text;
    private List<String> pictureUrls;

    public Review() { }

    public Review(String reviewID, String userID, String beachID, int rating, String text, List<String> pictureUrls) {
        this.reviewID = reviewID;
        this.userID = userID;
        this.beachID = beachID;
        this.rating = rating;
        this.text = text;
        this.pictureUrls = pictureUrls;
    }

    public String getReviewID() {
        return reviewID;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getBeachID() {
        return beachID;
    }

    public void setBeachID(String beachID) {
        this.beachID = beachID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        // Optional validation to ensure rating is between 1 and 5
        if (rating < 1) {
            this.rating = 1;
        } else if (rating > 5) {
            this.rating = 5;
        } else {
            this.rating = rating;
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getPictureUrls() {
        return pictureUrls;
    }

    public void setPictureUrls(List<String> pictureUrls) {
        this.pictureUrls = pictureUrls;
    }

    public void addPictureUrl(String url) {
        if (pictureUrls != null) {
            pictureUrls.add(url);
        }
    }

    public void removePictureUrl(String url) {
        if (pictureUrls != null) {
            pictureUrls.remove(url);
        }
    }
}

