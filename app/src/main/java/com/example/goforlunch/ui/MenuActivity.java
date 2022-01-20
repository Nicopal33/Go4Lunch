package com.example.goforlunch.ui;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.viewbinding.ViewBinding;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.goforlunch.R;
//import com.example.goforlunch.bottomfragments.MapViewFragment;
import com.example.goforlunch.databinding.ActivityMenuBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MenuActivity extends BaseActivity {


    BottomNavigationView  bottomNavigationView ;




    @Override
    protected ActivityMenuBinding getViewBinding() {
        return ActivityMenuBinding.inflate(getLayoutInflater());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.showFragment(MAPS_FRAGMENT);//
        setUpNavigation();

    }

    /*public boolean onNavigationBottomItemSelected (MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.mapFragment :
                this.showFragment(MAPS_FRAGMENT);
                break;
            case R.id.listFragment :
                this.showFragment(LIST_FRAGMENT);
                break;
            case R.id.workFragment :
                this.showFragment(WORK_FRAGMENT);
                break;
        }
        return true;
    }*/

    /*private void showFragment (int fragmentIdentifier) {
        switch (fragmentIdentifier) {
            case MAPS_FRAGMENT :
                this.showMapsFragment();
                break;
        }
        mFragmentIdentifier = fragmentIdentifier;

    }*/

    /*private void showMapsFragment() {
        if (this.mapViewFragment == null) this.mapViewFragment = MapViewFragment.newInstance();
        this.startTransactionFragment(this.mapViewFragment);
    }

    private void startTransactionFragment(Fragment fragment) {
        if (!fragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mapFragment,fragment).commit();
        }
    }*/





    public void setUpNavigation(){
        bottomNavigationView =findViewById(R.id.nav_bottom_bar );
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id. frameLayout );
        NavigationUI. setupWithNavController (bottomNavigationView,
                navHostFragment.getNavController());
    }










}