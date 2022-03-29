package com.example.goforlunch.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.goforlunch.BuildConfig;
import com.example.goforlunch.R;
import com.example.goforlunch.databinding.ListRestItemBinding;
import com.example.goforlunch.model.User;
import com.example.goforlunch.model.restaurants.ResultRestau;
import com.example.goforlunch.repository.UserInjection;
import com.example.goforlunch.repository.UserViewModelFactory;
import com.example.goforlunch.ui.DetailActivity;
import com.example.goforlunch.ui.viewmodel.UserViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class ListRestRecyclerViewAdapter
        extends RecyclerView.Adapter<ListRestRecyclerViewAdapter.ViewHolder> {

    private FusedLocationProviderClient mFusedLocationProviderClient;
    public List<ResultRestau> restaurants;
    //private final ListRestItemBinding mListRestItemBinding;
    private Location mLastKnowLocation;
    private UserViewModel mUserViewModel;
    private final List<User> mUsers = new ArrayList<>();

    public ListRestRecyclerViewAdapter(List<ResultRestau> restaurants) {
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        com.example.goforlunch.databinding.ListRestItemBinding listRestItemBinding =
                (ListRestItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent,false));
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(parent.getContext());
        configureUserViewModel(parent.getContext());
        return new ViewHolder(listRestItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        ResultRestau restaurant = restaurants.get(position);
        Context context = holder.mListRestItemBinding.getRoot().getContext();
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                mLastKnowLocation=task.getResult();

        if (restaurant.getGeometry() !=null) {
            double distanceDouble = meterDistanceBetweenPoints(restaurant.getGeometry()
                    .getLocation().getLat(),restaurant.getGeometry().getLocation().getLng(),
                    mLastKnowLocation.getLatitude(), mLastKnowLocation.getLongitude());
            String distance = (int) distanceDouble + "m";
            holder.mRestaurantDistance.setText(distance);
        }
            });
        }
        String placeId = restaurant.getPlaceId();
        if (restaurant.getPhotos()!=null) {
            String photoUrl =
                    "https://maps.googleapis.com/maps/api/photo?maxwidth=400&photoreference="
                            + restaurant.getPhotos().get(0).getPhotoReference() + "&key=" + BuildConfig.API_KEY;
            Glide.with(holder.mRestaurantPhoto.getContext()).load(photoUrl).into(holder.mRestaurantPhoto);
        }
        else  {
            holder.mRestaurantPhoto.setColorFilter(R.color.black);
        }
        if (restaurant.getName() !=null) {
            holder.mRestaurantName.setText(restaurant.getName());
        }
        else {
            holder.mRestaurantName.setText(R.string.no_name_found);
        }
        if (restaurant.getVicinity() != null){
            String address= restaurant.getVicinity().split(",")[0];
            holder.mRestaurantText.setText(address);
        }
        else {
            holder.mRestaurantText.setText(R.string.unknown);
        }
        holder.mRestaurantOpening.setText(getRestOpeningHours (restaurant));


        holder.mRestaurantRatingBar.setRating((float) setRating(restaurant.getRating()));
        mUserViewModel.getUsers()
                .observe((LifecycleOwner) context, users -> setUsers(users, restaurant, holder));
        holder.mListRestItemBinding.getRoot().setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailActivity.class);
            intent.putExtra("placeId", placeId);
            startActivity(v.getContext(), intent, null);
        });


    }

    public int getRestOpeningHours(ResultRestau restaurant) {
        int result;
        if (restaurant.getOpeningHours() != null) {
            if (restaurant.getOpeningHours().getOpenNow()) {
                result = R.string.open;
            }else {
                result = R.string.close;
            }
        }
        else {
        result = R.string.unknown;
        }
        return result;
    }



    public static double setRating (double rating) {
        return ((rating/5)*3);
    }

    public void setUsers (List<User> users, ResultRestau restau, ViewHolder holder) {
        for (User user : users) {
            if (user.getRestaurant() !=null && user.getRestaurant().equals(restau.getPlaceId())) {
                mUsers.add(user);
            }else  {
                mUsers.remove(user);
            }
        }
        if (mUsers.size() !=0) {
            holder.mRestaurantWorkmate.getDisplay();
            String workmates = "(" +mUsers.size() + ")";
            holder.mRestaurantNbrWork.setText(workmates);
        }
    }

    private void configureUserViewModel (Context context) {
        UserViewModelFactory mViewModelFactory = UserInjection.provideViewModelFactory();
        this.mUserViewModel = new ViewModelProvider((ViewModelStoreOwner) context,
            mViewModelFactory).get(UserViewModel.class);
        this.mUserViewModel.initUsers(context);

    }

    public static double meterDistanceBetweenPoints (double lat_a, double lng_a, double lat_b, double lng_b) {
        float pk = (float) (180.f/Math.PI);
        double a1 = lat_a/pk;
        double a2 = lng_a/pk;
        double b1 = lat_b/pk;
        double b2 = lng_b/pk;

        double t1 = Math.cos(a1) * Math.cos(a2) *Math.cos(b1) *Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) *Math.cos(b1) *Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1+t2+t3);

        return 636600 * tt;
    }



    @Override
    public int getItemCount() {
        return restaurants.size();
    }


    public  class ViewHolder extends RecyclerView.ViewHolder {
        private final ListRestItemBinding mListRestItemBinding;
        private final TextView mRestaurantDistance;
        private final ImageView mRestaurantPhoto;
        private final TextView mRestaurantName;
        private final TextView mRestaurantText;
        private final TextView mRestaurantOpening;
        private final RatingBar mRestaurantRatingBar;
        private final ImageView mRestaurantWorkmate;
        private final TextView mRestaurantNbrWork;

        public ViewHolder(ListRestItemBinding listRestItemBinding) {
            super(listRestItemBinding.getRoot());
            mListRestItemBinding = listRestItemBinding;
            mRestaurantDistance = listRestItemBinding.restaurantDistance;
            mRestaurantPhoto = listRestItemBinding.restaurantPhoto;
            mRestaurantName = listRestItemBinding.restaurantName;
            mRestaurantText = listRestItemBinding.restaurantText;
            mRestaurantOpening = listRestItemBinding.restaurantOpening;
            mRestaurantRatingBar = listRestItemBinding.restaurantRatingBar;
            mRestaurantWorkmate = listRestItemBinding.restaurantWorkmate;
            mRestaurantNbrWork = listRestItemBinding.restaurantNbrWork;
        }
        @NotNull
        @Override
        public String toString() { return super.toString();}

    }
}
