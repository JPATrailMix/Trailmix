package com.example.trailmix;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by andrewgoering on 4/2/18.
 */

public class SongRetriever {
    private ArrayList<String> songNames;
    private ArrayList<String> songLengths1;
    private ArrayList<Integer> songIDs1;
    private ArrayList<Long> songIds;
    private ArrayList<Integer> songLengths;

    public SongRetriever(Activity act){
        songNames = new ArrayList<String>();
        songLengths1= new ArrayList<String>();
        songIDs1 = new ArrayList<Integer>();
        songLengths = new ArrayList<Integer>();
        songIds = new ArrayList<Long>();
        getMusic(act);
    }

    public void getMusic(Activity act){
        ContentResolver musicResolver = act.getContentResolver();
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

        }
        for(int i=0; i<songIds.size();i++){
            Log.d("Songs", "Name: " +songNames.get(i));
            Log.d("Songs", "Song Length; "+ songLengths.get(i));
            Log.d("Songs", "File Path: "+ songIds.get(i));
            Log.d("Songs", "****************");
        }
    }

    public ArrayList<String> getSongNames() {
        return songNames;
    }

    public ArrayList<Integer> getSongLengths() {
        return songLengths;
    }

    public ArrayList<Long> getSongIds() {
        return songIds;
    }
}
