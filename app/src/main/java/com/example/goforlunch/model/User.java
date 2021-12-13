package com.example.goforlunch.model;

import androidx.annotation.Nullable;

public class User {

    private String uid;
    private String userName;
    @Nullable
    private String avatarUrl;
    public String restaurantName;
    public String restaurant;
    public String picture;

    public User() {}

    public User (String userId, String userName, @Nullable String avatarUrl) {
        this.uid = userId;
        this.userName = userName;
        this.avatarUrl = avatarUrl;
    }

    public String getUid() {return uid;}
    public String getUserName() {return userName;}
    @Nullable
    public String getAvatarUrl() {return avatarUrl;}
    public String getRestaurantName() {return restaurantName;}
    public String getRestaurant () {return restaurant; }
    public String getPicture () {return picture;}

    public void setUid (String uid) {this.uid = uid;}

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAvatarUrl(@Nullable String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}
