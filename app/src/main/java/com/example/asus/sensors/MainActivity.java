package com.example.asus.sensors;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private String TAG = "Main Activity";
    //port num
    private static final String Flame_pins = "BCM17";
    private static final String gas_Pins = "BCM27";

    //gpio sensor
    Gpio fireGpio;
    Gpio gasGpio;

    private FirebaseDatabase database;
    private DatabaseReference   sensorRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database=FirebaseDatabase.getInstance();
      sensorRef=database.getReference("Sensors");



        Log.i(TAG, "No GPIO port available on this device.");
        PeripheralManager var = PeripheralManager.getInstance();  // open object to can use the method in bulid app
        Log.i(TAG, "GPIO" + var.getGpioList());  // to know name of pins in raspberryList<String> portList = manager.getGpioList();
        List<String> portList = var.getGpioList();
        if (portList.isEmpty()) {
            Log.i(TAG, "No GPIO port available on this device.");
        } else {
            Log.i(TAG, "List of available ports: " + portList);
        }
        try {
            //open connection
            fireGpio= var.openGpio(Flame_pins);
            gasGpio=var.openGpio(gas_Pins);
            // type Input
            fireGpio.setDirection(Gpio.DIRECTION_IN);
            gasGpio.setDirection(Gpio.DIRECTION_IN);

            fireGpio.setEdgeTriggerType(Gpio.EDGE_BOTH);
            gasGpio.setEdgeTriggerType(Gpio.EDGE_BOTH);

            fireGpio.setActiveType(Gpio.ACTIVE_HIGH);
            fireGpio.registerGpioCallback(callback);

            gasGpio.setActiveType(Gpio.ACTIVE_HIGH);
            gasGpio.registerGpioCallback(gcallback);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private GpioCallback callback = new GpioCallback() {


        @Override
        public boolean onGpioEdge(Gpio gpio) {
            Log.i(TAG, "okk" );
            try {
                if(gpio.getValue()){

                    //pin is high
                    sensorRef.child("fireStatus").setValue("true");
                }
                else{
                    //pin is low
                    sensorRef.child("fireStatus").setValue("false");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;

        }

    };

    private GpioCallback gcallback = new GpioCallback() {
        @Override
        public boolean onGpioEdge(Gpio gpio) {
            Log.i(TAG, "GPIO changed , gas occur");
            try {
                if(gpio.getValue()){
                    //pin is high
                    sensorRef.child("gasStatus").setValue("true");
                }
                else{
                    //pin is low
                    sensorRef.child("gasStatus").setValue("false");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }

    };

}



