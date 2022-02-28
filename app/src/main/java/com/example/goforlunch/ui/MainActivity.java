package com.example.goforlunch.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.OneTimeWorkRequest;
//import androidx.work.Worker;
import androidx.work.WorkManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.goforlunch.BuildConfig;
import com.example.goforlunch.R;
import com.example.goforlunch.utils.Worker;
import com.example.goforlunch.bottomfragments.ListViewFragment;
import com.example.goforlunch.bottomfragments.MapsViewFragment;
import com.example.goforlunch.bottomfragments.SettingFragment;
import com.example.goforlunch.bottomfragments.WorkmatesFragment;
import com.example.goforlunch.databinding.ActivityMainBinding;


//import com.example.goforlunch.manager.UserManager;

import com.example.goforlunch.databinding.ActivityNavHeaderBinding;
import com.example.goforlunch.model.User;
import com.example.goforlunch.model.restaurants.Viewport;
import com.example.goforlunch.repository.UserInjection;
import com.example.goforlunch.repository.UserViewModelFactory;
import com.example.goforlunch.ui.viewmodel.UserViewModel;
import com.example.goforlunch.ui.views.ConnexionActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Array;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLEngineResult;

import io.reactivex.internal.operators.observable.ObservableOnErrorReturn;

import static android.content.ContentValues.TAG;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private ActivityMainBinding binding;
    private Fragment mapsViewFragment;
    private Fragment listViewFragment;
    private Fragment workmatesFragment;
    private Fragment settingFragment;
    private DrawerLayout drawerLayout;
    private UserViewModel mUserViewModel;
    private static final int MAPS_FRAGMENT = 0;
    private static final int RESTAURANT_FRAGMENT = 1;
    private static final int USER_FRAGMENT = 2;
    private final int AUTOCOMPLETE_REQUEST_CODE = 26;
    private int mFragmentIdentifier;
    private Toolbar toolbar;
    private TextView currentUserName;
    private TextView currentUserEmail;
    private ImageView currentUserImage;
    private User mUser = new User("","","","",
            "",null,"","");



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        Places.initialize(this, BuildConfig.API_KEY);
        PlacesClient placesClient = Places.createClient(this);
        this.configureUserViewModel();
        this.showFragment(MAPS_FRAGMENT);
        this.configureToolBar();
        this.configureBottomView();
        this.configureNavigationView();
        this.configureDrawerLayout();
        this.updateUIWhenCreating();
        setContentView(view);
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onNavigationBottomItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.map_view :
                this.showFragment(MAPS_FRAGMENT);
                break;
            case R.id.list_view :
                this.showFragment(RESTAURANT_FRAGMENT);
                break;
            case R.id.workmates :
                this.showFragment(USER_FRAGMENT);
                break;
        }
        return true;
    }

    private void showFragment (int fragmentIdentifier) {
        switch (fragmentIdentifier) {
            case MAPS_FRAGMENT :
                this.showMapsFragment();
                break;
            case RESTAURANT_FRAGMENT :
                this.showRestaurantFragment();
                break;
            case USER_FRAGMENT :
                this.showUserFragment();
                break;
        }
        mFragmentIdentifier = fragmentIdentifier;
    }

    private void showMapsFragment() {
        if (this.mapsViewFragment == null) this.mapsViewFragment = MapsViewFragment.newInstance();
        binding.bannerTitle.setText(R.string.default_banner);
        binding.searchBtn.setColorFilter(getResources().getColor(R.color.white));
        binding.searchBtn.setEnabled(true);
        this.startTransactionFragment(this.mapsViewFragment);
    }

    private void showRestaurantFragment() {
        if (this.listViewFragment == null) this.listViewFragment =
                ListViewFragment.newInstance(1);
        binding.bannerTitle.setText(R.string.default_banner);
        binding.searchBtn.setColorFilter(getResources().getColor(R.color.white));
        binding.searchBtn.setEnabled(true);
        this.startTransactionFragment(listViewFragment);
    }

    private void showUserFragment() {
        if (this.workmatesFragment == null) this.workmatesFragment = WorkmatesFragment.newInstance(1);
        binding.bannerTitle.setText(R.string.available_work);
        binding.searchBtn.setEnabled(false);
        binding.searchBtn.setColorFilter(getResources().getColor(R.color.primary_color));
        this.startTransactionFragment(workmatesFragment);
    }

    private void showSettingsFragment() {
        if(this.settingFragment == null) this.settingFragment = SettingFragment.newInstance();
        binding.bannerTitle.setText(R.string.preference);
        binding.searchBtn.setEnabled(false);
        binding.searchBtn.setColorFilter(getResources().getColor(R.color.primary_color));
        this.startTransactionFragment(settingFragment);
    }

    private void startTransactionFragment (Fragment fragment) {
        if (!fragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, fragment).commit();
        }
    }

    private void configureToolBar() {
        this.toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        ImageButton searchBtn = binding.searchBtn;
        searchBtn.setOnClickListener(v-> onSearchCalled());
    }

    private void configureBottomView() {
        BottomNavigationView bottomNavigationView = binding.navBottomBar;
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationBottomItemSelected);
    }

    private void configureDrawerLayout() {
        this.drawerLayout = binding.drawer;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_24);
    }

    private void configureNavigationView() {
        NavigationView navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);
        com.example.goforlunch.databinding.ActivityNavHeaderBinding navHeaderBinding =
                ActivityNavHeaderBinding.inflate(LayoutInflater.from(navigationView.getContext()));
        navigationView.addHeaderView(navHeaderBinding.getRoot());
        this.currentUserName = navHeaderBinding.nameProfile;
        this.currentUserEmail = navHeaderBinding.emailProfile;
        this.currentUserImage = navHeaderBinding.imageProfile;
    }

    private void signOut() {
        AuthUI.getInstance().signOut(this);
        Intent intent = new Intent(this, ConnexionActivity.class);
        startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.your_lunch :
                getYourLunch();
                break;
            case R.id.settings :
                showSettingsFragment();
                break;
            case R.id.logout :
                signOut();
                break;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK && mFragmentIdentifier == RESTAURANT_FRAGMENT) {
                if (Objects.requireNonNull(Autocomplete.getPlaceFromIntent
                        (Objects.requireNonNull(data)).getTypes()).contains(Place.Type.RESTAURANT)) {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    String placeId = place.getId();
                    Intent intent = new Intent(this, DetailActivity.class);
                    intent.putExtra("place Id", placeId);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this, R.string.choose_restaurant, Toast.LENGTH_SHORT).show();
                }
            }else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(Objects.requireNonNull(data));
                Toast.makeText(this, "Error" + status.getStatusMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.i(TAG, status.getStatusMessage());
            }
            else if (resultCode == RESULT_OK && mFragmentIdentifier == MAPS_FRAGMENT) {
                if (Objects.requireNonNull(Autocomplete.getPlaceFromIntent
                        (Objects.requireNonNull(data)).getTypes()).contains(Place.Type.RESTAURANT)) {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    double placeLatitude = Objects.requireNonNull(place.getLatLng()).latitude;
                    double placeLongitude = place.getLatLng().longitude;
                    String placeId = place.getId();
                    Bundle bundle = new Bundle();
                    bundle.putDouble("placeLatitude", placeLatitude);
                    bundle.putDouble("placeLongitude", placeLongitude);
                    bundle.putString("place Id", placeId);
                    MapsViewFragment mapsViewFragment = new MapsViewFragment();
                    mapsViewFragment.setArguments(bundle);
                    startTransactionFragment(mapsViewFragment);
                }
                else {
                    Toast.makeText(this, R.string.choose_restaurant, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void onSearchCalled() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.TYPES);

        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields).setCountry("FR")
                .setHint(getString(R.string.hint_search_bar)).setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setInitialQuery("restaurant")
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Nullable
    protected FirebaseUser getCurrentUser() {return FirebaseAuth.getInstance().getCurrentUser();}

    protected boolean isCurrentUserLogged() {return (this.getCurrentUser() !=null);}

    @RequiresApi (api = Build.VERSION_CODES.O)
    private void setUser(User user) {
        mUser = user;
        if (isCurrentUserLogged()) {
            if (Objects.requireNonNull(this.getCurrentUser()).getPhotoUrl() != null) {
                Glide.with(this)
                        .load(this.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(currentUserImage);
            }
            String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ?
                    getString(R.string.info_no_email_found) : this.getCurrentUser().getEmail();
            String username = TextUtils.isEmpty(this.getCurrentUser().getDisplayName()) ?
                    getString(R.string.info_no_username_found) : this.getCurrentUser().getDisplayName();
            currentUserName.setText(username);
            currentUserEmail.setText(email);
            this.useWorker();
            if (mUser == null || mUser.getUid() == null || mUser.getUid().equals("")) {
                this.mUserViewModel.createCurrentUser(this);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateUIWhenCreating() {
        String uid = Objects.requireNonNull(getCurrentUser()).getUid();
        this.mUserViewModel.getUser(uid,this).observe(this, this::setUser);
    }

    private void getYourLunch() {
        if (mUser.getRestaurant() != null && !mUser.getRestaurant().equals("")) {
            String placeId = mUser.getRestaurant();
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("placeId", placeId);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.choose_restaurant, Toast.LENGTH_SHORT).show();
        }
    }

    //private void getYourLunch() {
    //    mUserViewModel.getUser(getCurrentUser().getUid(),this).observe(this, new Observer<User>() {
    //        @Override
    //        public void onChanged(User user) {
    //            mUser = user;
    //            if (mUser.getRestaurant() !=null && !mUser.getRestaurant().equals("")) {
    //                String placeId = mUser.getRestaurant();
    //                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
    //                intent.putExtra("place Id", placeId);
    //                startActivity(intent);
    //            }
    //            else {
    //                Toast.makeText(getApplicationContext(), R.string.choose_restaurant, Toast.LENGTH_SHORT)
    //                        .show();
    //            }
//
    //        }
    //    });
//
    //}

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        updateUIWhenCreating();
    }

    private void configureUserViewModel() {
        UserViewModelFactory mViewModelFactory = UserInjection.provideViewModelFactory();
        this.mUserViewModel = new ViewModelProvider(this, mViewModelFactory)
                .get(UserViewModel.class);
        this.mUserViewModel.initUsers(this);
    }

    @RequiresApi (api = Build.VERSION_CODES.O)
    private void useWorker() {
        long timeNow = ZonedDateTime.now(ZoneId.systemDefault()).getHour();
        long hourAlarm = 12;
        long delay;
        if (timeNow <= hourAlarm) {
            delay = hourAlarm - timeNow;
        }
        else {
            delay = hourAlarm + (24-timeNow);
        }
        OneTimeWorkRequest myWork = new OneTimeWorkRequest.Builder(Worker.class)
                .setInitialDelay(delay, TimeUnit.HOURS).build();
        WorkManager.getInstance(this).enqueueUniqueWork(TAG, ExistingWorkPolicy.REPLACE,myWork);
    }




   //@Override
   //protected ActivityMainBinding getViewBinding() {
   //    return ActivityMainBinding.inflate(getLayoutInflater());
   //}

   //@Override
   //public void onCreate(Bundle savedInstanceState) {
   //    super.onCreate(savedInstanceState);
   //    startSignInActivity();
   //}

   ///*private void setupListeners(){
   //    //login button
   //    binding.loginButton.setOnClickListener(view -> {
   //        startSignInActivity();
   //    });

   //}*/




   //private void startSignInActivity() {
   //    List<AuthUI.IdpConfig> providers = Arrays.asList(
   //            new AuthUI.IdpConfig.EmailBuilder().build(),
   //            new AuthUI.IdpConfig.GoogleBuilder().build());

   //    // Launch the activity
   //    startActivityForResult(
   //            AuthUI.getInstance()
   //                    .createSignInIntentBuilder()
   //                    .setTheme(R.style.AppTheme)
   //                    .setAvailableProviders(providers)
   //                    .setIsSmartLockEnabled(false, true)
   //                    .build(),
   //            RC_SIGN_IN);
   //}

   //@Override
   //protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
   //    super.onActivityResult(requestCode, resultCode, data);
   //    this.handleResponseAfterSignIn (requestCode, resultCode, data);
   //}


   //private void handleResponseAfterSignIn (int requestCode, int resultCode, Intent data) {

   //    IdpResponse response = IdpResponse.fromResultIntent(data);

   //    if (requestCode == RC_SIGN_IN) {
   //        //SUCCESS
   //        if (resultCode == RESULT_OK) {

   //            showSnackBar(getString(R.string.connection_succeed));
   //            Intent intent = new Intent(this, MenuActivity.class);
   //            //UserManager.getInstance().createUser();
   //            startActivity (intent);
   //        } else {
   //            //ERROR
   //            if (response == null) {
   //                showSnackBar(getString(R.string.error_authentication_canceled));
   //            } else if (response.getError() != null) {
   //                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
   //                    showSnackBar(getString(R.string.error_no_internet));
   //                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
   //                    showSnackBar(getString(R.string.error_unknown_error));
   //                }
   //            }
   //        }

   //    }

   //}
   //private void showSnackBar (String message) {
   //    Snackbar.make(binding.mainLayout, message, Snackbar.LENGTH_SHORT).show();
   //}

}