package com.example.trailmix;
/*
Author: Patrick Tan
This is the launch screen.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Music", "Song Retriever Loading");
    }
    //when there is an input of time
    public void getDuration(View v){
        Context context = getApplicationContext();
        CharSequence text = "Please enter a time at least 3 minutes and less than 600 minutes";
        int duration = Toast.LENGTH_LONG;

        Log.d("Music", "getDuration starting");
        EditText timeText = findViewById(R.id.editText);
        String timeStr = timeText.getText().toString();
        //starts timer
        if(timeStr.isEmpty())
            timeStr = "0";

        Log.d("Music", timeStr);
        Double timeD = Double.parseDouble(timeStr);
        Log.d("Music", "TargetTime = " + timeD*60);

        if(timeD < 3 || timeD >= 600){
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else{
           // SongRetriever sr = new SongRetriever(this);
            long startTime = System.currentTimeMillis();

        Intent intent = new Intent(this,TimeActivity.class);
        intent.putExtra("MinutesTime", timeD);

        startActivity(intent);
        Log.d("Music", "time activity intent started");
        }
    }
    //if need be, this is the get location option
    public void getLocation(View view){
        Intent intentMaps= new Intent(this,MapsActivity.class);
        startActivity(intentMaps);

    }
}