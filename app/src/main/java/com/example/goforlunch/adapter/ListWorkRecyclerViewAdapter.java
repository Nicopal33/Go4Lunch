/*package com.example.goforlunch.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.goforlunch.R;
import androidx.annotation.NonNull;
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

public class ListWorkRecyclerViewAdapter
        extends RecyclerView.Adapter<ListWorkRecyclerViewAdapter.ViewHolder> {

    private final List<User> users;

    public ListWorkRecyclerViewAdapter(List<User> users) {this.users = users; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        com.example.goforlunch.databinding.WorkmatesItemBinding workmatesItemBinding =
                (WorkmatesItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent, false));
        return new ViewHolder(workmatesItemBinding);
    }

    @Override
    public void onBindViewHolder (@NotNull final ViewHolder holder, int position) {
        User user = users.get(position);
        String userKnowWhatEatingText = user.getUsername() +
                "it's eating" + user.getRestaurantName();
        String userDoestKnowWhatEating = user.getUsername() + "hasn't decided yet";
        String placeId = user.getRestaurant();
        if (user.getRestaurantName() !=null && !user.getRestaurantName().equals("")) {
            holder.workmateText.setText(userKnowWhatEatingText);
            holder.workmateText.setTextColor(Color.BLACK);
            holder.workmateText.setTypeface(holder.workmateText.getTypeface(), Typeface.NORMAL);
            holder.mWorkmatesBinding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("place Id", placeId);
                startActivity(v.getContext(), intent, null);
            });
        }
        else {
            holder.workmateText.setText(userDoestKnowWhatEating);
            holder.workmateText.setTypeface(holder.workmateText.getTypeface(), Typeface.ITALIC);
            holder.workmateText.setTextColor(Color.GRAY);
        }
        if (user.getPicture() !=null) {
            Glide.with(holder.workmatePhoto.getContext()).load(user.getPicture())
                    .apply(RequestOptions.circleCropTransform()).into(holder.workmatePhoto);
        }
        else {
            holder.workmatePhoto.setColorFilter(R.color.primary_color);
        }
        Collections.sort(users, new User.UserRestaurantComparator());
}

    @Override
    public int getItemCount() { return users.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView workmatePhoto;
        public TextView workmateText;
        public WorkmatesItemBinding mWorkmatesBinding;

        public ViewHolder (WorkmatesItemBinding workmatesItemBinding) {
            super(workmatesItemBinding.getRoot());
           mWorkmatesBinding = workmatesItemBinding;
            workmatePhoto = workmatesItemBinding.workmatePhoto;
            workmateText = workmatesItemBinding.workmateText;
        }
    }

    @NotNull
    @Override
    public String toString () {return super.toString();
    }
}*/
