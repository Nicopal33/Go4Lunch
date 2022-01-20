package com.example.goforlunch.bottomfragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.goforlunch.databinding.FragmentMapViewBinding;
import com.example.goforlunch.model.User;
import com.example.goforlunch.R;
import com.example.goforlunch.model.restaurants.RestauOutputs;
import com.example.goforlunch.model.restaurants.ResultRestau;
import com.example.goforlunch.repository.UserInjection;
import com.example.goforlunch.repository.UserViewModelFactory;
import com.example.goforlunch.ui.DetailActivity;
import com.example.goforlunch.ui.viewmodel.NearbyInjection;
import com.example.goforlunch.ui.viewmodel.NearbyRestViewModel;
import com.example.goforlunch.BuildConfig;
import com.example.goforlunch.ui.viewmodel.NearbyViewModelFactory;
import com.example.goforlunch.ui.viewmodel.UserViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.goforlunch.BuildConfig;
import com.example.goforlunch.R;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.ProgressDialog.show;


public class MapsViewFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FragmentMapViewBinding mMapsBinding;
    private final int PERMISSION_ID = 26;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final int DEFAULT_ZOOM = 16;
    private Location mLastKnownLocation;
    private boolean mLocationPermission;
    private NearbyRestViewModel mNearbyRestaurantViewModel;
    private double placeLatitude;
    private double placeLongitude;
    private LatLng placePosition;
    private String placeIdFromSearch = "";
    private UserViewModel mUserViewModel;
    List<ResultRestau> restaurantsList = new ArrayList<>();
    private final List<User> mUsers = new ArrayList<>();

    public static MapsViewFragment newInstance() {
        return (new MapsViewFragment());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            placeLatitude = requireArguments().getDouble("placeLatitude");
            placeLongitude = requireArguments().getDouble("placeLongitude");
            placeIdFromSearch = requireArguments().getString("placeId");
            placePosition = new LatLng(placeLatitude, placeLongitude);
        }
        mMapsBinding = FragmentMapViewBinding.inflate(inflater, container, false);
        configureNearbyRestaurantViewModel();
        configureUserViewModel();
        return mMapsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            placeLatitude = requireArguments().getDouble("placeLatitude");
            placeLongitude = requireArguments().getDouble("placeLongitude");
            placeIdFromSearch = requireArguments().getString("placeId");
            placePosition = new LatLng(placeLatitude, placeLongitude);
        }
        configureUserViewModel();
        configureNearbyRestaurantViewModel();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient
                (this.requireContext());
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        getLastLocation();
        mMapsBinding.position.setOnClickListener(v -> getLastLocation());
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (placePosition != null) {
            getPlaceSearched();
        }
        else {
            getLastLocation();
        }
    }

    public void getLocationPermission(Context context, Activity activity) {
        mLocationPermission = false;
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermission = true;
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ID);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (mLocationPermission) {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient
                    (requireContext());
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                mLastKnownLocation = task.getResult();
                LatLng mLastKnownLocationLatLng = new
                        LatLng(Objects.requireNonNull(mLastKnownLocation).getLatitude(),
                        mLastKnownLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        mLastKnownLocationLatLng, DEFAULT_ZOOM));
                getPlaces();
            });
        }else {
            getLocationPermission(requireContext(), requireActivity());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getPlaceSearched() {
        if (mMap != null) {
            mMap.clear();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placePosition, DEFAULT_ZOOM));
            setMarker(placePosition, placeIdFromSearch);
            mMap.setOnMarkerClickListener(marker -> {
                Intent intent = new Intent(this.getContext(), DetailActivity.class);
                intent.putExtra("placeId", Objects.requireNonNull(placeIdFromSearch));
                startActivity(intent);
                return false;
            });
        }
    }

    private void getPlaces() {
        String location = mLastKnownLocation.getLatitude() + "," + mLastKnownLocation.getLongitude();
        mNearbyRestaurantViewModel.getRestaurantsList(location, "1000",
                BuildConfig.API_KEY).observe(getViewLifecycleOwner(),
                this::getRestaurants);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getRestaurants(RestauOutputs restaurants) {

        if (restaurants != null) {
            restaurantsList = restaurants.getResults();
            for (ResultRestau restaurant : restaurantsList) {
                LatLng latLng = new LatLng(restaurant.getGeometry().getLocation().getLat(),
                        restaurant.getGeometry().getLocation().getLng());
                String id = restaurant.getPlaceId();
                setMarker(latLng, id);
            }
            mMap.setOnMarkerClickListener(marker -> {
                Intent intent = new Intent(requireContext(), DetailActivity.class);
                intent.putExtra("placeId", Objects.requireNonNull(marker.getTag())
                        .toString());
                startActivity(intent);
                return false;
            });
        }
        else {
            Toast.makeText(this.requireContext(), "There is no restaurants near you",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setMarker(LatLng latLng, String id) {
        mUserViewModel.getUsers().observe(this,
                users -> setUsers(users, latLng, id));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private BitmapDescriptor getBitmapDescriptor(int id) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
        Bitmap bitmap = Bitmap.createBitmap(Objects.requireNonNull(vectorDrawable)
                        .getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, 75, 100);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUsers(List<User> users, LatLng latLng, String id) {
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
        Objects.requireNonNull(marker).setTag(id);
        for (User user : users) {
            if (user.getRestaurant() != null && user.getRestaurant().equals(marker.getTag())) {
                mUsers.add(user);
            }
            else {
                mUsers.remove(user);
            }
        }
        if (mUsers.size() != 0) {
            marker.setIcon(getBitmapDescriptor(R.drawable.marker_green));
        }
        else {
            marker.setIcon(getBitmapDescriptor(R.drawable.marker_orange));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume() {
        super.onResume();
        if (mLocationPermission && placePosition == null) {
            getLastLocation();
        }
        else if (placePosition != null) {
            getPlaceSearched();
        }
    }

    private void configureNearbyRestaurantViewModel() {
        NearbyViewModelFactory nearbyViewModelFactory = NearbyInjection.provideRestaurantViewModel();
        mNearbyRestaurantViewModel = new ViewModelProvider(this, nearbyViewModelFactory)
                .get(NearbyRestViewModel.class);
    }

    private void configureUserViewModel() {
        UserViewModelFactory mViewModelFactory = UserInjection.provideViewModelFactory();
        this.mUserViewModel = new ViewModelProvider(this, mViewModelFactory)
                .get(UserViewModel.class);
        this.mUserViewModel.initUsers(this.requireContext());
    }


//lic class MapViewFragment extends Fragment
//     implements OnMapReadyCallback {

// private GoogleMap map;
// private static final String TAG = MapViewFragment.class.getSimpleName();

// private FusedLocationProviderClient fusedLocationClient;
// private Location lastKnownLocation;
// private final int PERMISSION_ID = 15;
// List<ResultRestau> restaurantList = new ArrayList<>();
// private NearbyRestViewModel mNearbyRestViewModel;




// private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;


// public MapViewFragment() {
//     // Required empty public constructor
// }

// /**
//  * Use this factory method to create a new instance of
//  * this fragment using the provided parameters.
//  *
//  * @return A new instance of fragment MapViewFragment.
//  */
// public static MapViewFragment newInstance() {
//     return new MapViewFragment();
// }


// @Override
// public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                          Bundle savedInstanceState) {
//     // Inflate the layout for this fragment
//     View root= inflater.inflate(R.layout.fragment_map_view, container, false);
//     fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireContext());
//     SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
//             .findFragmentById(R.id.mapFragment);
//     if (mapFragment != null) {
//         mapFragment.getMapAsync(this);
//     }
//     configureNearbyRestViewModel();



//     return root;
//
// }


// @Override
// public void onMapReady(@NonNull GoogleMap googleMap) {
//     googleMap.addMarker(new MarkerOptions()
//             .position(new LatLng(0, 0))
//             .title("Marker"));
//     map=googleMap;
//     getLastLocation();

// }

// @Override
// public void onResume() {
//     super.onResume();
//     if (checkPermissions()) {

//     }else {
//         requestPermissions();

//     }
// }


// ///BBBBBBBBBBBBBBB


// private boolean checkPermissions() {
//     return (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//             == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(requireContext(),
//             Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
// }

// private void requestPermissions() {
//     boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
//             Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
//             Manifest.permission.ACCESS_COARSE_LOCATION);
//     if (shouldProvideRationale) {
//         Log.i(TAG, "Displaying permission rationale to provide additional context");
//         showSnackBar(Integer.parseInt("Location permission is needed for core functionaly"), android.R.string.ok, view -> startLocationPermissionRequest());
//     } else {
//         Log.i(TAG, "Requesting permission");
//         startLocationPermissionRequest();
//     }
// }

// private void showSnackBar(final int mainTextStringId, final int actionStringId, View.OnClickListener listener) {
//     Snackbar.make(requireActivity().findViewById(android.R.id.content),
//             getString(mainTextStringId),
//             Snackbar.LENGTH_INDEFINITE)
//             .setAction(getString(actionStringId), listener).show();
// }


// private void startLocationPermissionRequest() {
//     ActivityCompat.requestPermissions(requireActivity(),
//             new String[]{
//                     Manifest.permission.ACCESS_FINE_LOCATION,
//                     Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_ID);
// }

// public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//     super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//     if (requestCode == PERMISSION_ID) {
//         if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//             if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                     && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
//                     != PackageManager.PERMISSION_GRANTED) {
//                 // TODO: Consider calling
//                 //    ActivityCompat#requestPermissions
//                 // here to request the missing permissions, and then overriding
//                 //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                 //                                          int[] grantResults)
//                 // to handle the case where the user grants the permission. See the documentation
//                 // for ActivityCompat#requestPermissions for more details.
//                 map.setMyLocationEnabled(true);


//             }
//         }
//     }

// }


// @SuppressLint("MissingPermission")
// private void getLastLocation() {
//     if (checkPermissions()) {
//         map.setMyLocationEnabled(true);
//         fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
//         fusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
//             lastKnownLocation = task.getResult();
//             LatLng mLastKnownLocationLatLng = new
//                     LatLng(Objects.requireNonNull(lastKnownLocation).getLatitude(), lastKnownLocation.getLongitude());
//             map.moveCamera(CameraUpdateFactory.newLatLngZoom(mLastKnownLocationLatLng,10));
//             ////////
//             getPlaces();
//         });
//     }else {
//         requestPermissions();
//     }
// }

// private void getPlaces() {
//     String location = lastKnownLocation.getLatitude() +"," + lastKnownLocation.getLongitude();
//     mNearbyRestViewModel.getRestaurantsList(location, "1000", BuildConfig.API_KEY).observe(getViewLifecycleOwner(),
//             this::getRestaurants);

// }

// @RequiresApi (api= Build.VERSION_CODES.LOLLIPOP)
// private void getRestaurants (RestauOutputs restaurants) {
//     if ((restaurants !=null)) {
//         restaurantList = restaurants.getResults();
//         for (ResultRestau restaurant : restaurantList) {
//             LatLng latLng = new LatLng(restaurant.getGeometry().getLocation().getLatitude(),
//                     restaurant.getGeometry().getLocation().getLongitude());
//             String id = restaurant.getPlaceId();

//         }
//     }

// }

// //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//// private void setMarker (LatLng latLng, String id) {
//   //  mUser
// //}

// private void configureNearbyRestViewModel() {
//     NearbyViewModelFactory nearbyRestViewModelFactory = NearbyInjection.provideRestaurantViewModel();
//     mNearbyRestViewModel = new ViewModelProvider(this, nearbyRestViewModelFactory).get(NearbyRestViewModel.class);
// }



}


