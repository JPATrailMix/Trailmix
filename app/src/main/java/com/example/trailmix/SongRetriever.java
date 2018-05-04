package com.example.trailmix;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by andrewgoering on 4/2/18.
 * I figured out how to do this using this tutorial: https://www.youtube.com/watch?time_continue=1&v=kf2fxYLOiSo
 */

public class SongRetriever {
    private ArrayList<String> songNames;
    private ArrayList<String> songLengths1;
    private ArrayList<String> songPaths;
    private ArrayList<Integer> songLengths;

    public SongRetriever(Activity act){
        songPaths = new ArrayList<String>();
        songNames = new ArrayList<String>();
        songLengths1= new ArrayList<String>();
        songLengths = new ArrayList<Integer>();
        getMusic(act);
    }

    public void getMusic(Activity act){
       /* ContentResolver musicResolver = act.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int iDColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media._ID);
            int lengthColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DURATION);
            //add songs to list
            do {
                int thisId = musicCursor.getInt(iDColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisDuration = musicCursor.getString(lengthColumn);
                songLengths1.add(thisDuration);
                songIDs1.add(thisId);
                songNames.add(thisTitle);
            }
            while (musicCursor.moveToNext());
        }

        for(int i=0; i<songLengths1.size(); i++){
            songLengths.add(Integer.parseInt(songLengths1.get(i))/1000);
            songIds.add((long)songIDs1.get(i));

        }*/

        ContentResolver contentResolver = act.getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null,null,null,null);

        if(songCursor != null && songCursor.moveToFirst()){
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songLocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int songDuration = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

            do{
                String thisTitle = songCursor.getString(songTitle);
                String thisDuration = songCursor.getString(songDuration);
                String currentLocation = songCursor.getString(songLocation);
                songLengths1.add(thisDuration);
                songPaths.add(currentLocation);
                songNames.add(thisTitle);
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

    public ArrayList<String> getSongNames() {
        return songNames;
    }

    public ArrayList<Integer> getSongLengths() {
        return songLengths;
    }

   /* public ArrayList<Long> getSongIds() {
        return songIds;
    }*/

    public ArrayList<String> getSongPaths() {
        return songPaths;
    }
}
