package com.aimplatfarm.aimplatfarmdelivery.map;

/**
 * Created by nishant on 4/5/19.
 */

public class MyLocationBean {

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    double longitude;
    double latitude;
}
