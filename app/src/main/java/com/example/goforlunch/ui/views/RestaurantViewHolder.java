package com.example.goforlunch.ui.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.goforlunch.R;

public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    final ImageView photo;
    final TextView name;
    final TextView txt;
    final TextView opening;
    final TextView distance;
    final TextView nbWorkmates;
    final RatingBar ratingBar;


    public RestaurantViewHolder(View itemView) {
        super(itemView);

        photo = itemView.findViewById(R.id.restaurant_photo);
        name = itemView.findViewById(R.id.restaurant_name);
        txt = itemView.findViewById(R.id.restaurant_text);
        opening = itemView.findViewById(R.id.restaurant_opening);
        distance = itemView.findViewById(R.id.restaurant_distance);
        nbWorkmates = itemView.findViewById(R.id.restaurant_nbr_work);
        ratingBar = itemView.findViewById(R.id.restaurant_rating_bar);




    }
}
