package com.example.goforlunch.bottomfragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.goforlunch.BuildConfig;
import com.example.goforlunch.R;
import com.example.goforlunch.adapter.ListRestRecyclerViewAdapter;
//import com.example.goforlunch.adapter.ListWorkRecyclerViewAdapter;
import com.example.goforlunch.databinding.FragmentListViewBinding;
import com.example.goforlunch.model.restaurants.RestauOutputs;
import com.example.goforlunch.model.restaurants.ResultRestau;
import com.example.goforlunch.ui.viewmodel.NearbyInjection;
import com.example.goforlunch.ui.viewmodel.NearbyRestViewModel;
import com.example.goforlunch.ui.viewmodel.NearbyViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.protobuf.DescriptorProtos;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_COLUMN_COUNT ="column_count";
    private boolean mLocationPermission;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnowLocation;
    private NearbyRestViewModel mNearbyRestViewModel;
    private final List<ResultRestau> mRestaurants = new ArrayList<>();
    private final ListRestRecyclerViewAdapter mAdapter = new ListRestRecyclerViewAdapter(mRestaurants);

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListViewFragment() {
    }


    public static ListViewFragment newInstance(int columnCount) {
        ListViewFragment fragment = new ListViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireContext());
        this.getPlaces();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.example.goforlunch.databinding.FragmentListViewBinding fragmentListViewBinding
                = FragmentListViewBinding.inflate(inflater, container, false);
        RecyclerView view = fragmentListViewBinding.getRoot();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
                (this.requireContext()));
        Context context = view.getContext();
        view.setLayoutManager(new LinearLayoutManager(context));
        view.setAdapter(mAdapter);
        this.configureNearbyRestViewModel();
            this.getPlaces();

        return view;
    }

    @SuppressLint("MissingPermission")
    private void getPlaces() {
        if (mLocationPermission) {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                mLastKnowLocation = task.getResult();
                LatLng mLastKnowLocationLatLng = new LatLng(mLastKnowLocation.getLatitude(), mLastKnowLocation.getLongitude());
                String mLastKnowLocationString = mLastKnowLocationLatLng.toString();
                mNearbyRestViewModel.getRestaurantsList(mLastKnowLocationString, "1000", BuildConfig.API_KEY).
                        observe(getViewLifecycleOwner(), this::getRestaurants);
            });
        } else {
            getLocationPermission(requireContext(), requireActivity());
        }

    }

    private void getRestaurants (RestauOutputs restaurants) {
        if (restaurants != null) {
            if (!mRestaurants.containsAll(restaurants.getResults())) {
                mRestaurants.addAll(restaurants.getResults());
                mAdapter.notifyDataSetChanged();
            }
        }
        else {
            Toast.makeText(this.requireContext(), getString(R.string.no_rest_found),
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        configureNearbyRestViewModel();
        getPlaces();
    }

    public void getLocationPermission (Context context, Activity activity) {
        mLocationPermission = false;
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermission = true;
        } else {
            int PERMISSION_ID = 26;
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
        }


    }
            public void configureNearbyRestViewModel() {
        NearbyViewModelFactory nearbyViewModelFactory =
                NearbyInjection.provideRestaurantViewModel();
        mNearbyRestViewModel = new ViewModelProvider(this, nearbyViewModelFactory).get(NearbyRestViewModel.class);
    }


}