package com.example.goforlunch.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.goforlunch.model.restaurants.RestauDetails;
import com.example.goforlunch.model.restaurants.RestauOutputs;
import com.example.goforlunch.repository.NearbyRestRepository;

public class NearbyRestViewModel extends ViewModel {

    private final NearbyRestRepository mNearbyRestRepository;

    public NearbyRestViewModel (NearbyRestRepository nearbyRestRepository) {
        mNearbyRestRepository = nearbyRestRepository;
    }

    public MutableLiveData<RestauOutputs> getRestaurantsList (String location, String radius, String key) {
        return mNearbyRestRepository.getRestaurants(location, radius, key);
    }

    public MutableLiveData<RestauDetails> getRestaurantDetails (String placeId, String key) {
        return mNearbyRestRepository.getRestaurant(placeId, key);
    }

}
