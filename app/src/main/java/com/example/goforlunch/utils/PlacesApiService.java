package com.example.goforlunch.utils;

import android.service.quickaccesswallet.QuickAccessWalletService;

import com.example.goforlunch.model.restaurants.RestauDetails;
import com.example.goforlunch.model.restaurants.RestauOutputs;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesApiService {

    @GET("nearbysearch/json?")
    Call<RestauOutputs> getFollowingPlaces(@Query("location") String location,
                                           @Query("radius") String radius,
                                           @Query("type") String type,
                                           @Query("key") String key);

    @GET("details/json")
    Call<RestauDetails> getFollowingDetails(@Query("place_id") String placeId,
                                            @Query("key") String key);

}

