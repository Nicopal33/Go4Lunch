package com.example.goforlunch.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.goforlunch.repository.NearbyRestRepository;

import org.jetbrains.annotations.NotNull;

public class NearbyViewModelFactory implements ViewModelProvider.Factory {

    private final NearbyRestRepository mNearbyRestRepository;

    public NearbyViewModelFactory (NearbyRestRepository nearbyRestRepository) {
        mNearbyRestRepository = NearbyRestRepository.getInstance();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NearbyRestRepository.class)) {
            return (T) new NearbyRestViewModel(mNearbyRestRepository);
        }
        return (T) new NearbyRestViewModel(mNearbyRestRepository);
    }
}
