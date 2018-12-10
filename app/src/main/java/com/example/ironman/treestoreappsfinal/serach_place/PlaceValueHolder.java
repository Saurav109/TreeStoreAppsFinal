package com.example.ironman.treestoreappsfinal.serach_place;

public class PlaceValueHolder {
    double lat;
    double lon;
    String placeName;
    String location;

    public PlaceValueHolder(double lat, double lon, String placeName, String location) {
        this.lat = lat;
        this.lon = lon;
        this.placeName = placeName;
        this.location = location;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
