package com.example.goforlunch.bottomfragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.goforlunch.R;
import com.example.goforlunch.databinding.FragmentSettingBinding;
import com.example.goforlunch.repository.UserInjection;
import com.example.goforlunch.repository.UserViewModelFactory;
import com.example.goforlunch.ui.viewmodel.UserViewModel;
import com.example.goforlunch.ui.views.ConnexionActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class SettingFragment extends Fragment {

    public static SettingFragment newInstance() {return (new SettingFragment());}

    private FragmentSettingBinding  mFragmentSettingBinding;
    private UserViewModel mUserViewModel;
    private Button mSuppressButton;

    private final FirebaseUser currentUser = Objects.requireNonNull(FirebaseAuth.getInstance()
    .getCurrentUser());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        mFragmentSettingBinding = FragmentSettingBinding.inflate(inflater, container, false);
        createUI();
        parametersListeners();
        return mFragmentSettingBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureUserViewModel();
    }

    public void createUI() {
        mSuppressButton = mFragmentSettingBinding.suppressBtn;
    }

    public void parametersListeners() {
        String currentUserId = currentUser.getUid();
        mSuppressButton.setOnClickListener(v-> {
            mUserViewModel.deleteUser(currentUserId, this.getContext());
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this.getActivity(), ConnexionActivity.class);
            startActivity(intent);
        });
    }

    private void configureUserViewModel() {
        UserViewModelFactory mViewModelFactory = UserInjection.provideViewModelFactory();
        this.mUserViewModel = new ViewModelProvider(this, mViewModelFactory)
                .get(UserViewModel.class);
        this.mUserViewModel.initUsers(this.requireContext());
    }






}