package com.example.goforlunch.model.details;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.goforlunch.adapter.UserRecyclerViewAdapter;
import com.example.goforlunch.databinding.WorkmatesItemBinding;
import com.example.goforlunch.model.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DetailsRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    private final List<User> users;

    public DetailsRecyclerViewAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @NotNull
    @Override
    public UserRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent,
                                                                 int viewType) {
        com.example.goforlunch.databinding.WorkmatesItemBinding workmatesItemBinding =
                (WorkmatesItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent, false));
        return new UserRecyclerViewAdapter.ViewHolder(workmatesItemBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull UserRecyclerViewAdapter.ViewHolder holder,
                                 int position) {
        User user = users.get(position);
        String userJoining = user.getUsername() + "is joining !";
        holder.userEating.setText(userJoining);
        Glide.with(holder.userImage).load(user.getPicture()).apply(RequestOptions
        .circleCropTransform()).into(holder.userImage);
    }

    @Override
    public int getItemCount() {return users.size();}



}
