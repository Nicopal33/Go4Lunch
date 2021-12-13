package com.example.goforlunch.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.example.goforlunch.R;
import com.example.goforlunch.databinding.FragmentWorlkmatesBinding;
import com.example.goforlunch.databinding.WorkmatesItemBinding;
import com.example.goforlunch.model.User;
import com.example.goforlunch.ui.DetailRest;
import com.example.goforlunch.views.WorkmateViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static android.os.Build.VERSION_CODES.R;
import static androidx.core.content.ContextCompat.startActivity;

public class ListWorkRecyclerViewAdapter
        extends FirestoreRecyclerAdapter<User, WorkmateViewHolder> {

    public ListWorkRecyclerViewAdapter(FirestoreRecyclerOptions<User> options) {
        super(options);
        }

    @NonNull
    @Override
    public WorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WorkmatesItemBinding workmatesItemBinding =
                (WorkmatesItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        return new WorkmateViewHolder(workmatesItemBinding.getRoot());
    }


    @Override
    protected void onBindViewHolder(@NonNull WorkmateViewHolder holder, int position, @NonNull User user) {
        String userKnowWhatEatingText = user.getUserName() + "is eating" + user.getRestaurantName();
        String userDontKnowWhatEating = user.getUserName() + "hasn't decided yet";
        String placeId = user.getRestaurant();
        if (user.getRestaurantName() !=null && !user.getRestaurantName().equals("")) {
            holder.workmateText.setText(userDontKnowWhatEating);
            //holder.mWorkmatesItemBinding.getRoot().setOnClickListener(v -> {
           //     Intent intent = new Intent(v.getContext(), DetailRest.class);
             //   intent.putExtra("place Id", placeId);
             //   startActivity(v.getContext(),intent,null);
           // });
        }
        else {
            holder.workmateText.setText(userDontKnowWhatEating);
        }
        //if (user.getPicture() !=null) {
        //    Glide.with(holder.workmatePhoto.getContext()).load(user.getPicture())
        //            .apply(RequestOptions.circleCropTransform()).into(holder.workmatePhoto);
       // }
       // else {
        //    holder.workmatePhoto.setColorFilter(Integer.parseInt("@color/primary_color"));
       // }

    }



}
