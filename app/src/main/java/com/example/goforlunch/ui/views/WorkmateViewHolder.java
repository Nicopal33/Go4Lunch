package com.example.goforlunch.ui.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.goforlunch.R;


public class WorkmateViewHolder extends RecyclerView.ViewHolder {

    public final ImageView avatar;
    //public final TextView workmateText;
    //public final TextView restName;

    public WorkmateViewHolder(View itemView) {
        super(itemView);
        avatar = itemView.findViewById(R.id.user_image);
        //workmateText = itemView.findViewById(R.id.workmate_text);

    }


}