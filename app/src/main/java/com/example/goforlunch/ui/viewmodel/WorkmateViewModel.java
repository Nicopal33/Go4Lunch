//package com.example.goforlunch.ui.viewmodel;
//
//import android.graphics.Typeface;
//import android.os.Build;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.annotation.RequiresApi;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MediatorLiveData;
//import androidx.lifecycle.ViewModel;
//
//import com.example.goforlunch.model.WorkmatesUiModel;
//import com.example.goforlunch.repository.RestaurantRepository;
//import com.example.goforlunch.model.User;
//import com.example.goforlunch.model.restaurants.Restaurant;
//import com.example.goforlunch.repository.UserRepository;
//import com.example.goforlunch.utils.SingleLiveEvent;
//import com.google.firebase.firestore.CollectionReference;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class WorkmateViewModel extends ViewModel {
//
//    @NonNull
//    private final RestaurantRepository restaurantRepository;
//    final LiveData<List<User>> usersLiveData;
//    final MediatorLiveData<List<WorkmatesUiModel>> uiModelMutableLiveData = new MediatorLiveData<>();
//    final MediatorLiveData<Map<String, Restaurant>> nameRestaurantLiveData = new MediatorLiveData<>();
//    private final List<String> alreadyRequiredIds = new ArrayList<>();
//    private final SingleLiveEvent<String > eventDetailActivity = new SingleLiveEvent<>();
//
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public WorkmateViewModel(@NonNull RestaurantRepository restaurantRepository,
//                             @NonNull UserRepository userRepository) {
//        this.restaurantRepository = restaurantRepository;
//        nameRestaurantLiveData.setValue(new HashMap<>());
//        usersLiveData = userRepository.getAllUsers();
//        uiModelMutableLiveData.addSource(
//                usersLiveData,
//                userList -> combine(
//                        userList,nameRestaurantLiveData.getValue()));
//
//        uiModelMutableLiveData.addSource(
//                nameRestaurantLiveData,
//                stringStringMap -> combine(
//                        usersLiveData.getValue(),stringStringMap));
//
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void combine (
//            @Nullable List<User> users,
//            @NonNull Map<String, Restaurant> restaurantMap
//    ) {
//
//        if ((users == null)) return;
//
//        List<WorkmatesUiModel> uiStateList = new ArrayList<>();
//
//        for (User user : users) {
//            Restaurant existingRestaurant = restaurantMap.get(user.getUid());
//            if (!alreadyRequiredIds.contains(user.getUid())) {
//                alreadyRequiredIds.add(user.getUid());
//                nameRestaurantLiveData.addSource(
//                        restaurantRepository.getActiveRestaurantFromLockup(user.getUid()),
//                        restaurant -> {
//                            Map<String, Restaurant> existingMap = nameRestaurantLiveData.getValue();
//                            existingMap.put(user.getUid(), restaurant);
//                            nameRestaurantLiveData.setValue(existingMap);
//                        }
//                );
//            }
//            Map<String, String> map = new HashMap<>();
//            String txt;
//            int style;
//
//            if (existingRestaurant != null) {
//                map.put("id", existingRestaurant.getId());
//                map.put("name", existingRestaurant.getName());
//                txt = "is eating";
//                style = Typeface.BOLD;
//            } else {
//                txt = "hasn't decided yet";
//                map.put("id", "");
//                map.put("name", "");
//                style = Typeface.ITALIC;
//            }
//            String sentence = user.getUserName() + txt;
//
//            uiStateList.add(
//                    new WorkmatesUiModel(
//                            user.getUid(),
//                            map,
//                            sentence,
//                            user.getAvatarUrl(),
//                            style
//                    ));
//
//            Collections.sort(uiStateList, (o1, o2) -> Integer.compare(o1.getTxtStyle(), o2.getTxtStyle()));
//
//
//        }
//
//        if (!uiStateList.isEmpty()) {
//            uiModelMutableLiveData.setValue(uiStateList);
//        }
//    }
//
//    public LiveData<List<WorkmatesUiModel>> getWorkmatesUiModelLiveData() {
//        return uiModelMutableLiveData;
//    }
//
//    public SingleLiveEvent<String > getEventDetailActivity() {return eventDetailActivity;
//    }
//
//    public void openDetailActivity(String id) {eventDetailActivity.setValue(id);}

//}
