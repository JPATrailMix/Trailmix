package com.example.trailmix;

import android.app.Activity;
import android.content.ContentUris;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class SongsPlayer {
    private ArrayList <Song> songs;
    private MediaPlayer player;
    private Activity activity;
    private boolean stopped = false;
    long songStartTime;
    long songLength;


    public SongsPlayer (ArrayList<Song> songs, Activity act)  {
        this.songs = songs;
        activity = act;
        try {
            startPlayer();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    public void startPlayer() throws IOException {
        //Log.d("Music", "SongsPlayer's song list:" + songs);
        stopped = false;
        if(songs.size() != 0) {
            songLength = songs.get(0).getTime();
            player = new MediaPlayer();
            if(songs.get(0).getPath()==null) {
                long id = songs.get(0).getId();
                Uri contentUri = ContentUris.withAppendedId(
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setDataSource(activity.getApplicationContext(), contentUri);
            }
            else{
                player.setDataSource(songs.get(0).getPath());
            }
            player.prepare();
            player.start();
            songStartTime = System.currentTimeMillis();
            //Log.d("Music", "Name: " + songs.get(0).getName());
            //Log.d("Music", "Artist: " + songs.get(0).getArtist());
           // Log.d("Music", "SongsPlayer's song list:" + songs);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    songs.remove(0);
                    try {
                        if(!stopped)
                            startPlayer();
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void startPlayer(boolean thing) throws IOException {
        while(songs.size() != 0) {
            player = new MediaPlayer();
            if(songs.get(0).getPath()==null) {
                long id = songs.get(0).getId();
                Uri contentUri = ContentUris.withAppendedId(
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setDataSource(activity.getApplicationContext(), contentUri);
            }
            else{
                player.setDataSource(songs.get(0).getPath());
            }
            player.prepare();
            player.start();
            Log.d("Music", "Name: " + songs.get(0).getName());
            Log.d("Music", "Artist: " + songs.get(0).getArtist());
            while(player.isPlaying()){
                Log.d("Hi", "while loops are fun");
            }
            player.stop();
            songs.remove(0);


        }
    }

    public void stop(){
        player.stop();
        stopped = true;
    }

    public void skip() throws IOException{
        //Log.d("Music", "SongsPlayer.skip starting");
        stop();

        startPlayer();
       // Log.d("Music", "SongsPlayer.skip starting");
    }

    public long getRemainingSongTime(){
        long runTime = (System.currentTimeMillis()-songStartTime)/1000;
        return songLength-runTime;

    }




}
