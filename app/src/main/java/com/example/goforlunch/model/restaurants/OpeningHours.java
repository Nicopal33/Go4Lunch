package com.example.goforlunch.model.restaurants;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpeningHours {

    @SerializedName("open now")
    @Expose
    private boolean openNow;
    public  boolean getOpenNow() {return openNow;}
    public void setOpenNow(boolean openNow) {this.openNow = openNow;}
}
