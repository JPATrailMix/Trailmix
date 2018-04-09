package com.example.trailmix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "Song Retriever Loading");
    }
    public void getDuration(View v){
        Log.d("getDuration", "getDuration starting");
        EditText timeText = findViewById(R.id.editText);
        String timeStr = timeText.getText().toString();

        Log.d("getDuration", timeStr);
        Double timeD = Double.parseDouble(timeStr);
        Log.d("PGA", "TargetTime = " + timeD*60);

        Log.d("getDuration", "parse double worked maybe");
        SongRetriever sr = new SongRetriever(this);
        ArrayList<Song> songs = PlaylistGenerator.generateSongs(sr.getSongNames(),sr.getSongIds(),sr.getSongLengths());
        Playlist playlist = PlaylistGenerator.generatePlaylist(songs,(int)(timeD*60));
        long startTime = System.currentTimeMillis();
        int targetTime = (int)(timeD*60);
        Log.d("PGA", "hi there, target time is " + targetTime + "s");
        Log.d("PGA", "Playlist: " + playlist);
        Log.d("PGA", "Entire PGA took " + (System.currentTimeMillis() - startTime) + "ms to run");

        Intent intent = new Intent(this,TimeActivity.class);
       intent.putExtra("time", timeD);
       startActivity(intent);
       Log.d("getDuration", "time activity intent started");
    }
}
