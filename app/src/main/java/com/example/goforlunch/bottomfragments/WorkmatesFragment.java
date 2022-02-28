package com.example.goforlunch.bottomfragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.goforlunch.R;
//import com.example.goforlunch.adapter.ListWorkRecyclerViewAdapter;
import com.example.goforlunch.adapter.UserRecyclerViewAdapter;
import com.example.goforlunch.databinding.FragmentWorkmatesBinding;
import com.example.goforlunch.databinding.FragmentWorkmatesBinding;
//import com.example.goforlunch.manager.UserManager;
import com.example.goforlunch.model.User;
import com.example.goforlunch.repository.UserInjection;
import com.example.goforlunch.repository.UserViewModelFactory;
import com.example.goforlunch.ui.viewmodel.UserViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.grpc.NameResolver;


public class WorkmatesFragment  extends Fragment  {

    public static final String ARG_COLUMN_COUNT = "column-count";
    private UserViewModel mViewModel;
    private final List<User> users = new ArrayList<>();
    private final UserRecyclerViewAdapter adapter = new UserRecyclerViewAdapter(users);

    public WorkmatesFragment() {}

    public static WorkmatesFragment newInstance(int columncount) {
        WorkmatesFragment fragment = new WorkmatesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columncount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate (Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        this.configureViewModel();
        this.getUsers();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        com.example.goforlunch.databinding.FragmentWorkmatesBinding workmatesFragmentBinding =
                FragmentWorkmatesBinding.inflate(inflater, container,false);
        RecyclerView view = workmatesFragmentBinding.getRoot();
        Context context = view.getContext();
        view.setLayoutManager(new LinearLayoutManager(context));
        view.setAdapter(adapter);
        return view;
    }

    private void configureViewModel() {
        UserViewModelFactory mViewModelFactory = UserInjection.provideViewModelFactory();
        this.mViewModel = new ViewModelProvider(this,mViewModelFactory)
                .get(UserViewModel.class);
        this.mViewModel.initUsers(this.getContext());
    }

    private void updateUsers (List<User> users) {
           this.users.clear();
           this.users.addAll(users);
           adapter.notifyDataSetChanged();
        }

    private void getUsers() {
        if (this.mViewModel.getUsers() != null) {
            this.mViewModel.getUsers().observe(this, this::updateUsers);
        }
    }



}