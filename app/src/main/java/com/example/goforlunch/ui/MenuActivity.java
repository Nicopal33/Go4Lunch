package com.example.goforlunch.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import com.example.goforlunch.R;
import com.example.goforlunch.bottomfragments.MapViewFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;

public class MenuActivity extends AppCompatActivity {

    private Fragment mapViewFragment;



    //1 FOR DESIGN
    @BindView(R.id.activity_menu_bottom_navigation) BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        this.configureBottomView();
    }

    private void configureAndShowMainFragment (){
        mapViewFragment = (MapViewFragment) getSupportFragmentManager().findFragmentById(R.id.frameLayout)
    }

    // 2 - Configure BottomNavigationView Listener
    private void configureBottomView(){
        bottomNavigationView.setOnNavigationItemSelectedListener (item -> updateMapViewFragment (item.getItemId()));
    }

    // 3 - Update Main Fragment design
    /*
    private Boolean updateMainFragment (Integer integer){
        switch (integer) {
            case R.id.mapViewFragment  :
                this.mapViewFragment.updateDesignWhenUserClickBottomView(MapViewFragment.REQUEST_MAPVIEW);
                break;
        }*/


}