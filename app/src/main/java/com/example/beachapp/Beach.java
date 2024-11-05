package com.example.beachapp;

import java.util.ArrayList;
import java.util.List;

public class Beach {
    private String beachID;
    private String name;
    private String accessHours;
    private double latitude;
    private double longitude;
    private List<String> reviewIDList;
    public Beach() { }
    public Beach(String beachID, String name, String accessHours, double latitude, double longitude){
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
    public List<String> getReviewIDList() {
        return reviewIDList;
    }
    public void setReviewIDList(List<String> reviewIDList) {
        this.reviewIDList = reviewIDList;
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
}
