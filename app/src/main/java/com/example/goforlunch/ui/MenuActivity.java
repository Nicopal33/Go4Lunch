package com.example.goforlunch.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.example.goforlunch.R;
import com.example.goforlunch.bottomfragments.MapViewFragment;
import com.example.goforlunch.databinding.ActivityMenuBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MenuActivity extends BaseActivity {

    private ActivityMenuBinding binding;
    private Fragment mapViewFragment;
    private Fragment listViewFragment;
    private Fragment worlkmatesFragment;
    BottomNavigationView  bottomNavigationView ;
    private static final int MAPS_FRAGMENT = 0;
    private int mFragmentIdentifier;



    @Override
    protected ActivityMenuBinding getViewBinding() {
        return ActivityMenuBinding.inflate(getLayoutInflater());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.configureBottomView();

    }

    public boolean onNavigationBottomItemSelected (MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.mapFragment :
                this.showFragment(MAPS_FRAGMENT);
                break;
        }
        return true;
    }

    private void showFragment (int fragmentIdentifier) {
        switch (fragmentIdentifier) {
            case MAPS_FRAGMENT :
                this.showMapsFragment();
                break;
        }
        mFragmentIdentifier = fragmentIdentifier;

    }

    private void showMapsFragment() {
        if (this.mapViewFragment == null) this.mapViewFragment = MapViewFragment.newInstance();
    }



    // 2 - Configure BottomNavigationView Listener
    private void configureBottomView() {
        BottomNavigationView bottomNavigationView = binding.navBottomBar;
        bottomNavigationView.setOnNavigationItemSelectedListener
                (this::onNavigationBottomItemSelected);
    }







}