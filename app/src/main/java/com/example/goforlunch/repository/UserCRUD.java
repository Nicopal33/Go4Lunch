package com.example.goforlunch.repository;

import com.example.goforlunch.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;

import java.util.List;

public class UserCRUD {

    private static final String COLLECTION_NAME = "users";

    public static CollectionReference getUsersCollection () {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static Task<QuerySnapshot> getUsers() { return getUsersCollection().get(); }

    public static Task<DocumentSnapshot> getUser (String uid) {
        return UserCRUD.getUsersCollection().document(uid).get();
    }

    public static Task<Void> createUser (String uid, String username, String email, String picture, String restaurant,
                                         List<String> restaurantsLiked, String restaurantName, String restaurantAddress) {
        User userToCreate = new User(uid, username, email, picture, restaurant, restaurantsLiked, restaurantName, restaurantAddress);
        return UserCRUD.getUsersCollection().document(uid).set(userToCreate);
    }

    public static Task<Void> updateUsername (String uid, String username) {
        return UserCRUD.getUsersCollection().document(uid).update("username", username);
    }

    public static Task<Void> updateUserEmail (String uid, String email) {
        return UserCRUD.getUsersCollection().document(uid).update("email", email);
    }
    public static Task<Void> updateUserImage (String uid, String picture) {
        return UserCRUD.getUsersCollection().document(uid).update("picture", picture);
    }

    public static Task<Void> updateUserRestaurant (String uid, String restaurant) {
        return UserCRUD.getUsersCollection().document(uid).update("restaurant", restaurant);
    }

    public static Task<Void> updateUserRestaurantName (String uid, String name) {
        return UserCRUD.getUsersCollection().document(uid).update("restaurantName", name);
    }

    public static Task<Void> updateUserRestaurantAddress (String uid, String address) {
        return UserCRUD.getUsersCollection().document(uid).update("restaurantAddress", address);
    }

    public static Task<Void> updateUserRestaurantsLiked (String uid, List<String> restaurants) {
        return UserCRUD.getUsersCollection().document(uid).update("restaurantLiked", restaurants);
    }

    public static Task<Void> deleteUser (String uid) {
        return UserCRUD.getUsersCollection().document(uid).delete();
    }




}
