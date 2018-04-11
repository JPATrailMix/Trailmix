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
    ArrayList <Song> songs;
    MediaPlayer player;
    Activity activity;


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

    public void startPlayer() throws IOException {
        for (int i=0; i<songs.size();i++) {

            long id = songs.get(i).getId();
            Uri contentUri = ContentUris.withAppendedId(
                    android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

            player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(activity.getApplicationContext(), contentUri);

            player.prepare();
            player.start();
            Log.d("Music", "Name: " + songs.get(i).getName());
            Log.d("Music", "Artist: " + songs.get(i).getArtist());
        }
    }
}
