package com.example.myapplication;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    public Button gobtn;

    //Fire
    FirebaseDatabase database;
    DatabaseReference fireRef;
    DatabaseReference gasRef;

    private String TAG = "MainActivity";
    ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        imageView=findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.ic_ok);
//active the vibration service for app
        Intent intent = new Intent( this, MyService.class );
        startService( intent );



// Sensors

        database = FirebaseDatabase.getInstance();
        fireRef = database.getReference("Sensors/fireStatus");
        gasRef =database.getReference("Sensors/gasStatus");
        checkStatusFire();
        checkStatusGas();


       //############################### Sensors

    }



    // Method to check sensors status
    private void checkStatusFire() {
        fireRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

// This method is called once with the initial value and again
// whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if(value.equals("true")) {

                    Log.i(TAG, "GPIO changed , fire occur");
                    imageView.setImageResource(R.drawable.attentionfire);

                }
                else{


                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
    private void checkStatusGas() {
        gasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

// This method is called once with the initial value and again
// whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if(value.equals("true")) {

                    Log.i(TAG, "GPIO changed , gas occur");
                    imageView.setImageResource(R.drawable.flammablegases);

                }
                else{


                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }


    }
