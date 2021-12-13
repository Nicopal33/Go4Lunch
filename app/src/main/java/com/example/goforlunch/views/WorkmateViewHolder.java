package com.example.goforlunch.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.example.goforlunch.R;


public class WorkmateViewHolder extends RecyclerView.ViewHolder {

    final ImageView avatar;
    final TextView workmateText;
    final TextView restName;

    public WorkmateViewHolder(View itemView) {
        super(itemView);

        avatar = itemView.findViewById(R.id.workmate_photo);
        workmateText = itemView.findViewById(R.id.workmate_text);
        restName = itemView.findViewById(R.id.rest_name);

    }
}

