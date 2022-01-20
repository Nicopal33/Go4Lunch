package com.example.goforlunch.repository;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import com.example.goforlunch.R;
import com.google.android.material.circularreveal.CircularRevealHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.OnFailureListener;
import com.example.goforlunch.model.User;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserCRUDRepository {


    MutableLiveData<List<String>> mutableLiveData = new MutableLiveData<>();

    public MutableLiveData<List<User>> getUsers (Context context) {
        MutableLiveData<List<User>> result = new MutableLiveData<>();
        UserCRUD.getUsers().addOnFailureListener(onFailureListener(context)).addOnSuccessListener(documentSnapshots -> {
                 List<User> users = new ArrayList<>();
                 for (DocumentSnapshot documentSnapshot : documentSnapshots.getDocuments()){
                     if (documentSnapshot !=null && !documentSnapshot.getId().equals(getCurrentUser()
                             .getUid()))  {
                        User user = (User) documentSnapshot.toObject(User.class);
                        users.add(user);
                    }
                 }
                 result.setValue(users);
        });
        return result;
    }


    public MutableLiveData<List<User>> getUserByPlaceId (String placeId, Context context) {
        MutableLiveData<List<User>> result = new MutableLiveData<>();
        UserCRUD.getUsers().addOnFailureListener(onFailureListener(context)).addOnSuccessListener(documentSnapshots  -> {
            List<User> users = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : documentSnapshots.getDocuments()){
                if (Objects.equals(documentSnapshot.get("restaurant"),placeId) &&!documentSnapshot
                        .getId().equals(getCurrentUser().getUid())) {
                    User user = (User) documentSnapshot.toObject(User.class);
                    users.add(user);
                }
            }
            result.setValue(users);
        });
        return result;

    }

    public MutableLiveData<User> getUser (String uid, Context context) {
        MutableLiveData<User> result = new MutableLiveData<>();
        UserCRUD.getUser(uid).addOnFailureListener(onFailureListener(context)).addOnSuccessListener
                (documentSnapshot -> {
                    if (documentSnapshot != null) {
                        User user = documentSnapshot.toObject(User.class);
                        result.setValue(user);
                    } else {
                        result.setValue(new User("", "", "", "", "",
                                new ArrayList<>(), "", ""));
                    }
                });
                return result;
    }

    public void createUser (Context context) {
        MutableLiveData<FirebaseUser> result = new MutableLiveData<>();
        FirebaseUser user = this.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            String username = user.getDisplayName();
            String email = user.getEmail();
            String imageUrl = (user.getPhotoUrl() !=null) ? Objects.requireNonNull
                    (this.getCurrentUser().getPhotoUrl()).toString() : null;

            UserCRUD.createUser(uid, username, email, imageUrl, "", new ArrayList<>(),
                    "","").addOnSuccessListener(aVoid -> result.setValue(user));
        }
    }

    public void updateUsername (String uid, String username, Context context) {
        MutableLiveData<String> result = new MutableLiveData<>();
        UserCRUD.updateUsername(uid, username).addOnFailureListener(onFailureListener(context))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context,"Information has been update", Toast.LENGTH_SHORT).show();
                    result.setValue(username);
                });
    }

    public void updateUserEmail (String uid, String email, Context context) {
        MutableLiveData<String> result = new MutableLiveData<>();
        UserCRUD.updateUserEmail(uid, email).addOnFailureListener(onFailureListener(context))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Information has been update", Toast.LENGTH_SHORT).show();
                    result.setValue(email);
                });
    }

    public void updateUserImage (String uid, String picture, Context context) {
        MutableLiveData<String> result = new MutableLiveData<>();
        UserCRUD.updateUserImage(uid, picture).addOnFailureListener(onFailureListener(context))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Information has been update", Toast.LENGTH_SHORT).show();
                    result.setValue(picture);
                });

    }

    public void updateUserRestaurant (String uid, String restaurantId, Context context) {
        MutableLiveData<String> result = new MutableLiveData<>();
        UserCRUD.updateUserRestaurantName(uid, restaurantId).addOnFailureListener(onFailureListener(context))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Your choice has been registred", Toast.LENGTH_SHORT).show();
                    result.setValue(restaurantId);
                });
    }

    public void updateUserRestaurantName (String uid, String restaurantName, Context context) {
        MutableLiveData<String> result = new MutableLiveData<>();
        UserCRUD.updateUserRestaurantName(uid, restaurantName).addOnFailureListener(onFailureListener(context))
                .addOnSuccessListener(aVoid -> result.setValue(restaurantName));

    }

    public void updateUserRestaurantAddress (String uid, String restaurantAddress, Context context) {
        MutableLiveData<String> result = new MutableLiveData<>();
        UserCRUD.updateUserRestaurantAddress(uid, restaurantAddress).addOnFailureListener(onFailureListener(context))
                .addOnSuccessListener(aVoid -> result.setValue(restaurantAddress));
    }

    public MutableLiveData<List<String>> getRestaurantsFavorites (String uid, Context context) {
        UserCRUD.getUser(uid).addOnFailureListener(onFailureListener(context))
                .addOnSuccessListener(documentSnapshot -> {
                    List<String> restaurantLikedFirestore;
                    if (documentSnapshot !=null) {
                        restaurantLikedFirestore =
                                (List<String>) documentSnapshot.get("restaurantLiked");
                        }
                    else {
                        restaurantLikedFirestore = new ArrayList<>();
                    }
                    mutableLiveData.setValue(restaurantLikedFirestore);
                });
        return mutableLiveData;
    }

    public void updateRestaurantsLiked (String uid, List<String> restaurantLiked, String restaurantLike, Context context) {
        if (restaurantLiked != null) {
            if (restaurantLiked.contains(restaurantLike)) {
                restaurantLiked.remove(restaurantLike);
            }
            else {
                restaurantLiked.add(restaurantLike);
            }
            UserCRUD.updateUserRestaurantsLiked(uid, restaurantLiked).addOnFailureListener(onFailureListener(context));
        }
    }

    public void deleteUser (String uid, Context context) {
        MutableLiveData<String> result = new MutableLiveData<>();
        UserCRUD.deleteUser(uid).addOnFailureListener(onFailureListener(context))
                .addOnSuccessListener(aVoid -> result.setValue(uid));
    }



    protected FirebaseUser getCurrentUser() {return FirebaseAuth.getInstance().getCurrentUser();

    }

    private OnFailureListener onFailureListener (Context context) {
        return e -> Toast.makeText(context, "An Unknow Error Occured", Toast.LENGTH_SHORT).show();
    }



    /*
    public static UserCRUDRepository getInstance() {
        UserCRUDRepository result = instance;
        if (result !=null) {
            return result;
        }
        synchronized (UserCRUDRepository.class) {
            if (instance ==null) {
                instance = new UserCRUDRepository();
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

    //GET USER DATA FROM FIRESTORE
    public Task<DocumentSnapshot> getUserData() {
        String uid = Objects.requireNonNull(this.getCurrentUser()).getUid();
        return this.getUsersCollection().document(uid).get();
    }


    }

    // Update User Username
    /*public Task<Void> updateUsername(String username) {
        String uid = Objects.requireNonNull(this.getCurrentUser()).getUid();
        return this.getUsersCollection().document(uid).update(USERNAME_FIELD, username);
    }

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
    }

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


    */








}
