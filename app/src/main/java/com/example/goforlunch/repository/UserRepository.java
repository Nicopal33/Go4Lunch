//package com.example.goforlunch.repository;
//
//import android.app.Application;
//import android.net.Uri;
//import android.os.Build;
//import android.text.TextUtils;
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.annotation.RequiresApi;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//
//import com.example.goforlunch.api.UserHelper;
//import com.example.goforlunch.model.User;
//import com.example.goforlunch.model.restaurants.Restaurant;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class UserRepository {
//
//    private static final String TAG = UserRepository.class.getSimpleName();
//    private final Application application;
//    private final FirebaseAuth firebaseAuth;
//    final MutableLiveData<String> selectedQuery = new MutableLiveData<>();
//
//    public UserRepository(Application application, FirebaseAuth firebaseAuth) {
//        this.application = application;
//        this.firebaseAuth = firebaseAuth;
//
//
//    }
//
//    public boolean isCurrentUserLogged() {return firebaseAuth.getCurrentUser() != null; }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void createUser() {
//        FirebaseUser userValue = firebaseAuth.getCurrentUser();
//        if (userValue != null) {
//            String urlPicture = (userValue.getPhotoUrl() != null) ? userValue.getPhotoUrl().toString() : null;
//            String username = userValue.getDisplayName();
//            String uid = userValue.getUid();
//            UserHelper.createUser(uid, username, urlPicture).addOnFailureListener(this.onFailureListener());
//        }
//    }
//
//    public LiveData<User> getCurrentUser() {
//        MutableLiveData<User> mutableLiveData = new MutableLiveData<>();
//        FirebaseUser userValue = firebaseAuth.getCurrentUser();
//        if (userValue != null) {
//            String urlPicture = (userValue.getPhotoUrl() != null) ? userValue.getPhotoUrl().toString() : null;
//            String username = userValue.getDisplayName();
//            String uid = userValue.getUid();
//            mutableLiveData.postValue(new User(uid, username, urlPicture));
//        }
//        return mutableLiveData;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public LiveData<List<User>> getAllUsers() {
//        MutableLiveData<List<User>> mutableLiveData = new MutableLiveData<>();
//        UserHelper.getUsersCollection()
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    List<User> users = new ArrayList<>();
//                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                        User user = documentSnapshot.toObject(User.class);
//                        if (firebaseAuth.getCurrentUser() != null
//                                && !(user.getUid().equals(firebaseAuth.getCurrentUser().getUid()))
//                        ) {
//                            users.add(user);
//                        }
//                    }
//                    mutableLiveData.setValue(users);
//                });
//        return mutableLiveData;
//    }
//
//
//    public LiveData<Map<String,String>> getCurrentUserInstance() {
//        FirebaseUser userValue = firebaseAuth.getCurrentUser();
//        MutableLiveData<Map<String, String>> mapMutableLiveData = new MutableLiveData<>();
//        Map<String, String> map = new HashMap<>();
//        if (userValue != null) {
//            String name = TextUtils.isEmpty(userValue.getDisplayName()) ? "Info username found" : userValue.getDisplayName();
//            String email = TextUtils.isEmpty(userValue.getEmail()) ? "Info no email found" : userValue.getEmail();
//            Uri photo = userValue.getPhotoUrl();
//            String url = null;
//            if (photo != null) url = photo.toString();
//            map.put("name", name);
//            map.put("email", email);
//            map.put("url", url);
//            mapMutableLiveData.setValue(map);
//        }
//        return mapMutableLiveData;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public LiveData<String> getCurrentUserRestaurant() {
//        MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
//        if (firebaseAuth.getCurrentUser() != null) {
//            UserHelper.getLockupUser(firebaseAuth.getCurrentUser().getUid())
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document.exists()) {
//                                Log.d(TAG, document.getId() + " =>" + document.getData());
//                                Restaurant restaurant = document.toObject(Restaurant.class);
//                                if (restaurant != null) {
//                                    mutableLiveData.postValue(restaurant.getId());
//                                }
//                            }
//                        }
//                    });
//        }
//        return mutableLiveData;
//    }
//
//    public void setSelectedQuery (String text) { selectedQuery.setValue(text); }
//
//    public LiveData<String> getSelectedQuery() {return selectedQuery;}
//
//
//    protected OnFailureListener onFailureListener() {
//        return e -> Toast.makeText(
//                application,
//                "An unknow error is occured",
//                Toast.LENGTH_LONG
//        ).show();
//    }
//
//}
