package com.example.goforlunch.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.goforlunch.R;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.goforlunch.databinding.WorkmatesItemBinding;
import com.example.goforlunch.model.User;
import com.example.goforlunch.ui.DetailActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    private final List<User> users;

    public UserRecyclerViewAdapter(List<User> users) {this.users = users;}

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder (@NotNull ViewGroup parent, int viewType) {
        com.example.goforlunch.databinding.WorkmatesItemBinding workmatesItemBinding =
                (WorkmatesItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent,false));
        return new ViewHolder(workmatesItemBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, int position) {
        User user = users.get(position);
        String userKnowWhatEatingText = user.getUsername() + " is eating" + user.getRestaurantName();
        String userDoestKnowWhatEating = user.getUsername() + " hasn't decided yet";
        String placeId = user.getRestaurant();
        if(user.getRestaurantName() != null && !user.getRestaurantName().equals("")) {
            holder.userEating.setText(userKnowWhatEatingText);
            holder.userEating.setTextColor(Color.BLACK);
            holder.userEating.setTypeface(holder.userEating.getTypeface(), Typeface.NORMAL);
            holder.mWorkmatesItemBinding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("place Id", placeId);
                startActivity(v.getContext(),intent, null);
            });
        }
        else {
            holder.userEating.setText(userDoestKnowWhatEating);
            holder.userEating.setTypeface(holder.userEating.getTypeface(), Typeface.ITALIC);
            holder.userEating.setTextColor(Color.GRAY);
        }
        if (user.getPicture() != null) {
            Glide.with(holder.userImage.getContext()).load(user.getPicture())
                    .apply(RequestOptions.circleCropTransform()).into(holder.userImage);
        }
        else {
            holder.userImage.setColorFilter(R.color.primary_color);
        }
        Collections.sort(users, new User.UserRestaurantComparator());

    }

    @Override
    public int getItemCount() {return users.size();}

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView userImage;
        public TextView userEating;
        public WorkmatesItemBinding mWorkmatesItemBinding;

        public ViewHolder(WorkmatesItemBinding workmatesItemBinding) {
            super(workmatesItemBinding.getRoot());
            mWorkmatesItemBinding = workmatesItemBinding;
            userImage = workmatesItemBinding.userImage;
            userEating = workmatesItemBinding.workmateText;
        }
        @NotNull
        @Override
        public String toString() {return super.toString();}
    }



}
