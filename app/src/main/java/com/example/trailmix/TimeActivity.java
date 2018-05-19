package com.example.trailmix;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This program plays music and counts how much time there is remaining.
 * @author Patrick Tan with additions by Andy Goering and Josh DeOliveira
 * @date 05/18/2018
 */
public class TimeActivity extends AppCompatActivity {
    private SongsPlayer songsPlayer;
    private Playlist playlist;
    private ArrayList<Song> songs;
    private ArrayList<Song> origSongs;
    private long timerTimeRemaining;
    private CountDownTimer timer;
    private long lastSkipTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        Bundle bundle = getIntent().getExtras();
        lastSkipTime = 0;
        double time = bundle.getDouble("MinutesTime");
        SongRetriever sr = new SongRetriever(this);
        long startTime = System.currentTimeMillis();
        songs = PlaylistGenerator.generateSongs(sr.getSongNames(),sr.getSongPaths(),sr.getSongLengths());
        origSongs = (ArrayList<Song>) songs.clone();
        Log.d("Music", "ArrayList of songs right before the PGA runs:" + songs);
        try {
            playlist = PlaylistGenerator.generatePlaylist(songs, (int) (time * 60));

            int targetTime = (int) (time * 60);
            Log.d("Music", "hi there, target time is " + targetTime + "s");
            Log.d("Music", "Playlist: " + playlist);
            Log.d("Music", "Entire PGA and song retrieval took " + (System.currentTimeMillis() - startTime) + "ms to run");

            songsPlayer = new SongsPlayer(playlist.getSongs(), this);
            Log.d("Countdown", "Countdown about to begin");
            int millis = (int) (time * 60 * 1000);

            //Timer counts down the time left on the playlist and display the current song playing
            // Timer code from https://developer.android.com/reference/android/os/CountDownTimer
            timer = new CountDownTimer(millis, 1000) {
                TextView tv = (TextView) findViewById(R.id.time);
                TextView sN = (TextView) findViewById(R.id.songName);

                public void onTick(long millisUntilFinished) {
                    sN.setText(playlist.getSongs().get(0).getName()); //display song playing

                    //display time correctly in minutes in seconds
                    long seconds = millisUntilFinished / 1000;
                    if (seconds % 60 < 10) {
                        tv.setText("" + seconds / 60 + ":0" + seconds % 60);
                    } else {
                        tv.setText("" + seconds / 60 + ":" + seconds % 60);
                    }
                    Log.d("Countdown", "" + seconds);
                    timerTimeRemaining = seconds;
                }

                public void onFinish() {
                    tv.setText("done!");

                    // stop the SongsPlayer even if it is still playing. This may occur in
                    // certain cases where the user provides an inadequate number of songs.
                    songsPlayer.stop();
                    startFinishedActivity();
                }
            }.start();
        }
        catch(IOException e){ // Tell user to go get some songs if they don't have any.
            Toast getSomeSongs = Toast.makeText(getApplicationContext(),"No songs found in native storage. Go buy some songs!",Toast.LENGTH_LONG);
            getSomeSongs.show();
        }
    }


    private void startFinishedActivity(){
        Intent intent2 = new Intent(this,FinishedActivity.class);
        startActivity(intent2);
    }

    /**
     * cancel prompt
     * @param view
     */
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

    /**
     * if the user says yes to the cancel prompt
     * @param view
     */
    public void onYes(View view) {
        songsPlayer.stop();
        Intent intent2 = new Intent(this, FinishedActivity.class);
        startActivity(intent2);
    }

    /**
     * if the user says no to the cancel prompt, hide the cancel option buttons
     * @param view
     */
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

    /**
     * If the user presses the skip button, start the skipping process.
     * Doesn't allow the user to skip more than once per second.
     * @param view
     */
    public void onSkip(View view) {
        if (System.currentTimeMillis() - lastSkipTime > 1000) {
            lastSkipTime = System.currentTimeMillis();
            if (timerTimeRemaining > 30) {
                Log.d("Music", "Starting TimeActivity.onSkip");
                PlaylistGenerator.replaceSong(playlist, songs, origSongs, songsPlayer.getRemainingSongTime(), 0, System.currentTimeMillis());
                // Log.d("Music", "Finished switching song in playlist in TimeActivity.onSkip");
                // Log.d("Music", "New playlist after PlaylistGenerator.replaceSong: " + playlist);
                try {
                    songsPlayer.skip();
                } catch (IOException e) {
                    //  Log.d("Music", "TimeActivity.onSkip failed");
                    e.printStackTrace();
                }
                Log.d("Music", "Finished TimeActivity.onSkip");
            } else {
                songsPlayer.stop();
                timer.onFinish();
            }
        }
        else {
            Toast coolIt = Toast.makeText(getApplicationContext(), "chill dude. cool it. one skip per second.", Toast.LENGTH_LONG);
            coolIt.show();
        }
    }

}