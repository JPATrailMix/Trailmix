package com.example.trailmix;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

/**
 * This class retrieves song paths and other metadata to be used later by TimeActivity and SongsPlayer.
 * Much of the code for song retrieval came from this tutorial: https://www.youtube.com/watch?time_continue=1&v=kf2fxYLOiSo
 */
public class SongRetriever {
    private ArrayList<String> songNames;
    private ArrayList<String> songLengths1;
    private ArrayList<String> songPaths;
    private ArrayList<Integer> songLengths;
    private ArrayList<String> artistNames;

    /**
     * Constructs song retriever.
     * @param activity
     */
    public SongRetriever(Activity activity){
        songPaths = new ArrayList<String>();
        songNames = new ArrayList<String>();
        artistNames = new ArrayList<String>();
        songLengths1= new ArrayList<String>();
        songLengths = new ArrayList<Integer>();
        getMusic(activity);
    }

    /**
     * Generates ArrayLists of Song names, durations, and paths.
     * @param activity
     */
    public void getMusic(Activity activity){

        ContentResolver contentResolver = activity.getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI; // magic URI that retrieves all songs in local storage and on the SD card.
        Cursor songCursor = contentResolver.query(songUri, null,null,null,null); //cursor to step through the songs.

        if(songCursor != null && songCursor.moveToFirst()){
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songLocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int songDuration = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

            do{
                //use cursor gets song metadata
                String thisTitle = songCursor.getString(songTitle);
                String thisDuration = songCursor.getString(songDuration);
                String currentLocation = songCursor.getString(songLocation);
                String thisArtist = songCursor.getString(songArtist);

                //add metadata to corresponding ArrayLists.
                songLengths1.add(thisDuration);
                songPaths.add(currentLocation);
                songNames.add(thisTitle);
                artistNames.add(thisArtist);
            } while(songCursor.moveToNext());
        }

        Log.d("Music", "Music info retrieved:");
        for(int i=0; i<songPaths.size();i++){
            Log.d("Music", "Name: " +songNames.get(i));
            Log.d("Music", "Song Length; "+ songLengths1.get(i));
            Log.d("Music", "File Path: "+ songPaths.get(i));
            Log.d("Music", "******************************");
        }
        for(int i=0; i<songLengths1.size(); i++){
            songLengths.add(Integer.parseInt(songLengths1.get(i))/1000);
        }
    }

    /**
     * @return songNames
     */
    public ArrayList<String> getSongNames() {
        return songNames;
    }

    /**
     *
     * @return songLengths
     */
    public ArrayList<Integer> getSongLengths() {
        return songLengths;
    }

    /**
     *
     * @return songPaths
     */
    public ArrayList<String> getSongPaths() {
        return songPaths;
    }
}
