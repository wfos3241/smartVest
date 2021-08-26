package com.example.smartvest;

public class UserVO {
    private float latitude;
    private double longitude;
    private float altitude;

    public UserVO(float latitude,double longitude,  float altitude) {

        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public UserVO( double latitude,double longitude, double altitude) {

        this.latitude = (float)latitude;
        this.longitude = longitude;
        this.altitude = (float)altitude;
    }
    public UserVO() {
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getAltitude() {
        return altitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }
}
