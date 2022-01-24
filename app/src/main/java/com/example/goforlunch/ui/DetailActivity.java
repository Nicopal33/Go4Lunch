package com.example.goforlunch.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.goforlunch.BuildConfig;
import com.example.goforlunch.R;
import com.example.goforlunch.adapter.ListRestRecyclerViewAdapter;
import com.example.goforlunch.databinding.ActivityDetailRestBinding;
import com.example.goforlunch.model.User;
import com.example.goforlunch.model.details.DetailsRecyclerViewAdapter;
import com.example.goforlunch.model.restaurants.RestauDetails;
import com.example.goforlunch.model.restaurants.Result;
import com.example.goforlunch.repository.UserInjection;
import com.example.goforlunch.repository.UserViewModelFactory;
import com.example.goforlunch.ui.viewmodel.NearbyInjection;
import com.example.goforlunch.ui.viewmodel.NearbyRestViewModel;
import com.example.goforlunch.ui.viewmodel.NearbyViewModelFactory;
import com.example.goforlunch.ui.viewmodel.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailRestBinding mActivityBinding;
    private TextView mName;
    private TextView mAddress;
    private ImageView mImage;
    private RatingBar mRatingbar;
    private ImageButton mReturnButton;
    private AppCompatTextView mPhoneButton;
    private AppCompatTextView mWebButton;
    private AppCompatTextView mLikeButton;
    private FloatingActionButton mFab;
    private UserViewModel mUserViewModel;
    private final String currentUserId = FirebaseAuth.getInstance().getUid();
    private final List<String> mRestaurantLiked = new ArrayList<>();
    private String placeId;
    private final List<User> mUsers = new ArrayList<>();
    private NearbyRestViewModel mRestaurantViewModel;
    private final DetailsRecyclerViewAdapter mAdapter = new DetailsRecyclerViewAdapter(mUsers);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityBinding = ActivityDetailRestBinding.inflate(getLayoutInflater());
        View view = mActivityBinding.getRoot();
        Intent getIntent = getIntent();
        placeId = getIntent.getStringExtra("placeId");
        configureUI();
        configureUserViewModel();
        configureNearbyRestViewModel();
        mRestaurantViewModel.getRestaurantsDetails(placeId, BuildConfig.API_KEY)
                .observe(this, this::setRestaurant);
        configureUsers();
        RecyclerView recyclerView = mActivityBinding.detailWorkmates;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        setContentView(view);
    }

    private void setRestaurant (RestauDetails restaurant) {
        Result restaurant1 = restaurant.getResult();
            updateWithRestaurant(restaurant1);
    }

    private void updateWithRestaurant (Result mRestaurant) {
        mReturnButton.setOnClickListener(v-> onBackPressed());
        if (mRestaurant != null) {
            String phone = "tel:" + mRestaurant.getInternationalPhoneNumber();
            String name = mRestaurant.getName();
            if (mRestaurant.getName() != null) {
                mName.setText(name);
            } else {
                mName.setText(R.string.info_no_username_found);
            }
            mAddress.setText(mRestaurant.getVicinity());
            if (mRestaurant.getPhotos() != null) {
                if (mRestaurant.getPhotos().get(0) != null) {
                    String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth" + "=400&photoreference="
                            + mRestaurant.getPhotos().get(0)
                            .getPhotoReference() + "&key=" + BuildConfig.API_KEY;
                    Glide.with(mImage).load(photoUrl).into(mImage);
                }
            } else {
                Glide.with(mImage).load(R.drawable.logo).into(mImage);
            }
            if (mRestaurant.getRating() != null) {
                mRatingbar.setRating((float)
                        ListRestRecyclerViewAdapter.setRating(mRestaurant.getRating()));
            }
            mPhoneButton.setOnClickListener(v -> {
                if (mRestaurant.getInternationalPhoneNumber() != null) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phone));
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this, getString(R.string.no_phone),
                            Toast.LENGTH_SHORT).show();
                }
            });
            mWebButton.setOnClickListener(v -> {
                if (mRestaurant.getWebsite() != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mRestaurant.getWebsite()));
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this, getString(R.string.no_website),
                            Toast.LENGTH_SHORT).show();
                }
            });
            mFab.setOnClickListener(v -> updateRestaurantChoose(mRestaurant));
            mLikeButton.setOnClickListener(v -> updateRestaurantLiked(mRestaurantLiked, mRestaurant.getPlaceId()));
        }
    }

    private void configureUI() {
        mName = mActivityBinding.detailName;
        mAddress = mActivityBinding.detailAdress;
        mImage = mActivityBinding.detailPhoto;
        mPhoneButton = mActivityBinding.detailCallBtn;
        mLikeButton = mActivityBinding.detailLikeBtn;
        mWebButton = mActivityBinding.detailWebBtn;
        mReturnButton = mActivityBinding.arrowBack;
        mFab = mActivityBinding.checkBtn;
        mRatingbar = mActivityBinding.detailRating;
    }

    private void updateRestaurantChoose(Result mRestaurant) {
        mUserViewModel.updateUserRestaurant(currentUserId, placeId, this);
        mUserViewModel.updateUserRestaurantName(currentUserId, mRestaurant.getName(), this);
        mUserViewModel.updateUserRestaurantAddress(currentUserId, mRestaurant.getFormattedAddress(), this);
    }

    private void configureUsers() {
        mUserViewModel.getUserByPlaceId(placeId, this)
                .observe(this, this::setUsersList);
    }


    private void setRestaurantsLiked() {
        mUserViewModel.getRestaurantLiked().observe(this, mRestaurantLiked::addAll);
    }

    private void updateRestaurantLiked(List<String> restaurantsLiked, String restaurantLike) {
        mUserViewModel.updateRestaurantsLiked(currentUserId, restaurantsLiked, restaurantLike, getBaseContext());
    }

    private void setUsersList(User users) {
        mUsers.clear();
        mUsers.addAll((Collection<? extends User>) users);
        mAdapter.notifyDataSetChanged();
    }

    private void configureUserViewModel() {
        UserViewModelFactory mViewModelFactory = UserInjection.provideViewModelFactory();
        this.mUserViewModel = new ViewModelProvider(this, mViewModelFactory)
                .get(UserViewModel.class);
        this.mUserViewModel.initUsers(this);
        this.mUserViewModel.initRestaurantLiked(currentUserId, this);
        this.setRestaurantsLiked();
    }

    private void configureNearbyRestViewModel() {
        NearbyViewModelFactory nearbyViewModelFactory = NearbyInjection.provideRestaurantViewModel();
        mRestaurantViewModel = new ViewModelProvider(this, nearbyViewModelFactory)
                .get(NearbyRestViewModel.class);
    }


}