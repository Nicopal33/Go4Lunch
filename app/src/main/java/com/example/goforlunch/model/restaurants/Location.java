package com.example.goforlunch.model.restaurants;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Location {

    @SerializedName("lat")
    @Expose
    private Double lat;

    @SerializedName("lng")
    @Expose
    private Double lng;

    private Double getLat() {return lat;}
    private void setLat(Double lat) {this.lat = lat;}

    private Double getLng() {return lng;}
    private void setLng (Double lng) {this.lng = lng;}
}
