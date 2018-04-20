package com.example.trailmix;

import android.content.Context;
import android.content.Intent;
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
    public void getDuration(View v){
        Context context = getApplicationContext();
        CharSequence text = "Please enter a time at least 3 minutes and less than 600 minutes";
        int duration = Toast.LENGTH_LONG;


        Log.d("Music", "getDuration starting");
        EditText timeText = findViewById(R.id.editText);
        String timeStr = timeText.getText().toString();
//        while(timeStr.isEmpty()||timeStr.equals("0")){
//            timeText = findViewById(R.id.editText);
//            timeStr = timeText.getText().toString();
//        }
        if(timeStr.isEmpty())
            timeStr = "0";

        Log.d("Music", timeStr);
        Double timeD = Double.parseDouble(timeStr);
        Log.d("Music", "TargetTime = " + timeD*60);
        Log.d("Music", "parse double worked");
        if(timeD < 3 || timeD >= 600){
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else{
            SongRetriever sr = new SongRetriever(this);
            long startTime = System.currentTimeMillis();

            ArrayList<Song> songs = PlaylistGenerator.generateSongs(sr.getSongNames(),sr.getSongIds(),sr.getSongLengths());
            Playlist playlist = PlaylistGenerator.generatePlaylist(songs,(int)(timeD*60));

            int targetTime = (int)(timeD*60);
            Log.d("Music", "hi there, target time is " + targetTime + "s");
            Log.d("Music", "Playlist: " + playlist);
            Log.d("Music", "Entire PGA took " + (System.currentTimeMillis() - startTime) + "ms to run");

            Intent intent = new Intent(this,TimeActivity.class);
           intent.putExtra("time", timeD);
           startActivity(intent);
           Log.d("Music", "time activity intent started");
           SongsPlayer sp = new SongsPlayer(playlist.getSongs(),this);
        }
    }
    public void getLocation(View view){
//        Intent intentMaps= new Intent(this, MapsActivity.class);
//        startActivity(intentMaps);

    }
}
