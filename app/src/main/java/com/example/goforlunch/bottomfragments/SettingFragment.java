package com.example.goforlunch.bottomfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.goforlunch.R;
import com.example.goforlunch.ui.viewmodel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class SettingFragment extends Fragment {

    public static SettingFragment newInstance() {return (new SettingFragment());}

    private UserViewModel mUserViewModel;
    private Button mSuppressButton;

    private final FirebaseUser currentUser = Objects.requireNonNull(FirebaseAuth.getInstance()
    .getCurrentUser());


}