package com.example.trailmix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Music", "Song Retriever Loading");
    }
    public void getDuration(View v){
        Log.d("Music", "getDuration starting");
        EditText timeText = findViewById(R.id.editText);
        String timeStr = timeText.getText().toString();
        if(timeStr.isEmpty())
            timeStr = "0";

        Log.d("Music", timeStr);
        Double timeD = Double.parseDouble(timeStr);
        Log.d("Music", "TargetTime = " + timeD*60);

        Log.d("Music", "parse double worked maybe");
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
