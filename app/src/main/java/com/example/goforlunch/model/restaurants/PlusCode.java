package com.example.goforlunch.model.restaurants;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlusCode {

    @SerializedName("height")
    @Expose
    private String compoudCode;

    @SerializedName("globam_vode")
    @Expose
    private String globalCode;

    public String getCompoudCode() {return compoudCode;}
    public void setCompoudCode (String compoudCode) {this.compoudCode = compoudCode;}

    public String getGlobalCode() {return globalCode; }
    public void setGlobalCode(String globalCode) {this.globalCode = globalCode;}
}
