package com.example.trailmix;
/*
Author: Patrick Tan
This program plays music and counts how much time there is remaining.
 */
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TimeActivity extends AppCompatActivity {
    private SongsPlayer songsPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        Bundle bundle = getIntent().getExtras();

        double time = bundle.getDouble("MinutesTime");
        SongRetriever sr = new SongRetriever(this);
        long startTime = System.currentTimeMillis();

        ArrayList<Song> songs = PlaylistGenerator.generateSongs(sr.getSongNames(),sr.getSongIds(),sr.getSongLengths());
        try {
            Playlist playlist = PlaylistGenerator.generatePlaylist(songs, (int) (time * 60));

            int targetTime = (int) (time * 60);
            Log.d("Music", "hi there, target time is " + targetTime + "s");
            Log.d("Music", "Playlist: " + playlist);
            Log.d("Music", "Entire PGA took " + (System.currentTimeMillis() - startTime) + "ms to run");

            songsPlayer = new SongsPlayer(playlist.getSongs(), this);

            Log.d("Countdown", "Countdown about to begin");
            int millis = (int) (time * 60 * 1000);
            new CountDownTimer(millis, 1000) {
                TextView tv = (TextView) findViewById(R.id.time);

                public void onTick(long millisUntilFinished) {
                    long seconds = millisUntilFinished / 1000;
                    if (seconds % 60 < 10) {
                        tv.setText("" + seconds / 60 + ":0" + seconds % 60);
                    } else {
                        tv.setText("" + seconds / 60 + ":" + seconds % 60);
                    }
                    Log.d("Countdown", "" + seconds);
                }

                public void onFinish() {
                    tv.setText("done!");
                    startFinishedActivity();
                }
            }.start();
        }
        catch(IOException e){
            Toast passiveAgressive = Toast.makeText(getApplicationContext(),"No songs found in native storage. Get a life.",Toast.LENGTH_LONG);
            passiveAgressive.show();
        }
    }
    private void startFinishedActivity(){
        Intent intent2 = new Intent(this,FinishedActivity.class);
        startActivity(intent2);
    }

    public void onCancel(View view){

        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setVisibility(View.INVISIBLE);
        TextView sure = (TextView) findViewById(R.id.sure);
        sure.setVisibility(View.VISIBLE);
        Button confirm = (Button) findViewById(R.id.confirm);
        confirm.setVisibility(View.VISIBLE);
        Button nonconfirm = (Button) findViewById(R.id.nonconfirm);
        nonconfirm.setVisibility(View.VISIBLE);
    }
    public void onYes(View view){
        songsPlayer.stop();
        Intent intent2 = new Intent(this,FinishedActivity.class);
        startActivity(intent2);
    }
    public void onNo(View view){
        TextView sure = (TextView) findViewById(R.id.sure);
        sure.setVisibility(View.INVISIBLE);
        Button confirm = (Button) findViewById(R.id.confirm);
        confirm.setVisibility(View.INVISIBLE);
        Button nonconfirm = (Button) findViewById(R.id.nonconfirm);
        nonconfirm.setVisibility(View.INVISIBLE);
        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setVisibility(View.VISIBLE);
    }
}