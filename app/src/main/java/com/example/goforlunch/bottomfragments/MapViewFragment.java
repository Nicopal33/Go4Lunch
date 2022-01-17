package com.example.goforlunch.bottomfragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.goforlunch.model.restaurants.RestauOutputs;
import com.example.goforlunch.model.restaurants.ResultRestau;
import com.example.goforlunch.ui.viewmodel.NearbyInjection;
import com.example.goforlunch.ui.viewmodel.NearbyRestViewModel;
import com.example.goforlunch.ui.viewmodel.NearbyRestViewModelFactory;
import com.example.goforlunch.ui.viewmodel.NearbyViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.goforlunch.BuildConfig;
import com.example.goforlunch.R;
import com.google.android.material.snackbar.Snackbar;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapViewFragment extends Fragment
        implements OnMapReadyCallback {

    private GoogleMap map;
    private static final String TAG = MapViewFragment.class.getSimpleName();

    private FusedLocationProviderClient fusedLocationClient;
    private Location lastKnownLocation;
    private final int PERMISSION_ID = 15;
    List<ResultRestau> restaurantList = new ArrayList<>();
    private NearbyRestViewModel mNearbyRestViewModel;




    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;


    public MapViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MapViewFragment.
     */
    public static MapViewFragment newInstance() {
        return new MapViewFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_map_view, container, false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireContext());
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        configureNearbyRestViewModel();



        return root;
        
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
        map=googleMap;
        getLastLocation();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {

        }else {
            requestPermissions();

        }
    }


    ///BBBBBBBBBBBBBBB


    private boolean checkPermissions() {
        return (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context");
            showSnackBar(Integer.parseInt("Location permission is needed for core functionaly"), android.R.string.ok, view -> startLocationPermissionRequest());
        } else {
            Log.i(TAG, "Requesting permission");
            startLocationPermissionRequest();
        }
    }

    private void showSnackBar(final int mainTextStringId, final int actionStringId, View.OnClickListener listener) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }


    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_ID);
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    map.setMyLocationEnabled(true);


                }
            }
        }

    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            map.setMyLocationEnabled(true);
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
            fusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                lastKnownLocation = task.getResult();
                LatLng mLastKnownLocationLatLng = new
                        LatLng(Objects.requireNonNull(lastKnownLocation).getLatitude(), lastKnownLocation.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(mLastKnownLocationLatLng,10));
                ////////
                getPlaces();
            });
        }else {
            requestPermissions();
        }
    }

    private void getPlaces() {
        String location = lastKnownLocation.getLatitude() +"," + lastKnownLocation.getLongitude();
        mNearbyRestViewModel.getRestaurantsList(location, "1000", BuildConfig.API_KEY).observe(getViewLifecycleOwner(),
                this::getRestaurants);

    }

    @RequiresApi (api= Build.VERSION_CODES.LOLLIPOP)
    private void getRestaurants (RestauOutputs restaurants) {
        if ((restaurants !=null)) {
            restaurantList = restaurants.getResults();
            for (ResultRestau restaurant : restaurantList) {
                LatLng latLng = new LatLng(restaurant.getGeometry().getLocation().getLatitude(),
                        restaurant.getGeometry().getLocation().getLongitude());
                String id = restaurant.getPlaceId();

            }
        }

    }

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
   // private void setMarker (LatLng latLng, String id) {
      //  mUser
    //}

    private void configureNearbyRestViewModel() {
        NearbyViewModelFactory nearbyRestViewModelFactory = NearbyInjection.provideRestaurantViewModel();
        mNearbyRestViewModel = new ViewModelProvider(this, nearbyRestViewModelFactory).get(NearbyRestViewModel.class);
    }



}


