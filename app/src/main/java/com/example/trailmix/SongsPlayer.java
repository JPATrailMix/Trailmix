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

        if(songs.size() != 0) {
            player = new MediaPlayer();
            long id = songs.get(0).getId();
            Uri contentUri = ContentUris.withAppendedId(
                    android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(activity.getApplicationContext(), contentUri);
            player.prepare();
            player.start();
            Log.d("Music", "Name: " + songs.get(0).getName());
            Log.d("Music", "Artist: " + songs.get(0).getArtist());
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

    public void stop(){
        player.stop();
        stopped = true;
    }


}
