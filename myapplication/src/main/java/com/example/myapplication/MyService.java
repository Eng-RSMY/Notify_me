package com.example.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    //this class to interaction between service and acrivity to return to activity
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference  SensorRef=database.getReference("Sensors");

        SensorRef.child("fireStatus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if(value.equals("true"))
                {
                    notifi();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        SensorRef.child("gasStatus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if(value.equals("true"))
                {
                    notifi();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        //active the service when it killed by another when it active
        return START_STICKY;

    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    private void notifi()
    {
        //responsibility of charastaristics
        NotificationCompat.Builder builder= new  NotificationCompat.Builder(this,"1");
        //set charastaristics
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Attention !!!!!" );
        builder.setContentText("Deticting fire ");
        //vibrattion setting two vibration (vibration ,sleep)
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000,1000,1000,1000});
        //color of notification
        builder.setLights(Color.BLUE,3000,3000);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(uri);


        //intent we want to go on when press on button
        Intent notificationIntent = new Intent(this,MainActivity.class);
        //link with intent
        PendingIntent contentIntent =PendingIntent.getActivity(this,0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        //Active the notification to appear in mobile channel
        NotificationManager manager=(  NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //apply all charastaristic of notifivation
        manager.notify(0,builder.build());

    }






}
