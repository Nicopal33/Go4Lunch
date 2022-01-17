package com.example.goforlunch.model;

import androidx.annotation.Nullable;

import java.util.Comparator;
import java.util.List;

public class User {

    private String uid;
    private final String username;
    private final String email;
    private String userName;
    private String avatarUrl;
    public String restaurantName;
    private final String restaurantAddress;
    public String restaurant;
    private final List<String> restaurantsLiked;
    public String picture;
    /*
    public User() {}

    public User (String userId, String userName, @Nullable String avatarUrl) {
        this.uid = userId;
        this.userName = userName;
        this.avatarUrl = avatarUrl;
    }
    */
    public User(String uid, String username, String email, String picture, String restaurant, List<String> restaurantsLiked,
                String restaurantName, String restaurantAddress) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.picture = picture;
        this.restaurant = restaurant;
        this.restaurantsLiked = restaurantsLiked;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
    }

    public String getUid() {return uid;}
    public String getUserName() {return userName;}
    @Nullable
    public String getEmail() {return email; }
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

    public static class UserRestaurantComparator implements Comparator<User> {
        @Override
        public int compare (User o1, User o2) {
            return o2.getRestaurant().length() - o1.getRestaurant().length();
        }

    }
}
