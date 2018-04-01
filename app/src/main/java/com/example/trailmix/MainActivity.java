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
        long startTime = System.currentTimeMillis();
        int targetTime = 60 * 60;
        Log.d("PGA", "hi there");
        Song[] songs = PlaylistGenerator.createRandomSongList(10000, 120, 300);
        Log.d("PGA","Songs: " + Arrays.toString(songs));
        Log.d("PGA", "Playlist: " + PlaylistGenerator.generatePlaylist(new ArrayList<Song>(Arrays.asList(songs)), targetTime));
        Log.d("PGA", "Entire PGA took " + (System.currentTimeMillis() - startTime) + "ms to run");

    }
    public void getDuration(View v){
        System.out.println("Hello There This is Duration a;lsdkf;alsdkfj;aldjksf;aljksdf;lajksdf;lkajsd;flkja;sdlkfja;ldksfja;ldksfj");
        Log.d("getDuration", "getDuration starting");
        EditText timeText = findViewById(R.id.editText);
        String timeStr = timeText.getText().toString();

        Log.d("getDuration", timeStr);
        Double timeD = Double.parseDouble(timeStr);

        Log.d("getDuration", "parse double worked maybe");

        Intent intent = new Intent(this,TimeActivity.class);
       intent.putExtra("time", timeD);
       startActivity(intent);
       Log.d("getDuration", "time activity intent started");
    }
}
