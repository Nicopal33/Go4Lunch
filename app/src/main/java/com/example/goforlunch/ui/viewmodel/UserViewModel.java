package com.example.goforlunch.ui.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.goforlunch.model.User;
import com.example.goforlunch.repository.UserCRUDRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class UserViewModel extends androidx.lifecycle.ViewModel {

    private LiveData<List<User>> users;
    private final Executor executor;
    private final UserCRUDRepository mUserCRUDRepository;
    private MutableLiveData<List<String>> restaurantLiked;

    public UserViewModel (UserCRUDRepository userCRUDRepository, Executor executor) {
        this.mUserCRUDRepository = userCRUDRepository;
        this.executor = executor;
    }

    public void initUsers (Context context) {
        if (this.users !=null) {
            return;
        }
        users = mUserCRUDRepository.getUsers(context);
    }

    public LiveData<List<User>> getUsers() {return users;}

    public void initRestaurantLiked (String uid, Context context) {
        if (this.restaurantLiked != null) {
            return;
        }
        restaurantLiked = mUserCRUDRepository.getRestaurantsFavorites(uid, context);
    }

    public MutableLiveData<List<String>> getRestaurantLiked() { return restaurantLiked;}

    public MutableLiveData<User> getUser (String uid, Context context) {
        return mUserCRUDRepository.getUser(uid, context);
    }

    public MutableLiveData<User> getUserByPlaceId (String placeId, Context context) {
        return mUserCRUDRepository.getUser(placeId, context);
    }

    public void createCurrentUser (Context context) {
        executor.execute(() -> mUserCRUDRepository.createUser(context));
    }

    public void updateUsername(String uid, String username, Context context) {
        executor.execute(() -> mUserCRUDRepository.updateUsername(uid, username, context));
    }

    public void updateUserEmail(String uid, String email, Context context) {
        executor.execute(() -> mUserCRUDRepository.updateUserEmail(uid, email, context));
    }

    public void updateUserImage(String uid, String image, Context context) {
        executor.execute(() -> mUserCRUDRepository.updateUserImage(uid, image, context));
    }

    public void updateUserRestaurant(String uid, String restaurantId, Context context) {
        executor.execute(() -> mUserCRUDRepository.updateUserRestaurant(uid, restaurantId, context));
    }

    public void updateUserRestaurantAddress(String uid, String restaurantAddress, Context context) {
        executor.execute(() -> mUserCRUDRepository.updateUserRestaurantAddress(uid,
                restaurantAddress, context));
    }

    public void updateRestaurantsLiked(String uid, List<String> restaurants, String restaurant,
                                       Context context) {
        executor.execute(() -> mUserCRUDRepository.updateRestaurantsLiked(uid, restaurants,
                restaurant, context));
    }

    public void updateUserRestaurantName(String uid, String restaurantToLike, Context context) {
        executor.execute(() -> mUserCRUDRepository.updateUserRestaurantName(uid,
                restaurantToLike, context));
    }

    public void deleteUser(String uid, Context context) {
        executor.execute(() -> mUserCRUDRepository.deleteUser(uid, context));
    }



}
