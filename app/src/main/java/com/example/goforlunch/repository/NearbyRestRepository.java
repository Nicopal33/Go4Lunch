package com.example.goforlunch.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.goforlunch.model.restaurants.RestauDetails;
import com.example.goforlunch.model.restaurants.RestauOutputs;
import com.example.goforlunch.utils.PlacesApiService;
import com.example.goforlunch.utils.RetrofitService;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearbyRestRepository {

    private static PlacesApiService placesApiService;
    private final MutableLiveData<RestauOutputs> sRestaurants = new MutableLiveData<>();
    private static NearbyRestRepository sNearbyRestRepository;

    public static NearbyRestRepository getInstance() {
        if (sNearbyRestRepository == null) {
            sNearbyRestRepository = new NearbyRestRepository();
        }
        return sNearbyRestRepository;
    }

    public NearbyRestRepository() {
        String baseUrlForNearbySearch = "https://maps.googleapis.com/maps/api/place/";
        placesApiService = RetrofitService.getPlacesInterface(baseUrlForNearbySearch);
    }

    public MutableLiveData<RestauOutputs> getRestaurants (String location, String radius, String key) {
        Call<RestauOutputs> restaurantsList = placesApiService.getFollowingPlaces(location, radius, "restaurant", key);
        restaurantsList.enqueue(new Callback<RestauOutputs>() {
            @Override
            public void onResponse(@NotNull Call<RestauOutputs> call, @NotNull Response<RestauOutputs> response) {
                sRestaurants.setValue(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<RestauOutputs> call, @NotNull Throwable t) {
                sRestaurants.postValue(null);
            }
        });
        return sRestaurants;




    }

    public MutableLiveData<RestauDetails> getRestaurant(String placeId, String key) {
        MutableLiveData<RestauDetails> restaurantOutputsMutableLiveData = new MutableLiveData<>();
        Call<RestauDetails> restaurantOutputsCall = placesApiService.getFollowingDetails(placeId, key);
        restaurantOutputsCall.enqueue(new Callback<RestauDetails>() {
            @Override
            public void onResponse(@NotNull Call<RestauDetails> call,
                                   @NotNull Response<RestauDetails> response) {
                restaurantOutputsMutableLiveData.setValue(response.body());
            }


            @Override
            public void onFailure(Call<RestauDetails> call, @NotNull Throwable t) {
                restaurantOutputsMutableLiveData.postValue(null);

            }
        });
        return restaurantOutputsMutableLiveData;

    }
}
