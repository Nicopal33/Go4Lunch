package com.example.goforlunch.bottomfragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;


public class WorlkmatesFragment  extends Fragment  {

    private RecyclerView recyclerView;
    private UserManager userManager = UserManager.getInstance();


    private  ListWorkRecyclerViewAdapter adapter;


    public WorlkmatesFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.example.goforlunch.databinding.FragmentWorlkmatesBinding fragmentWorlkmatesBinding =
                FragmentWorlkmatesBinding.inflate(inflater,container,false);
        View view = fragmentWorlkmatesBinding.getRoot();
        configureRecyclerView(fragmentWorlkmatesBinding);

        return view;
    }

    public void configureRecyclerView (FragmentWorlkmatesBinding binding) {
        this.adapter = new ListWorkRecyclerViewAdapter(generateOptionsForAdapter(userManager.getAllUsers()));
        binding.listWorkRecyclerview.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.listWorkRecyclerview.setAdapter(this.adapter);


    }

    public FirestoreRecyclerOptions <User> generateOptionsForAdapter (Query query) {
        return new FirestoreRecyclerOptions.Builder<User>()
            .setQuery(query, User.class)
            .setLifecycleOwner(this)
            .build();
    }












}