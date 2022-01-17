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
import com.example.goforlunch.adapter.ListWorkRecyclerViewAdapter;
import com.example.goforlunch.databinding.FragmentWorlkmatesBinding;
import com.example.goforlunch.manager.UserManager;
import com.example.goforlunch.model.User;
import com.example.goforlunch.repository.UserInjection;
import com.example.goforlunch.repository.UserViewModelFactory;
import com.example.goforlunch.ui.viewmodel.UserViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;


public class WorlkmatesFragment  extends Fragment  {

    private RecyclerView recyclerView;
    private UserManager userManager = UserManager.getInstance();
    private final List<User> users = new ArrayList<>();
    public UserViewModel mViewModel;
    private  ListWorkRecyclerViewAdapter adapter;


    public WorlkmatesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.configureViewModel();
        this.getUsers();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.example.goforlunch.databinding.FragmentWorlkmatesBinding fragmentWorkmatesBinding =
                FragmentWorlkmatesBinding.inflate(inflater,container,false);
        //View view = fragmentWorkmatesBinding.getRoot();
        //configureRecyclerView(fragmentWorkmatesBinding);
        RecyclerView view = fragmentWorkmatesBinding.getRoot();

        Context context = view.getContext();
        view.setLayoutManager(new LinearLayoutManager(context));
        view.setAdapter(adapter);

        return view;
    }

    private void configureViewModel() {
        UserViewModelFactory mViewModelFactory = UserInjection.provideViewModelFactory();
        this.mViewModel = new ViewModelProvider(this, mViewModelFactory).get(UserViewModel.class);
        this.mViewModel.initUsers(this.getContext());
    }

    private void updateUsers (List<User> users) {
        this.users.clear();
        this.users.addAll(users);
        adapter.notifyDataSetChanged();
    }

    private void getUsers() {
        if(this.mViewModel.getUsers() !=null) {
            this.mViewModel.getUsers().observe(this, this::updateUsers);
        }
    }

    //public void configureRecyclerView (FragmentWorlkmatesBinding binding) {
    //    this.adapter = new ListWorkRecyclerViewAdapter(generateOptionsForAdapter(userManager.getAllUsers()));
     //   binding.listWorkRecyclerview.setLayoutManager(new LinearLayoutManager(this.getContext()));
     //   binding.listWorkRecyclerview.setAdapter(this.adapter);


    //}

    public FirestoreRecyclerOptions <User> generateOptionsForAdapter (Query query) {
        return new FirestoreRecyclerOptions.Builder<User>()
            .setQuery(query, User.class)
            .setLifecycleOwner(this)
            .build();
    }















}