//package com.example.goforlunch.ui.viewmodel;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.ViewModel;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.example.goforlunch.repository.NearbyRestRepository;
//
//public class NearbyRestViewModelFactory implements ViewModelProvider.Factory {
//
//    private final NearbyRestRepository mNearbyRestRepository;
//
//    public NearbyRestViewModelFactory (NearbyRestRepository nearbyRestRepository) {
//        mNearbyRestRepository = NearbyRestRepository.getInstance();
//    }
//
//    @NonNull
//    @Override
//    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//        if (modelClass.isAssignableFrom(NearbyRestRepository.class)) {
//            return (T) new NearbyRestViewModel(mNearbyRestRepository);
//        }
//        return (T) new NearbyRestViewModel(mNearbyRestRepository);
//    }
//}
