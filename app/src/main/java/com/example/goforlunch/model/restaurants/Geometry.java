package com.example.goforlunch.model.restaurants;

import android.location.Location;

import com.google.geo.type.Viewport;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geometry {

    @SerializedName("location")
    @Expose
    private Location location;

    @SerializedName("viewport")
    @Expose
    private Viewport viewport;

    public Location getLocation() { return location;}

    public void setLocation(Location location) {this.location = location;}
    ;
    public Viewport getViewport()   { return viewport;}

    public void setViewport(Viewport viewport) {this.viewport = viewport;}
}
