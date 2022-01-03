package com.example.goforlunch.repository;

import android.content.Context;

import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.goforlunch.model.User;
import com.google.firebase.firestore.Query;

public class UserRepository {

    private static volatile UserRepository instance;
    private UserRepository userRepository;
    private UserRepository (){}

    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result !=null) {
            return result;
        }
        synchronized (UserRepository.class) {
            if (instance ==null) {
                instance = new UserRepository();
            }
            return instance;
        }
    }
    // get the current user connected
    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    @Nullable
    public String getCurrentUserUID(){
        FirebaseUser user = getCurrentUser();
        return (user !=null)? user.getUid() : null;
    }

    public Task<Void> deleteUser(Context context){
        return AuthUI.getInstance().delete(context);
    }

    private static final String COLLECTION_NAME ="users";
    private static final String ID_FIELD ="userId";
    private static final String USERNAME_FIELD ="username";
    private static final String AVATAR_FIELD = "avatarUrl";



    //GET THE COLLECTION REFERENCE
    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    //CREATE USER IN FIRESTORE
    public void createUser() {
        FirebaseUser user = getCurrentUser();
        if (user!=null) {
            String urlPicture = (user.getPhotoUrl() !=null) ? user.getPhotoUrl().toString() : null;
            String username = user.getDisplayName();
            String uid = user.getUid();

            User userToCreate = new User(uid, username, urlPicture);
            this.getUsersCollection().document(uid).set(userToCreate);

        }
    }
    /*
    //GET USER DATA FROM FIRESTORE
    public Task<DocumentSnapshot> getUserData() {
        String uid = Objects.requireNonNull(this.getCurrentUser()).getUid();
        return this.getUsersCollection().document(uid).get();
    }*/

    public Task<DocumentSnapshot> getUserData(){
        String uid = this.getCurrentUserUID();
        if(uid !=null) {
            return this.getUsersCollection().document(uid).get();
        }else {
            return null;
        }
    }

    // Update User Username
    /*public Task<Void> updateUsername(String username) {
        String uid = Objects.requireNonNull(this.getCurrentUser()).getUid();
        return this.getUsersCollection().document(uid).update(USERNAME_FIELD, username);
    }*/

    public Task<Void> updateUsername(String username) {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            return this.getUsersCollection().document(uid).update(USERNAME_FIELD, username);
        }else{
            return  null;
        }
    }

    // Delete the User from Firestore
    /*public void deleteUserFromFirestore() {
        String uid = Objects.requireNonNull(this.getCurrentUser()).getUid();
        this.getUsersCollection().document(uid).delete();
    }*/

    public void deleteUserFromFirestore() {
        String uid = this.getCurrentUserUID();
        if (uid != null){
            this.getUsersCollection().document(uid).delete();
        }
    }

        public Query getAllUsers () {
        return this.getUsersCollection()
                .orderBy("uid");
    }








}
