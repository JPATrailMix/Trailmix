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
        int targetTime = 60 * 60;
        Log.d("PGA", "hi there");
        Song[] songs = PlaylistGenerator.createRandomSongList(5, 120, 300);
        Log.d("PGA","Songs: " + Arrays.toString(songs));
        Log.d("PGA", "Playlist: " + PlaylistGenerator.generatePlaylist(new ArrayList<Song>(Arrays.asList(songs)), targetTime));

    }
    public void getDuration(View v){
        EditText timeText = findViewById(R.id.editText);
        String timeStr = timeText.getText().toString();

        Log.d("MainActivity", timeStr);
        Double timeD = Double.parseDouble(timeStr);

        Intent intent = new Intent(this,TimeActivity.class);
        intent.putExtra("time", timeD);
        startActivity(intent);
    }
}
