package com.example.trailmix;

import android.app.Activity;
import android.content.ContentUris;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Class to play an ArrayList of songs. Allows for Song skipping and playlist cancellation.
 * @author Josh DeOliveira and Andy Goering
 * @date 05/18/2018
 */
public class SongsPlayer {
    private ArrayList <Song> songs;
    private MediaPlayer player;
    private Activity activity;
    private boolean stopped = false;
    long songStartTime;
    long songLength;
    long lastSkipTime;


    /**
     * Automatically starts playing ArrayList of Songs.
     * @param songs
     * @param act (the activity constructing this SongsPlayer)
     */
    public SongsPlayer (ArrayList<Song> songs, Activity act)  {
        lastSkipTime = 0;
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

    /**
     * Starts the SongsPlayer. Begins with the first song in the given ArrayList of Songs.
     * Removes the song just played when it is finished playing it.
     * @throws IOException
     */
    public void startPlayer() throws IOException {
        Log.d("Music", "SongsPlayer's song list:" + songs);
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

    /**
     * Stops the SongsPlayer
     */
    public void stop(){
        player.stop();
        stopped = true;
    }

    /**
     * Skips the current song. It just moves onto the next on in the list.
     * A replacement song is added onto the end of the ArrayList in the replaceSong method in PlaylistGenerator class.
     * @throws IOException
     */
    public void skip() throws IOException{
        //Log.d("Music", "SongsPlayer.skip starting");
        if(System.currentTimeMillis()-lastSkipTime>1000) { //Doesn't allow for skipping more than once a second because that can crash the app.
            lastSkipTime = System.currentTimeMillis();
            stop();
            startPlayer();
        }

       // Log.d("Music", "SongsPlayer.skip starting");
    }

    /**
     * Used for getting a song of an appropriate length to replace a skipped song.
     * @return amount of time left unplayed in the current song.
     */
    public long getRemainingSongTime(){
        long runTime = (System.currentTimeMillis()-songStartTime)/1000;
        return songLength-runTime;

    }




}
