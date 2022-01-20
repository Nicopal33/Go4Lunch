package com.example.goforlunch.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.work.WorkerParameters;

import com.example.goforlunch.R;
import com.example.goforlunch.model.User;
import com.example.goforlunch.repository.UserCRUD;
import com.example.goforlunch.ui.views.ConnexionActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.transform.Result;

public class Worker extends androidx.work.Worker {

    private String message;

    private String mWorkmates;

    private final List<User> mUsers = new ArrayList<>();

    public Worker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Result doWork() {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        UserCRUD.getUser(uid).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null) {
                User currentUser = documentSnapshot.toObject(User.class);
                String username = Objects.requireNonNull(currentUser).getUsername();
                String restaurantName = currentUser.getRestaurantName();
                String restaurantAddress = currentUser.getRestaurantAddress();


                UserCRUD.getUsers().addOnSuccessListener(documentsSnapshot -> {
                    List<User> users = new ArrayList<>();
                    if (documentsSnapshot != null) {
                        for (DocumentSnapshot documentSnapshot1 : documentsSnapshot.getDocuments()) {
                            User user = documentSnapshot1.toObject(User.class);
                            if (Objects.equals(Objects.requireNonNull(user).getRestaurant(),
                                    currentUser.getRestaurant()) && user
                                    .getUid().equals(currentUser.getUid())) {
                                users.add(user);
                                mUsers.addAll(users);
                                List<String> names = new ArrayList<>();
                                names.add(user.getUsername());
                                mWorkmates = String.join(",", names);
                            }
                        }

                        if (mUsers.size() != 0) {
                            message = "Hello" + username +
                                    "it's Time to lunch you'll eat at" +
                                    restaurantName + " at " + restaurantAddress + " with " +
                                    mWorkmates;
                        }else if (currentUser.getRestaurant() == null ||
                                currentUser.getRestaurant().equals("")) {
                            message  = "Hello " + username + " let's choose a restaurant";
                        }else {
                            message = "Hello " + username + "it's Time to lunch you'll eat at "
                                    + restaurantName + restaurantAddress;
                        }
                        sendVisualNotification(message);
                    }
                });
            }
        });
        return Result.success();
    }

    private void sendVisualNotification(String message) {

        Intent intent = new Intent(this.getApplicationContext(), ConnexionActivity.class);
        int id = 26;
        PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), id,
                intent, PendingIntent.FLAG_ONE_SHOT);

        String notificationCanal = "password";
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this.getApplicationContext(), notificationCanal)
                        .setSmallIcon(R.drawable.logo_go4lunch)
                        .setContentTitle
                                ("Time To lunch")
                        .setContentText(message)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(message))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) this.getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            String notificationName = "Time To Lunch !";
            NotificationChannel mChannel = new NotificationChannel(this.getApplicationContext()
                    .getString(R.string.notification_chanel_id), notificationName,
                    importance);
            notificationManager.createNotificationChannel(mChannel);
            notificationManager.notify(notificationName, id, notificationBuilder.build());
        }
    }
}

