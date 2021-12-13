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
import com.example.goforlunch.model.User;

import java.util.ArrayList;
import java.util.List;


public class WorlkmatesFragment  extends Fragment  {

    private final List<User> users = new ArrayList<>();
    private RecyclerView recyclerView;
    private ListWorkRecyclerViewAdapter myAdapter;
    private List <User> mUser;


    private final ListWorkRecyclerViewAdapter adapter = new ListWorkRecyclerViewAdapter(users) ;


    public WorlkmatesFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.example.goforlunch.databinding.FragmentWorlkmatesBinding fragmentWorlkmatesBinding =
                FragmentWorlkmatesBinding.inflate(inflater,container,false);
        RecyclerView view = fragmentWorlkmatesBinding.getRoot();
        Context context = view.getContext();
        view.setLayoutManager(new LinearLayoutManager(context));
        view.setAdapter(adapter);

        return view;
    }



    private void updateUsers (List<User> users) {
        this.users.clear();
        this.users.addAll(users);
        adapter.notifyDataSetChanged();
    }








}