package com.example.beachapp;

import java.util.ArrayList;
import java.util.List;

public class Beach {
    private String beachID;
    private String name;
    private String accessHours;
    private double latitude;
    private double longitude;
    private String photoUrl;
    private List<String> reviewIDList;
    private List<String> activityTagsIDList;
    private double avgRating;
    private int totalRatings;

    // Default constructor
    public Beach() {
        this.reviewIDList = new ArrayList<>();
        this.activityTagsIDList = new ArrayList<>();
        this.avgRating = 0.0;
        this.totalRatings = 0;
    }

    // Parameterized constructor
    public Beach(String beachID, String name, String accessHours, double latitude, double longitude) {
        this();
        this.beachID = beachID;
        this.name = name;
        this.accessHours = accessHours;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getBeachID() {
        return beachID;
    }

    public String getName() {
        return name;
    }

    public String getAccessHours() {
        return accessHours;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public int getTotalRatings() {
        return totalRatings;
    }

    public List<String> getReviewIDList() {
        return reviewIDList;
    }

    public List<String> getActivityTagsIDList() {
        return activityTagsIDList;
    }

    public void setBeachID(String beachID) {
        this.beachID = beachID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAccessHours(String accessHours) {
        this.accessHours = accessHours;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public void setTotalRatings(int totalRatings) {
        this.totalRatings = totalRatings;
    }

    public void setReviewIDList(List<String> reviewIDList) {
        this.reviewIDList = reviewIDList;
    }

    public void setActivityTagsIDList(List<String> activityTagsIDList) {
        this.activityTagsIDList = activityTagsIDList;
    }

    public void addReviewID(String reviewID) {
        if (reviewIDList == null) {
            reviewIDList = new ArrayList<>();
        }
        reviewIDList.add(reviewID);
    }

    public void removeReviewID(String reviewID) {
        if (reviewIDList != null) {
            reviewIDList.remove(reviewID);
        }
    }

    public void updateAvgRating(int rating) {
        totalRatings += rating;
        if (reviewIDList != null && !reviewIDList.isEmpty()) {
            avgRating = (double) totalRatings / reviewIDList.size();
        } else {
            avgRating = 0.0;
        }
    }

    public void addActivityTagID(String activityTagID) {
        if (activityTagsIDList == null) {
            activityTagsIDList = new ArrayList<>();
        }
        activityTagsIDList.add(activityTagID);
    }

    public void removeActivityTagID(String activityTagID) {
        if (activityTagsIDList != null) {
            activityTagsIDList.remove(activityTagID);
        }
    }
}

