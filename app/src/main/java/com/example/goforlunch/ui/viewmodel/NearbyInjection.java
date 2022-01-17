package com.example.goforlunch.ui.viewmodel;

import com.example.goforlunch.repository.NearbyRestRepository;

public class NearbyInjection {

    public static NearbyRestRepository provideNearbyRepository () {
        return new NearbyRestRepository();
    }

    public static NearbyViewModelFactory provideRestaurantViewModel () {
        NearbyRestRepository nearbyRestRepository = provideNearbyRepository();
        return new NearbyViewModelFactory(nearbyRestRepository);
    }

}
